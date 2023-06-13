package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.LocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.OsuInfos;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.OsuCustomLongNote;
import team.zxorg.zxnoter.note.fixedorbit.OsuCustomNote;
import team.zxorg.zxnoter.note.timing.ZxTiming;
import team.zxorg.zxnoter.note.timing.Timing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * osu读取器(osu!mania)
 */
public class OsuReader {
    public static ZXMap readFile(Path path) throws IOException {
        BufferedReader bfReader = new BufferedReader(new FileReader(path.toFile()));
        String readTemp;
        //读取模式[General]等
        byte mode = -1;
        //是否进入事件值读取模式
        boolean eventValueMode = false;
        //事件缓存
        String eventNameTemp = "";
        //获取基准bpm
        boolean getBaseBpm = true;

        ZXMap zxMap = new ZXMap();
        ArrayList<BaseNote> allNotes = new ArrayList<>();
        ArrayList<Timing> timingPoints = new ArrayList<>();
        LocalizedMapInfo localizedMapInfo = new LocalizedMapInfo();
        readTemp = bfReader.readLine();

        localizedMapInfo.addInfo(
                OsuInfos.valueOf(readTemp.substring(0 , readTemp.lastIndexOf("v")).replaceAll(" ","")).unLocalize(),
                readTemp.substring(readTemp.lastIndexOf("v"))
        );
        int keyCount = 0;
        while ((readTemp = bfReader.readLine()) != null){
            //读取谱面信息
            if (readTemp.startsWith("[")){
                //处理事件属性末尾未定义情况
                if (eventValueMode){
                    localizedMapInfo.addInfo(eventNameTemp , "");
                }
                //设置读取模式
                if ("[Events]".equals(readTemp)){
                    mode = 0;
                    continue;
                }
                if ("[TimingPoints]".equals(readTemp)){
                    mode = 1;
                    continue;
                }
                if ("[HitObjects]".equals(readTemp)){
                    mode = 2;
                    continue;
                }
                mode = -1;
                continue;
            }
            //按照设置的读取模式读取信息
            if ("".equals(readTemp)){
                //跳过空行
                continue;
            }
            switch (mode){
                case -1->{
                    //带冒号属性
                    String name = readTemp.substring(0 , readTemp.indexOf(":"));
                    String value = readTemp.substring(readTemp.lastIndexOf(":") + 1);
                    localizedMapInfo.addInfo(
                            name ,
                            value
                    );
                    if ("CircleSize".equals(name))
                        keyCount = Integer.parseInt(value);
                    continue;
                }
                case 0->{
                    //事件处理
                    String key = "";
                    if (readTemp.startsWith("//")){
                        key = readTemp.replaceAll("/", "").replaceAll("\\(", "").
                                replaceAll("\\)", "").
                                replaceAll(" ", "");
                    }
                    if (eventValueMode){
                        //事件值读取模式
                        if (readTemp.startsWith("//")){
                            //值读取模式又一次读到事件名
                            localizedMapInfo.addInfo(eventNameTemp , "");
                            eventNameTemp = OsuInfos.valueOf(key).unLocalize();
                        }else {
                            //值读取
                            localizedMapInfo.addInfo(eventNameTemp , readTemp);
                            eventValueMode = false;
                        }
                    }else {
                        if (readTemp.startsWith("//")){
                            eventValueMode = true;
                            eventNameTemp = OsuInfos.valueOf(key).unLocalize();
                        }
                    }
                    continue;
                }
                case 1->{
                    //时间点处理
                    String[] allPars = readTemp.split(",");
                    boolean isExtendTiming = (Integer.parseInt(allPars[6]) == 1);
                    if (getBaseBpm){
                        //基准bpm时间点添加
                        timingPoints.add(
                                new ZxTiming(
                                        Integer.parseInt(allPars[0]),
                                        60000/Double.parseDouble(allPars[1]),
                                        Integer.parseInt(allPars[2]),
                                        Integer.parseInt(allPars[3]),
                                        Integer.parseInt(allPars[4]),
                                        Integer.parseInt(allPars[5]),
                                        isExtendTiming,
                                        Integer.parseInt(allPars[7])
                                )
                        );
                        getBaseBpm = false;
                    }
                    timingPoints.add(
                            new ZxTiming(
                                    Integer.parseInt(allPars[0]),
                                    100/Math.abs(Double.parseDouble(allPars[1])),
                                    Integer.parseInt(allPars[2]),
                                    Integer.parseInt(allPars[3]),
                                    Integer.parseInt(allPars[4]),
                                    Integer.parseInt(allPars[5]),
                                    isExtendTiming,
                                    Integer.parseInt(allPars[7])
                            )
                    );
                    continue;
                }
                case 2->{
                    //物件处理

                    //分割物件参数集
                    String[] notePars = readTemp.substring(0 , readTemp.indexOf(":") ).split(",");
                    //分割物件音效组参数集
                    String[] sampleSetPars = readTemp.substring(readTemp.indexOf(":")-1).split(":");

                    //预定义物件
                    FixedOrbitNote note;

                    //通过参数列表长度区分长键单键
                    if (notePars.length == 5 ){
                        //单键
                        note = new OsuCustomNote(
                                Integer.parseInt(notePars[2]),//时间戳
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512),//轨道
                                Integer.parseInt(notePars[3]),
                                Integer.parseInt(notePars[4]),
                                sampleSetPars//音效组参数
                                );
                    }else{
                        //长键
                        note = new OsuCustomLongNote(
                                Integer.parseInt(notePars[2]),
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512),
                                Integer.parseInt(notePars[3]),
                                Integer.parseInt(notePars[4]),
                                Integer.parseInt(notePars[5]),
                                sampleSetPars
                        );
                    }
                    //音效组参数列表长度为5代表有定义自定义音效文件
                    if (sampleSetPars.length == 5){
                        note.setSound(sampleSetPars[4]);
                    }
                    allNotes.add(note);
                    continue;
                }
            }

        }
        zxMap.notes = allNotes;
        zxMap.timingPoints = timingPoints;
        zxMap.localizedMapInfo = localizedMapInfo;


        return zxMap;
    }

}