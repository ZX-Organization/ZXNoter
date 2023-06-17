package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.OsuInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.CustomLongNote;
import team.zxorg.zxnoter.note.fixedorbit.CustomNote;
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
        double baseBpm = 0.;

        ZXMap zxMap = new ZXMap();
        ArrayList<BaseNote> allNotes = new ArrayList<>();
        ArrayList<Timing> timingPoints = new ArrayList<>();
        UnLocalizedMapInfo unLocalizedMapInfo = new UnLocalizedMapInfo();
        readTemp = bfReader.readLine();

        unLocalizedMapInfo.addInfo(
                OsuInfo.valueOf(readTemp.substring(0 , readTemp.lastIndexOf("v")).replaceAll(" ","")).unLocalize(),
                readTemp.substring(readTemp.lastIndexOf("v"))
        );
        int keyCount = 0;
        while ((readTemp = bfReader.readLine()) != null){
            //读取谱面信息
            if (readTemp.startsWith("[")){
                //处理事件属性末尾未定义情况
                if (eventValueMode){
                    unLocalizedMapInfo.addInfo(eventNameTemp , "");
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
                    unLocalizedMapInfo.addInfo(
                            OsuInfo.valueOf(name).unLocalize() ,
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
                            unLocalizedMapInfo.addInfo(eventNameTemp , "");
                            eventNameTemp = OsuInfo.valueOf(key).unLocalize();
                        }else {
                            //值读取
                            unLocalizedMapInfo.addInfo(eventNameTemp , readTemp);
                            eventValueMode = false;
                        }
                    }else {
                        if (readTemp.startsWith("//")){
                            eventValueMode = true;
                            eventNameTemp = OsuInfo.valueOf(key).unLocalize();
                        }
                    }
                    continue;
                }
                case 1->{
                    //时间点处理
                    String[] allPars = readTemp.split(",");
                    boolean isExtendTiming = (Integer.parseInt(allPars[6]) == 1);
                    int timeStamp;
                    if (allPars[0].contains(".")){
                        timeStamp = (int) Double.parseDouble(allPars[0]);
                    }else {
                        timeStamp = Integer.parseInt(allPars[0]);
                    }
                    double beatPar = Double.parseDouble(allPars[1]);
                    if (beatPar >=0){
                        baseBpm = 60000/beatPar;
                        //基准bpm时间点添加
                        timingPoints.add(
                                new ZxTiming(
                                        timeStamp,
                                        baseBpm,
                                        true,
                                        Integer.parseInt(allPars[2]),
                                        Integer.parseInt(allPars[3]),
                                        Integer.parseInt(allPars[4]),
                                        Integer.parseInt(allPars[5]),
                                        isExtendTiming,
                                        Integer.parseInt(allPars[7])
                                )
                        );
                    }else {
                        //变速bpm时间点添加
                        timingPoints.add(
                                new ZxTiming(
                                        timeStamp,
                                        100/Math.abs(beatPar) * baseBpm,
                                        true,
                                        Integer.parseInt(allPars[2]),
                                        Integer.parseInt(allPars[3]),
                                        Integer.parseInt(allPars[4]),
                                        Integer.parseInt(allPars[5]),
                                        isExtendTiming,
                                        Integer.parseInt(allPars[7])
                                )
                        );
                    }
                    continue;
                }
                case 2->{
                    //物件处理
                    String noteStr = readTemp.substring(0 , readTemp.lastIndexOf(","));
                    String sampleStr = readTemp.substring(noteStr.length()+1);
                    //System.out.println("base->"+noteStr);
                    //System.out.println("temp->"+sampleStr);
                    //检查tempStr中是否有五个冒号,有五个冒号证明此物件为长条,且tempStr中包含长条参数

                    int colonSymCount = sampleStr.length() - sampleStr.replaceAll(":","").length();

                    if (colonSymCount == 5){
                        //包含长条参数
                        //取出参数
                        String longNotePar = sampleStr.split(":")[0];
                        //拼接到基础参数字符串
                        noteStr = noteStr +","+ longNotePar;
                        //从原字符串中删除
                        sampleStr = sampleStr.replaceFirst(longNotePar+":","");
                    }

                    //分割物件参数集
                    String[] notePars = noteStr.split(",");
                    //System.out.println("源->"+readTemp);
                    //分割物件音效组参数集
                    String[] sampleSetPars = sampleStr.split(":");

                    //预定义物件
                    FixedOrbitNote note;
                    //System.out.println("音效组->"+Arrays.toString(sampleSetPars));
                    //System.out.println("物件参数->"+Arrays.toString(notePars));
                    //System.out.println();
                    //通过参数列表长度区分长键单键
                    if (notePars.length == 5 ){
                        //单键
                        note = new CustomNote(
                                Integer.parseInt(notePars[2]),//时间戳
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512),//轨道
                                Integer.parseInt(notePars[3]),
                                Integer.parseInt(notePars[4]),
                                sampleSetPars//音效组参数
                                );
                    }else{
                        //长键
                        int timeStamp = Integer.parseInt(notePars[2]);
                        int endTime = Integer.parseInt(notePars[5]);
                        note = new CustomLongNote(
                                timeStamp,
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512),
                                Integer.parseInt(notePars[3]),
                                Integer.parseInt(notePars[4]),
                                endTime-timeStamp,
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
        unLocalizedMapInfo.addInfo("BaseBpm",String.valueOf(baseBpm));
        zxMap.notes = allNotes;
        zxMap.timingPoints = timingPoints;
        zxMap.unLocalizedMapInfo = unLocalizedMapInfo;


        return zxMap;
    }

}