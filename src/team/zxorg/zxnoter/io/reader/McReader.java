package team.zxorg.zxnoter.io.reader;

import com.google.gson.Gson;
import team.zxorg.zxnoter.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.info.mc.McMap;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * mc读取器(malody)
 */
public class McReader implements MapReader {
    private ArrayList<Timing> timingPoints;
    private UnLocalizedMapInfo unLocalizedMapInfo;
    private int key;

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
        String fileName = path.getFileName().toString();
        //检查合法性
        boolean illegalFile = !fileName.endsWith(".mc");
        if (illegalFile) {
            throw new RuntimeException("mc文件名非法");
        }

        //初始化
        ZXMap map = new ZXMap();
        unLocalizedMapInfo = new UnLocalizedMapInfo();
        ArrayList<BaseNote> allNotes = new ArrayList<>();


        McMap mcMap = new Gson().fromJson(Files.newBufferedReader(path), McMap.class);
        //谱面标题
        unLocalizedMapInfo.setInfo(ZXMInfo.Title, mcMap.meta.song.title);
        //图片路径
        unLocalizedMapInfo.setInfo(ZXMInfo.BgPath, mcMap.meta.background);
        //音频路径
        unLocalizedMapInfo.setInfo(ZXMInfo.AudioPath, mcMap.meta.song.file);
        //键数
        unLocalizedMapInfo.setInfo(ZXMInfo.KeyCount, mcMap.meta.version.substring(mcMap.meta.version.indexOf("_") + 1, mcMap.meta.version.lastIndexOf("_")).replaceAll("k", ""));
        key = Integer.parseInt(unLocalizedMapInfo.getInfo(ZXMInfo.KeyCount));
        //版本
        unLocalizedMapInfo.setInfo(ZXMInfo.Version, mcMap.meta.version.substring(mcMap.meta.version.lastIndexOf("_") + 1));
        //图长度
        //unLocalizedMapInfo.setInfo(ZXMInfo.MapLength, String.valueOf(mcMap.note[mcMap.note.length - 1].beat[0] * mcMap.meta.song.bpm));
        unLocalizedMapInfo.setInfo(ZXMInfo.Bpm, String.valueOf(mcMap.meta.song.bpm));
        unLocalizedMapInfo.setInfo(ZXMInfo.Artist, String.valueOf(mcMap.meta.song.artist));
        unLocalizedMapInfo.setInfo(ZXMInfo.MapCreator, mcMap.meta.creator);

        float bpm = mcMap.meta.song.bpm;
        timingPoints = new ArrayList<>();

        for (var t : mcMap.time) {
            timingPoints.add(new Timing((long) (t.beat[0] * bpm), t.bpm, true, t.bpm));
            bpm = t.bpm;
        }

        for (var note : mcMap.note) {
            long t = getTime(note.beat);

            if (note.x == null)
                continue;

            int o = getOrbit(note.x);
            if (note.seg != null) {
                if (note.seg.length > 1) {
                    //组合键
                    ComplexNote complexNote = new ComplexNote(t, o);
                    long ct = t;
                    int co = o;
                    for (var sub : note.seg) {

                        {
                            long nt = getTime(sub.beat);
                            if (nt != 0) {
                                complexNote.addNote(new LongNote(ct, co, nt));
                                ct = t + nt;
                            }
                        }

                        if (sub.x != null) {
                            int no = getOrbit(sub.x);
                            if (no != 0) {
                                complexNote.addNote(new SlideNote(ct, co, no));
                                co = o + no;
                            }
                        }

                    }
                    allNotes.add(complexNote);
                } else {
                    if (note.seg[0].x == null) {
                        allNotes.add(new LongNote(t, o, getTime(note.seg[0].beat)));
                    } else {
                        allNotes.add(new SlideNote(t, o, getOrbit(note.seg[0].x)));
                    }
                }
            } else {
                allNotes.add(new FixedOrbitNote(t, o));
            }

        }


        map.notes = allNotes;
        allNotes.sort(BaseNote::compareTo);
        completeInfo();
        map.unLocalizedMapInfo = unLocalizedMapInfo;
        map.timingPoints = timingPoints;

        return map;
    }

    private long getTime(int[] beat) {
        float currentBpm = getBpmAtBeat(beat);


        // 计算总的拍数 (measures * beatsPerMeasure + (ticks / ticksPerMeasure))
        float totalBeats = beat[0] + ((float) beat[1] / beat[2]);

        // 计算时间戳 (ms)
        return (long) ((60_000 / currentBpm) * totalBeats);
    }

    // 根据轨道位置x值计算轨道编号
    private int getOrbit(int x) {
        int xMax = 240;  // 可以替换为动态获取的最大x值
        return Math.min(x / (xMax / key), key - 1);
    }

    // 获取当前节拍位置的BPM值
    private float getBpmAtBeat(int[] beat) {
        float currentBpm = (float) timingPoints.get(0).absBpm;

        for (Timing timing : timingPoints) {
            if (timing.timestamp > beat[0]) {
                break;
            }
            currentBpm = (float) timing.absBpm;
        }

        return currentBpm;
    }

    @Override
    public void completeInfo() {
        for (ZXMInfo info : ZXMInfo.values())
            if (!unLocalizedMapInfo.allInfo.containsKey(info)) {
                unLocalizedMapInfo.allInfo.put(info, info.getDefaultValue());
            }
    }
}
