package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.info.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.info.map.OsuInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.note.timing.ZXTiming;

/**
 * @author xiang2333
 */
public class OsuWriter implements MapWriter {
    private HashMap<OsuInfo, String> allInfos;
    private BufferedWriter bW;
    @Override
    public void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException {
        allInfos = checkLocalizedInfos(zxMap);
        //检查本地化信息
        if (allInfos.size() == OsuInfo.values().length) {
            Collection<String> values = allInfos.values();
            for (String value : values) {
                if (value == null) {
                    throw new NoSuchFieldException("丢失本地化信息!");
                }
            }
        } else {
            throw new NoSuchFieldException("丢失本地化字段!");
        }
        //本地化信息检查通过
        bW = new BufferedWriter(new FileWriter(path.toAbsolutePath().toFile()));
        //切分属性数组
        OsuInfo[] allInfo = OsuInfo.values();
        //General
        OsuInfo[] generalInfos = Arrays.copyOfRange(allInfo,1,17);//不含尾
        //Editor
        OsuInfo[] editorInfos = Arrays.copyOfRange(allInfo,18,22);
        //Metadata
        OsuInfo[] metadataInfos = Arrays.copyOfRange(allInfo,22,32);
        //Difficulty
        OsuInfo[] difficultyInfos = Arrays.copyOfRange(allInfo,32,38);
        //Events
        OsuInfo[] eventInfos = Arrays.copyOfRange(allInfo,38,46);
        //osu文件版本
        bW.write(OsuInfo.osufileformat.unLocalize() + " ");
        bW.write(allInfos.get(OsuInfo.osufileformat));
        bW.newLine();
        //[General]
        bW.newLine();
        bW.write("[General]");
        bW.newLine();
        for (OsuInfo info:generalInfos){
            writeKeyValueInfo(info);
        }
        //[Editor]
        bW.newLine();
        bW.write("[Editor]");
        bW.newLine();
        for (OsuInfo info:editorInfos){
            writeKeyValueInfo(info);
        }
        //[Metadata]
        bW.newLine();
        bW.write("[Metadata]");
        bW.newLine();
        for (OsuInfo info:metadataInfos){
            writeKeyValueInfo(info);
        }
        //[Difficulty]
        bW.newLine();
        bW.write("[Difficulty]");
        bW.newLine();
        for (OsuInfo info:difficultyInfos){
            writeKeyValueInfo(info);
        }
        //[Events]
        bW.newLine();
        bW.write("[Events]");
        bW.newLine();
        for (OsuInfo info:eventInfos){
            bW.write("//"+info.getOriginName());
            bW.newLine();
            if (!"".equals(allInfos.get(info))){
                bW.write(allInfos.get(info));
                bW.newLine();
            }
        }
        //[TimingPoints]
        bW.newLine();
        bW.write("[TimingPoints]");
        bW.newLine();

        int index = 1;
        for (Timing timing: zxMap.timingPoints){
            StringBuilder strB = new StringBuilder();
            strB.append(timing.timestamp).append(",");

            System.out.println(timing);
            //判断是否属于ZxTiming,是则细分
            if (timing instanceof ZXTiming zxTiming){
                if (zxTiming.isExtendTiming) {
                    strB.append(1/(zxTiming.absBpm/60000)).append(",");
                } else {
                    //变速
                    strB.append("-");
                    double tempBeatPar = 100/(zxTiming.bpmSpeed / zxTiming.absBpm);
                    if (((tempBeatPar % 1) == 0)) {
                        strB.append((int)tempBeatPar).append(",");
                    } else {
                        strB.append(tempBeatPar).append(",");
                    }
                }
            }else {

                if (Math.abs(timing.bpmSpeed - timing.absBpm) <= 0.0002) {
                    strB.append(1 / (timing.absBpm / 60000)).append(",");
                } else {
                    strB.append("-").append(100/(timing.bpmSpeed / timing.absBpm)).append(",");
                }

            }

            if (timing instanceof ZXTiming zxTiming){

                //属于ZxTiming,直接使用其已有值
                strB.append(zxTiming.beats).append(",").
                        append(zxTiming.sampleSet).append(",").
                        append(zxTiming.soundPar).append(",").
                        append(zxTiming.volume).append(",").
                        append(zxTiming.isExtendTiming?1:0).append(",").
                        append(zxTiming.effect);
                bW.write(strB.toString());
                bW.newLine();
                index++;
                continue;
            }
            //不属于ZxTiming,自动适配默认值
            strB.append(4).append(",").append(0).append(",").
                    append(0).append(",").append(75).append(",");
            if (index==1){
                strB.append(1).append(",").append(0);
            }else {
                if (Math.abs(timing.bpmSpeed - timing.absBpm) <= 0.0002) {
                    strB.append(1).append(",").append(0);
                } else {
                    strB.append(0).append(",").append(0);
                }
            }
            bW.write(strB.toString());
            bW.newLine();
            index++;
        }
        //所有物件
        bW.newLine();
        bW.write("[HitObjects]");
        bW.newLine();
        for (BaseNote note: zxMap.notes){
            if (note instanceof FixedOrbitNote fixedOrbitNote){
                if (fixedOrbitNote instanceof ComplexNote complexNote){
                    //检查是组合键拆分写出

                }

                StringBuilder noteStrB = new StringBuilder();
                StringBuilder soundStrB = new StringBuilder();
                int keyCount = Integer.parseInt(allInfos.get(OsuInfo.CircleSize));
                noteStrB.append(512*fixedOrbitNote.orbit/keyCount + (int)(256/Double.parseDouble(allInfos.get(OsuInfo.CircleSize)))).append(",").
                                  append(192).append(",").
                                  append(fixedOrbitNote.timeStamp).append(",");
                if (fixedOrbitNote instanceof LongNote longNote){
                    //长键(类型128)
                    noteStrB.append(128).append(",");
                    if (longNote instanceof CustomLongNote customLongNote){
                        //带自定义音效
                        noteStrB.append(customLongNote.hitSound).append(",");
                        soundStrB.append(customLongNote.normalSampleSet).append(":").
                                            append(customLongNote.extendSampleSet).append(":").
                                            append(customLongNote.sampleIndex).append(":").
                                            append(customLongNote.sampleVolume).append(":");
                    }else {
                        //不带,默认0
                        noteStrB.append(0).append(",");
                        soundStrB.append(0).append(":").append(0).append(":").
                                append(0).append(":").append(0).append(":");
                    }
                    //结束时间后跟:
                    noteStrB.append(longNote.timeStamp + longNote.sustainedTime).append(":");
                    //无需其他操作(此时:结尾,参数长度6)
                }else{
                    //单键
                    noteStrB.append(1).append(",");
                    if (fixedOrbitNote instanceof CustomNote customNote){
                        //带音效
                        noteStrB.append(customNote.hitSound).append(",");
                        soundStrB.append(customNote.normalSampleSet).append(":").
                                append(customNote.extendSampleSet).append(":").
                                append(customNote.sampleIndex).append(":").
                                append(customNote.sampleVolume).append(":");
                    }else {
                        //不带,默认0
                        noteStrB.append(0).append(",");
                        soundStrB.append(0).append(":").append(0).append(":").
                                append(0).append(":").append(0).append(":");
                    }
                    //无需其他操作(此时,结尾,参数长度5)
                }
                //按键音效文件(默认已经是空字符串)
                soundStrB.append(fixedOrbitNote.getSoundPath());
                //拼接两个字符串
                noteStrB.append(soundStrB);
                //写出
                bW.write(noteStrB.toString());
                bW.newLine();
            }
        }

        bW.flush();
        bW.close();
    }
    @Override
    public String getDefaultName(){
        return allInfos.get(OsuInfo.Title) + " [" + allInfos.get(OsuInfo.Version) + "]" + ".osu";
    }
    private void writeKeyValueInfo(OsuInfo info) throws IOException {
        bW.write(info.getOriginName());
        bW.write(":");
        bW.write(allInfos.get(info));
        bW.newLine();
    }

    public HashMap<OsuInfo, String> checkLocalizedInfos(ZXMap zxMap) {
        //处理本地化信息
        UnLocalizedMapInfo unLocalizedMapInfo = zxMap.unLocalizedMapInfo;
        //列举写出需要的信息
        OsuInfo[] infos = OsuInfo.values();
        HashMap<OsuInfo, String> localizeMap = new HashMap<>();
        //初始化带key无value的信息表
        for (OsuInfo osuInfo : infos) {
            localizeMap.put(osuInfo, null);
        }
        //防止null,先初始化BgPath
        localizeMap.put(
                OsuInfo.valueOf("BgPath"),
                unLocalizedMapInfo.getInfo(OsuInfo.valueOf("BgPath").unLocalize())
        );
        //列举目前有的反本地化信息,判断有无和上面所需信息表反本地化信息相同的信息,直接使用
        for (OsuInfo localizedInfo : infos) {
            //通过本地化信息表查询所有反本地化信息
            String tempValue = unLocalizedMapInfo.getInfo(localizedInfo.unLocalize());
            if (tempValue == null) {
                //反本地化信息表中没有则代表null,使用默认值或计算
                switch (localizedInfo) {
                    case TitleUnicode -> {
                        tempValue = localizeMap.get(OsuInfo.valueOf("Title"));
                        localizeMap.put(localizedInfo, tempValue);
                        continue;
                    }
                    case ArtistUnicode -> {
                        tempValue = localizeMap.get(OsuInfo.valueOf("Artist"));
                        localizeMap.put(localizedInfo, tempValue);
                        continue;
                    }
                    case BackgroundandVideoevents -> {
                        tempValue = "0,0,\"" + localizeMap.get(OsuInfo.valueOf("BgPath")) + "\",0,0";
                        localizeMap.put(localizedInfo, tempValue);
                        continue;
                    }
                }
                tempValue = localizedInfo.unLocalize().getDefaultValue();
                localizeMap.put(localizedInfo, tempValue);
                continue;
            }
            localizeMap.put(localizedInfo, tempValue);
        }
        return localizeMap;
    }

}
