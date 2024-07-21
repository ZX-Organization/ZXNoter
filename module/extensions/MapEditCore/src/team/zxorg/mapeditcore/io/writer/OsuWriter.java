package team.zxorg.mapeditcore.io.writer;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.ZXMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.ImdMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.OsuMapData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.*;
import team.zxorg.mapeditcore.mapElement.timing.OsuTiming;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class OsuWriter extends MapWriter {

    @Override
    public void writeFile() throws IOException {
        OsuMapData osuMapData = null;
        ZXMapData zxMapData = null;
        if (map.metaData instanceof ImdMapData data) {
            zxMapData = data.toZxMeta();
            //将imd折线map转化为纯定轨map
            map = map.convert();
        } else if (map.metaData instanceof ZXMapData data)
            if (data instanceof OsuMapData osuData)
                osuMapData = osuData;
            else zxMapData = data;
        else osuMapData = new OsuMapData();

        if (zxMapData != null) {
            osuMapData = new OsuMapData(zxMapData,map);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()));

        writer.write("osu file format v14");
        writer.newLine();

        writer.newLine();
        writer.write("[General]");
        writer.newLine();
        Set<String> geKeys = osuMapData.getGeneralInfo().keySet();
        for (String key : geKeys) {
            writer.write(key + ":" + osuMapData.getGeneralInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Editor]");
        writer.newLine();
        Set<String> EdKeys = osuMapData.getEditorInfo().keySet();
        for (String key : EdKeys) {
            writer.write(key + ":" + osuMapData.getEditorInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Metadata]");
        writer.newLine();
        writer.write("Title:" + osuMapData.getTitle());
        writer.newLine();
        writer.write("TitleUnicode:" + osuMapData.getTitleUnicode());
        writer.newLine();
        writer.write("Artist:" + osuMapData.getArtist());
        writer.newLine();
        writer.write("ArtistUnicode:" + osuMapData.getArtistUnicode());
        writer.newLine();
        writer.write("Creator:" + osuMapData.getCreator());
        writer.newLine();
        writer.write("Version:" + osuMapData.getMapVersion());
        writer.newLine();
        writer.write("Source:" + osuMapData.getSource());
        writer.newLine();
        writer.write("Tags:" + osuMapData.getTags());
        writer.newLine();
        writer.write("BeatmapId:" + osuMapData.getBeatMapId());
        writer.newLine();
        writer.write("BeatmapSetId:" + osuMapData.getBeatMapSetId());
        writer.newLine();

        writer.newLine();
        writer.write("[Difficulty]");
        writer.newLine();
        Set<String> DiKeys = osuMapData.getDifficultInfo().keySet();
        for (String key : DiKeys) {
            writer.write(key + ":" + osuMapData.getDifficultInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Event]");
        writer.newLine();
        Set<String> EvKeys = osuMapData.getEventInfo().keySet();
        int index = 0;
        for (String key : EvKeys) {
            writer.write("//" + OsuMapData.eventDefaultInfo[index++]);
            writer.newLine();
            String value = osuMapData.getEventInfo().get(key);
            if (value == null || "".equals(value)) continue;
            else writer.write(value);
            writer.newLine();
        }

        writer.newLine();
        writer.write("[TimingPoints]");
        writer.newLine();

        for (IMapElement timing : map.timings) {

            OsuTiming osuTiming = null;
            if (timing instanceof OsuTiming t) {
                osuTiming = t;
            } else if (timing instanceof Timing t)
                osuTiming = new OsuTiming(t);
            if (osuTiming == null) {
                osuTiming = new OsuTiming(new Timing(0, map.preferenceBpm, 1));
            }
            writer.write(generateTimingPars(osuTiming));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[HitObjects]");
        writer.newLine();
        for (IMapElement note : map.notes) {
            if (note instanceof OsuNote osuNote) {
                //osu键
                writer.write(generateNotePars(osuNote));
                writer.newLine();
            } else if (note instanceof OsuHold osuHold) {
                //osu条
                writer.write(generateNotePars(osuHold));
                writer.newLine();
            } else if (note instanceof Note baseNote) {
                if (baseNote instanceof Hold hold) {
                    //长条
                    writer.write(generateNotePars(new OsuHold(hold)));
                    writer.newLine();
                } else if (baseNote instanceof Flick flick) {
                    //滑键
                    int direction = (flick.getDirection() == 0 ? -1 : 1);
                    OsuNote here = new OsuNote(new Note(flick.getTime(), flick.getPosition()));
                    OsuNote next = new OsuNote(new Note(flick.getTime(), flick.getPosition() + direction* flick.getSlideLength()));

                    //获得此滑键在map中的下标
                    int flickIndex = map.notes.indexOf(flick);

                    boolean writeHere = true;
                    boolean writeNext = true;
                    Note spanNote = flick;
                    //查重(查询前轨道数个和后轨道数个物件)
                    for (int i = flickIndex - map.orbitCount; i < flickIndex + map.orbitCount + 1; i++) {

                        if (i < map.notes.size()) {
                            spanNote = (Note) map.notes.get(i);
                        }

                        if (spanNote.getTime() == here.getTime() && Math.abs(spanNote.getPosition() - here.getPosition()) <= 0.01){
                            writeHere = false;
                        }
                        if (spanNote.getTime() == next.getTime() || Math.abs(spanNote.getPosition() - next.getPosition()) <= 0.01){
                            writeNext = false;
                        }
                    }
                    if (writeHere){
                        writer.write(generateNotePars(here));
                        writer.newLine();
                    }
                    if(writeNext){
                        writer.write(generateNotePars(next));
                        writer.newLine();
                    }
                } else {
                    //普通note
                    writer.write(generateNotePars(new OsuNote(baseNote)));
                    writer.newLine();
                }
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * 一个OsuTiming生成一行参数
     */
    private String generateTimingPars(OsuTiming timing) {
        ArrayList<String> tPars = new ArrayList<>();
        //时间参数
        tPars.add(String.valueOf(timing.getTime()));
        //拍参数
        double beatPar;
        double baseBpm = timing.getBpm();
        if (timing.isExtendTiming()) {
            beatPar = 1 / (baseBpm / 60000);
        } else {
            double speed = timing.getSpeed();
            beatPar = 1 / speed * baseBpm * 100 * -1;
        }
        tPars.add(String.valueOf(beatPar));
        tPars.add(String.valueOf(timing.getBeats()));
        tPars.add(String.valueOf(timing.getHitSound()));
        tPars.add(String.valueOf(timing.getHitSoundPar()));
        tPars.add(String.valueOf(timing.getVolume()));
        tPars.add(String.valueOf(timing.isExtendTiming() ? 1 : 0));
        tPars.add(String.valueOf(timing.getEffect()));
        return tPars.toString().replace("[", "").replace("]", "").replaceAll(" ", "");
    }

    /**
     * 一个Note生成一行参数
     */
    private String generateNotePars(Note note) throws IOException {
        ArrayList<String> bPars = new ArrayList<>();
        ArrayList<String> soundPars = new ArrayList<>();
        if (note instanceof OsuNote osuNote) {
            //位置（轨道）
            bPars.add(String.valueOf((int) (Math.rint(osuNote.getPosition() * 512))));
            //key模式固定参数
            bPars.add(String.valueOf(192));
            //时间戳
            bPars.add(String.valueOf(osuNote.getTime()));
            //类型1单键128长键
            bPars.add(String.valueOf(osuNote.getKeyType()));
            //打击音效
            bPars.add(String.valueOf(osuNote.getHitSound()));
            //参数（单键0，长键持续时间）
            bPars.add(String.valueOf(0));

            //音效组参数
            soundPars.add(String.valueOf(osuNote.getNormalSampleSet()));
            soundPars.add(String.valueOf(osuNote.getExtendSampleSet()));
            soundPars.add(String.valueOf(osuNote.getSampleIndex()));
            soundPars.add(String.valueOf(osuNote.getSampleVolume()));
            String soundParsStr = formatList(soundPars, false).replaceFirst(":", "");
            soundParsStr += ":";
            String soundFile = osuNote.getSoundFile();
            if (!(soundFile == null)) {
                soundParsStr += new File(osuNote.getKeyAudioPath()).getName();
            }
            return formatList(bPars, true) + "," + soundParsStr;
        }
        if (note instanceof OsuHold osuHold) {
            //位置（轨道）
            bPars.add(String.valueOf((int) (Math.rint(osuHold.getPosition() * 512))));
            //key模式固定参数
            bPars.add(String.valueOf(192));
            //时间戳
            bPars.add(String.valueOf(osuHold.getTime()));
            //类型1单键128长键
            bPars.add(String.valueOf(osuHold.getKeyType()));
            //打击音效
            bPars.add(String.valueOf(osuHold.getHitSound()));
            //参数（单键0，长键持续时间）
            bPars.add(String.valueOf(osuHold.getTime() + osuHold.getDuration()));
            //音效组参数
            soundPars.add(String.valueOf(osuHold.getNormalSampleSet()));
            soundPars.add(String.valueOf(osuHold.getExtendSampleSet()));
            soundPars.add(String.valueOf(osuHold.getSampleIndex()));
            soundPars.add(String.valueOf(osuHold.getSampleVolume()));
            String soundParsStr = formatList(soundPars, false);
            soundParsStr += ":";
            String soundFile = osuHold.getSoundFile();
            if (!(soundFile == null)) {
                soundParsStr += new File(osuHold.getKeyAudioPath()).getName();
            }
            return formatList(bPars, true) + soundParsStr;
        }
        throw new IOException("写出物件类型错误");
    }

    private String formatList(ArrayList<String> list, boolean isComma) {
        if (isComma)
            return list.toString().replace("[", "").replace("]", "").replaceAll(" ", "");
        else {
            StringBuilder str = new StringBuilder();
            for (String s : list) {
                str.append(":");
                str.append(s);
            }
            return str.toString();
        }
    }

    public OsuWriter(ZXMap map) {
        super(map);
    }

    @Override
    public String getSuffix() {
        return ".osu";
    }

    @Override
    public File getPath() {
        File file = null;
        boolean legal = true;
        if (directory.isDirectory()) legal = false;
        else if (directory.isFile()) file = directory;
        if (!legal) file = new File(directory.getAbsolutePath()+map.metaData.getTitleUnicode()+getSuffix());
        return file;
    }

    @Override
    public OsuWriter setDirectory(File directory) {
        this.directory = directory;
        return this;
    }
}
