package team.zxorg.zxnoter.io.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.zxnoter.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.info.mc.*;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * mc写出器(malody) - 修复漂移版
 */
public class McWriter implements MapWriter {

    @Override
    public String getDefaultName() {
        return "chart.mc";
    }

    @Override
    public void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException {
        McMap mcMap = new McMap();
        UnLocalizedMapInfo info = zxMap.unLocalizedMapInfo;

        // -------------------------
        // 步骤 0: 预处理 BPM (修复漂移的关键)
        // -------------------------
        // 复制一份 timingPoints 以免修改原对象
        List<Timing> sortedTimings = new ArrayList<>(zxMap.timingPoints);
        sortedTimings.sort((a, b) -> Long.compare(a.timestamp, b.timestamp));

        // 修正浮点误差 (Snap BPM)
        for (Timing t : sortedTimings) {
            t.absBpm = snapBpm(t.absBpm);
        }

        // -------------------------
        // 1. 构建 Meta
        // -------------------------
        mcMap.meta = new McMeta();
        mcMap.meta.version = info.getInfo(ZXMInfo.Version);
        mcMap.meta.creator = info.getInfo(ZXMInfo.MapCreator);
        mcMap.meta.background = info.getInfo(ZXMInfo.BgPath);
        mcMap.meta.cover = "";
        mcMap.meta.mode = 0;
        mcMap.meta.id = 0;

        mcMap.meta.song = new McSong();
        mcMap.meta.song.title = info.getInfo(ZXMInfo.Title);
        mcMap.meta.song.artist = info.getInfo(ZXMInfo.Artist);
        mcMap.meta.song.file = info.getInfo(ZXMInfo.AudioPath);
        mcMap.meta.song.titleorg = mcMap.meta.song.title;
        mcMap.meta.song.artistorg = mcMap.meta.song.artist;

        try {
            double rawBpm = Double.parseDouble(info.getInfo(ZXMInfo.Bpm));
            mcMap.meta.song.bpm = snapBpm(rawBpm); // 元数据里的 BPM 也要修正
        } catch (Exception e) {
            mcMap.meta.song.bpm = 120.0;
        }

        mcMap.meta.mode_ext = new McModeExt();
        try {
            mcMap.meta.mode_ext.column = Integer.parseInt(info.getInfo(ZXMInfo.KeyCount));
        } catch (Exception e) {
            mcMap.meta.mode_ext.column = 4;
        }
        mcMap.meta.mode_ext.bar_begin = 0;

        // -------------------------
        // 2. 构建 Time (Timing Points)
        // -------------------------
        mcMap.time = new ArrayList<>();

        // 使用修正后的 sortedTimings 计算
        double currentTotalBeats = 0;
        long prevTime = 0;
        double currentBpm = mcMap.meta.song.bpm;

        // 处理初始 BPM 可能不在 0ms 的情况
        if (!sortedTimings.isEmpty() && sortedTimings.get(0).timestamp == 0) {
            currentBpm = sortedTimings.get(0).absBpm;
        }

        for (Timing t : sortedTimings) {
            long deltaTime = t.timestamp - prevTime;
            // 使用修正后的 BPM 计算经过的拍数，能极大减少误差
            double deltaBeats = (deltaTime * currentBpm) / 60000.0;
            currentTotalBeats += deltaBeats;

            McTime mcTime = new McTime();
            mcTime.beat = convertBeatsToArray(currentTotalBeats, currentBpm);
            mcTime.bpm = t.absBpm; // 写入修正后的 BPM
            mcTime.delay = 0.0;
            mcMap.time.add(mcTime);

            prevTime = t.timestamp;
            currentBpm = t.absBpm;
        }

        // -------------------------
        // 3. 构建 Notes
        // -------------------------
        mcMap.note = new ArrayList<>();
        mcMap.effect = new ArrayList<>();
        McEffect signEffect = new McEffect();
        signEffect.beat = new int[]{0,0,1};
        signEffect.sign = 4;
        mcMap.effect.add(signEffect);

        for (BaseNote n : zxMap.notes) {
            if (n instanceof FixedOrbitNote) {
                FixedOrbitNote fn = (FixedOrbitNote) n;
                McNote mcNote = new McNote();

                mcNote.column = fn.getOrbit();

                // 使用修正后的 sortedTimings 进行计算
                mcNote.beat = calculateBeat(fn.timeStamp, sortedTimings, mcMap.meta.song.bpm);

                if (fn instanceof LongNote) {
                    LongNote ln = (LongNote) fn;
                    mcNote.endbeat = calculateBeat(ln.timeStamp + ln.sustainedTime, sortedTimings, mcMap.meta.song.bpm);
                }

                if (fn.getSoundPath() != null && !fn.getSoundPath().isEmpty()) {
                    mcNote.sound = fn.getSoundPath();
                }

                mcMap.note.add(mcNote);
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = Files.newBufferedWriter(path)) {
            gson.toJson(mcMap, writer);
        }
    }

    /**
     * BPM 吸附修正：阈值调小，防止误判
     */
    private double snapBpm(double bpm) {
        if (Math.abs(bpm - Math.round(bpm)) < 0.001) {
            return (double) Math.round(bpm);
        }
        if (Math.abs(bpm - (Math.round(bpm * 2) / 2.0)) < 0.001) {
            return Math.round(bpm * 2) / 2.0;
        }
        return bpm;
    }

    private int[] calculateBeat(long timestamp, List<Timing> timings, double defaultBpm) {
        double totalBeats = 0;
        double curBpm = defaultBpm;
        long prevTime = 0;

        if (!timings.isEmpty()) {
            curBpm = timings.get(0).absBpm;
        }

        for (Timing t : timings) {
            if (t.timestamp > timestamp) {
                break;
            }
            long delta = t.timestamp - prevTime;
            totalBeats += (delta * curBpm) / 60000.0;

            prevTime = t.timestamp;
            curBpm = t.absBpm;
        }

        long offset = timestamp - prevTime;
        totalBeats += (offset * curBpm) / 60000.0;

        // 重点：传入当前的 BPM (curBpm)
        return convertBeatsToArray(totalBeats, curBpm);
    }

    /**
     * 终极算法：基于常用音符分数的“磁吸”吸附
     * 不再依赖固定的 beat 误差，而是依赖固定的 ms 误差 (3ms)
     */
    private int[] convertBeatsToArray(double totalBeats, double currentBpm) {
        // 1. 先把 totalBeats 拆分为 整数部分 + 小数部分
        // 为了防止 3.999999 这种浮点误差，先加一个小 epsilon
        int bar = (int) Math.floor(totalBeats + 0.00001);
        double fraction = totalBeats - bar;

        // 2. 如果非常接近整数 (0 或 1)，直接返回
        // 允许误差：3ms (osu! 的整数存储误差最大 0.5ms，考虑到变速和对齐误差，3ms 是安全范围)
        double msPerBeat = 60000.0 / currentBpm;
        double allowedErrorInBeats = 3.0 / msPerBeat; // 3ms 对应的节拍数

        if (fraction < allowedErrorInBeats) {
            return new int[]{bar, 0, 1};
        }
        if (fraction > 1.0 - allowedErrorInBeats) {
            return new int[]{bar + 1, 0, 1};
        }

        // 3. 磁吸算法：尝试匹配常用分母
        // 优先级：分母越小越优先 (1/4 > 1/16 > 1/192)
        int[] commonDenominators = {
                2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64,
                96, 128, 192
        };

        for (int den : commonDenominators) {
            // 计算在这个分母下，最接近的分子是多少
            // 例如 fraction = 0.33331, den = 3 -> round(0.99993) -> 1
            long num = Math.round(fraction * den);

            // 算出吸附后的值
            double snappedFraction = (double) num / den;

            // 计算误差
            double errorInBeats = Math.abs(fraction - snappedFraction);

            // 核心判断：如果不匹配的误差转换成毫秒，小于 3ms，那就吸附！
            if (errorInBeats < allowedErrorInBeats) {
                // 约分 (例如 2/4 -> 1/2)
                int gcdVal = gcd((int)num, den);
                return new int[]{bar, (int)num / gcdVal, den / gcdVal};
            }
        }

        // 4. 如果所有常用分母都吸附失败（极少见的波浪线或变速Note）
        // 使用高精度保留（保留最大公约数约分）
        // 这种情况下通常是转换出现极端异常，或者原谱面就是乱写的
        // 回退到 192 分音的近似
        int fallbackDen = 192;
        int fallbackNum = (int) Math.round(fraction * fallbackDen);
        int gcdVal = gcd(fallbackNum, fallbackDen);

        return new int[]{bar, fallbackNum / gcdVal, fallbackDen / gcdVal};
    }

    // 辅助方法：GCD
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}