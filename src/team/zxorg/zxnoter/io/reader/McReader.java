package team.zxorg.zxnoter.io.reader;

import com.google.gson.Gson;
import team.zxorg.zxnoter.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.info.mc.McMap;
import team.zxorg.zxnoter.info.mc.McNote;
import team.zxorg.zxnoter.info.mc.McTime;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class McReader implements MapReader {
    private UnLocalizedMapInfo unLocalizedMapInfo;
    private ArrayList<Timing> timingPoints;

    @Override
    public String getSupportFileExtension() {
        return "mc";
    }

    @Override
    public Path getReadPath() {
        return null;
    }

    @Override
    public ZXMap read(Path path) throws IOException {
        McMap mcMap = new Gson().fromJson(Files.newBufferedReader(path), McMap.class);

        ZXMap map = new ZXMap();
        unLocalizedMapInfo = new UnLocalizedMapInfo();
        ArrayList<BaseNote> allNotes = new ArrayList<>();
        timingPoints = new ArrayList<>();

        // 1. 解析 Metadata
        if (mcMap.meta != null) {
            if (mcMap.meta.song != null) {
                unLocalizedMapInfo.setInfo(ZXMInfo.Title, mcMap.meta.song.title);
                unLocalizedMapInfo.setInfo(ZXMInfo.Artist, mcMap.meta.song.artist);
                unLocalizedMapInfo.setInfo(ZXMInfo.AudioPath, mcMap.meta.song.file);
                unLocalizedMapInfo.setInfo(ZXMInfo.Bpm, String.valueOf(mcMap.meta.song.bpm));
                // 可以在这里将 titleorg, artistorg 存入 UnLocalizedMapInfo 的保留字段，如果需要的话
            }
            unLocalizedMapInfo.setInfo(ZXMInfo.BgPath, mcMap.meta.background);
            unLocalizedMapInfo.setInfo(ZXMInfo.MapCreator, mcMap.meta.creator);
            unLocalizedMapInfo.setInfo(ZXMInfo.Version, mcMap.meta.version);

            // 读取键数，这是支持 4k/5k/6k 的关键
            if (mcMap.meta.mode_ext != null) {
                unLocalizedMapInfo.setInfo(ZXMInfo.KeyCount, String.valueOf(mcMap.meta.mode_ext.column));
            } else {
                unLocalizedMapInfo.setInfo(ZXMInfo.KeyCount, "4"); // 默认回退
            }
        }

        // 2. 解析 TimingPoints (时间点)
        // Malody 的 time 数组是基于 Beat 的 BPM 变化点
        // 需要先按 Beat 排序
        mcMap.time.sort(Comparator.comparingDouble(t -> beatToValue(t.beat)));

        // 计算绝对时间 (ms)
        calculateTimings(mcMap.time);

        // 3. 解析 Notes
        for (McNote note : mcMap.note) {
            // 过滤掉背景音效 (type=1 且没有 column 的通常是背景音效)
            if (note.column == null) {
                // 如果需要支持 KeySound (按键音)，可以在这里处理
                continue;
            }

            // 计算开始时间 ms
            long startTime = getMsFromBeat(note.beat, mcMap.time);
            int column = note.column; // 直接读取轨道索引，自动适配 4k/5k...

            if (note.endbeat != null) {
                // 长条
                long endTime = getMsFromBeat(note.endbeat, mcMap.time);
                LongNote ln = new LongNote(startTime, column, endTime - startTime);
                if (note.sound != null) ln.setSound(note.sound);
                allNotes.add(ln);
            } else {
                // 单键
                FixedOrbitNote fn = new FixedOrbitNote(startTime, column);
                if (note.sound != null) fn.setSound(note.sound);
                allNotes.add(fn);
            }
        }

        allNotes.sort(BaseNote::compareTo);
        map.notes = allNotes;
        map.timingPoints = timingPoints;
        map.unLocalizedMapInfo = unLocalizedMapInfo;
        completeInfo();

        return map;
    }

    /**
     * 将 Malody 的 [bar, num, den] 转换为 double 类型的总拍数
     */
    private double beatToValue(int[] beat) {
        if (beat == null || beat.length < 3) return 0;
        return beat[0] + (double) beat[1] / beat[2];
    }

    /**
     * 构建 Timing 列表并计算 ms 时间戳
     */
    private void calculateTimings(List<McTime> mcTimes) {
        if (mcTimes.isEmpty()) return;

        double currentBpm = mcTimes.get(0).bpm;
        double prevBeatVal = 0;
        long prevMs = 0;

        // 初始 BPM
        timingPoints.add(new Timing(0, currentBpm, true, currentBpm));

        for (McTime mt : mcTimes) {
            double currBeatVal = beatToValue(mt.beat);

            if (currBeatVal <= 0) {
                // 覆盖初始 BPM
                timingPoints.get(0).absBpm = mt.bpm;
                timingPoints.get(0).bpmSpeed = mt.bpm;
                currentBpm = mt.bpm;
            } else {
                // 计算从上一个时间点到现在的毫秒差
                double beatDelta = currBeatVal - prevBeatVal;
                long msDelta = (long) (beatDelta * (60000.0 / currentBpm));
                long currMs = prevMs + msDelta;

                timingPoints.add(new Timing(currMs, mt.bpm, true, mt.bpm));

                prevMs = currMs;
                prevBeatVal = currBeatVal;
                currentBpm = mt.bpm;
            }
        }
    }

    /**
     * 根据当前所有 BPM 点，将任意 beat 转换为 ms
     */
    private long getMsFromBeat(int[] beat, List<McTime> mcTimes) {
        double targetBeat = beatToValue(beat);

        // 找到 beat 所在区间的起始 BPM 点
        double currentBpm = mcTimes.get(0).bpm;
        double accumulatedMs = 0;
        double prevBeat = 0;

        for (McTime tp : mcTimes) {
            double tpBeat = beatToValue(tp.beat);
            if (tpBeat > targetBeat) break; // 超过了目标位置

            // 累加这段 BPM 持续的时间
            double beatDelta = tpBeat - prevBeat;
            if (beatDelta > 0) {
                accumulatedMs += beatDelta * (60000.0 / currentBpm);
            }

            currentBpm = tp.bpm;
            prevBeat = tpBeat;
        }

        // 加上最后一段剩余的时间
        double remainingBeats = targetBeat - prevBeat;
        accumulatedMs += remainingBeats * (60000.0 / currentBpm);

        return (long) accumulatedMs;
    }

    @Override
    public void completeInfo() {
        for (ZXMInfo info : ZXMInfo.values())
            if (!unLocalizedMapInfo.allInfo.containsKey(info)) {
                unLocalizedMapInfo.allInfo.put(info, info.getDefaultValue());
            }
        unLocalizedMapInfo.allInfo.put(ZXMInfo.TimingCount, String.valueOf(timingPoints.size()));
    }
}