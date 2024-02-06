package team.zxorg.mapeditcore.map.mapio;

import team.zxorg.mapeditcore.map.MapMetaData;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.note.Note;
import team.zxorg.mapeditcore.note.OsuHold;
import team.zxorg.mapeditcore.note.OsuNote;
import team.zxorg.mapeditcore.timing.OsuTiming;
import team.zxorg.mapeditcore.timing.Timing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class OsuReader extends MapReader{

    public OsuReader(Path filePath) {
        super(filePath);
    }

    @Override
    public ZXMap readMap() throws IOException {
        BufferedReader bfReader = new BufferedReader(new FileReader(filePath.toFile()));
        String readTemp;
        //读取模式[General]等
        byte mode = -1;
        //是否进入事件值读取模式
        boolean eventValueMode = false;
        //事件缓存
        //OsuInfo eventInfo = null;
        //获取基准bpm
        boolean getBaseBpm = true;
        double baseBpm = 0.;

        ZXMap zxMap = new ZXMap();
        ArrayList<Note> allNotes = new ArrayList<>();
        ArrayList<Timing> timingPoints = new ArrayList<>();
        //unLocalizedMapInfo = new UnLocalizedMapInfo();
        readTemp = bfReader.readLine();

        /*unLocalizedMapInfo.setInfo(
                OsuInfo.valueOf(readTemp.substring(0 , readTemp.lastIndexOf("v")).replaceAll(" ","")).unLocalize(),
                readTemp.substring(readTemp.lastIndexOf("v"))
        );*/
        int keyCount = 0;
        while ((readTemp = bfReader.readLine()) != null){
            //按照设置的读取模式读取信息
            if ("".equals(readTemp)){
                //跳过空行
                continue;
            }
            //读取谱面信息
            if (readTemp.startsWith("[")){
                //处理事件属性末尾未定义情况
                if (eventValueMode){
                    //unLocalizedMapInfo.setInfo(eventInfo.unLocalize() , "");
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

            switch (mode){
                case -1->{
                    //带冒号属性
                    String name = readTemp.substring(0 , readTemp.indexOf(":"));
                    String value = readTemp.substring(readTemp.lastIndexOf(":") + 1);
                    /*unLocalizedMapInfo.setInfo(
                            OsuInfo.valueOf(name).unLocalize() ,
                            value.trim()
                    );*/
                    if ("CircleSize".equals(name)) {
                        keyCount = Integer.parseInt(value);
                    }
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
                            /*unLocalizedMapInfo.setInfo(eventInfo.unLocalize() , "");
                            eventInfo = OsuInfo.valueOf(key);*/
                        }else {
                            //值读取
//                            unLocalizedMapInfo.setInfo(eventInfo.unLocalize() , readTemp);
                            eventValueMode = false;
                        }
                    }else {
                        if (readTemp.startsWith("//")){
                            eventValueMode = true;
//                            eventInfo = OsuInfo.valueOf(key);
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

                    if(beatPar > 0){
                        baseBpm = 60000/beatPar;
                    }

                    if (isExtendTiming){
                        //继承
                        //变速bpm时间点添加
                        zxMap.timings.add(
                                new OsuTiming(
                                        timeStamp,
                                        baseBpm,
                                        baseBpm,
                                        Integer.parseInt(allPars[2]),
                                        Integer.parseInt(allPars[3]),
                                        Integer.parseInt(allPars[4]),
                                        Integer.parseInt(allPars[5]),
                                        true,
                                        Integer.parseInt(allPars[7])
                                )
                        );
                    }else {
                        double speed = 100/Math.abs(beatPar) * baseBpm;
                        //不继承(变速)
                        zxMap.timings.add(
                                new OsuTiming(
                                        timeStamp,
                                        baseBpm,
                                        speed,
                                        Integer.parseInt(allPars[2]),
                                        Integer.parseInt(allPars[3]),
                                        Integer.parseInt(allPars[4]),
                                        Integer.parseInt(allPars[5]),
                                        false,
                                        Integer.parseInt(allPars[7])
                                )
                        );
                    }
                    continue;
                }
                case 2->{
                    //物件处理
                    String noteStr;
                    String sampleStr;
                    if (readTemp.contains(":")){
                        //物件处理
                        noteStr = readTemp.substring(0 , readTemp.lastIndexOf(","));
                        sampleStr = readTemp.substring(noteStr.length()+1);

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
                    }else {
                        noteStr = readTemp;
                        sampleStr = "0:0:0:0:";
                    }
                    //分割物件参数集
                    String[] notePars = noteStr.split(",");
                    //分割物件音效组参数集
                    String[] sampleSetPars = sampleStr.split(":");

                    //预定义物件
                    Note note;
                    //通过参数列表长度区分长键单键
                    if (notePars.length == 5 ){
                        //单键
                        note = new OsuNote(
                                Integer.parseInt(notePars[2]),//时间戳
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512) + 1,//轨道
                                keyCount,//轨道总数
                                Integer.parseInt(notePars[3]),//类型
                                Integer.parseInt(notePars[4]),//打击音效
                                sampleSetPars//音效组参数
                        );
                    }else{
                        //长键
                        int timeStamp = Integer.parseInt(notePars[2]);
                        int endTime = Integer.parseInt(notePars[5]);
                        note = new OsuHold(
                                timeStamp,
                                (int)Math.floor((double) (Integer.parseInt(notePars[0]) * keyCount) / 512) + 1,//轨道
                                keyCount,//轨道总数
                                Integer.parseInt(notePars[3]),//类型
                                Integer.parseInt(notePars[4]),//打击音效
                                endTime-timeStamp,//物件参数(长键持续时间)
                                sampleSetPars
                        );
                    }
                    //音效组参数列表长度为5代表有定义自定义音效文件
                    if (sampleSetPars.length == 5){
                        if (note instanceof OsuNote osuNote)
                            osuNote.setSoundFile(sampleSetPars[4]);
                        else if (note instanceof OsuHold osuHold)
                            osuHold.setSoundFile(sampleSetPars[4]);
                    }
                    zxMap.notes.add(note);
                }
            }
        }
        //unLocalizedMapInfo.setInfo(OsuInfo.Bpm.unLocalize(),String.valueOf(baseBpm));
        //unLocalizedMapInfo.setInfo(ZXMInfo.ObjectCount,String.valueOf(allNotes.size()));
        //completeInfo();
        //zxMap.unLocalizedMapInfo = unLocalizedMapInfo;

        return zxMap;
    }

    @Override
    public Note readNote() {
        return null;
    }

    @Override
    public MapMetaData readMeta() {
        return null;
    }

    @Override
    public String getSuffix() {
        return "osu";
    }

    @Override
    public void ready() {

    }
}
