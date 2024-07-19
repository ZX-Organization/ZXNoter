package team.zxorg.mapeditcore.io;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.ZXMetaData;
import team.zxorg.mapeditcore.map.mapdata.datas.OsuMapData;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.note.OsuHold;
import team.zxorg.mapeditcore.mapElement.note.OsuNote;
import team.zxorg.mapeditcore.mapElement.timing.OsuTiming;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OsuReader extends MapReader{
    BufferedReader bfReader;
    int orbitCount;
    @Override
    public OsuReader readFile(File file) throws IOException {
        this.file = file;
        ready();
        bfReader = new BufferedReader(new FileReader(file));
        OsuMapData mapData = new OsuMapData();
        this.mapData = mapData;

        String readTemp;
        //读取模式[General]等
        byte mode = -1;
        //当前读取到的类
        String current;
        //是否进入事件值读取模式
        boolean eventValueMode = false;
        //事件缓存
        //获取基准bpm
        boolean getBaseBpm = true;
        double baseBpm = 0.;

        bfReader.readLine();

        int keyCount = 0;
        int eventIndex=0;
        while ((readTemp = bfReader.readLine()) != null){
            //按照设置的读取模式读取信息
            if ("".equals(readTemp)){
                //跳过空行
                continue;
            }
            //读取谱面信息
            if (readTemp.startsWith("[")){
                //设置读取模式
                if ("[General]".equals(readTemp)){
                    mode = 0;
                    continue;
                }
                if ("[Editor]".equals(readTemp)){
                    mode = 1;
                    continue;
                }
                if ("[Metadata]".equals(readTemp)){
                    mode = 2;
                    continue;
                }
                if ("[Difficulty]".equals(readTemp)){
                    mode = 3;
                    continue;
                }
                if ("[Events]".equals(readTemp)){
                    mode = 4;
                    continue;
                }
                if ("[TimingPoints]".equals(readTemp)){
                    mode = 5;
                    continue;
                }
                if ("[HitObjects]".equals(readTemp)){
                    mode = 6;
                    continue;
                }
                mode = -1;
                continue;
            }

            switch (mode){
                case 0,1,3->{
                    LinkedHashMap<String,String> tempMap = null;
                    switch (mode){
                        case 0->tempMap = mapData.getGeneralInfo();
                        case 1->tempMap = mapData.getEditorInfo();
                        case 3->tempMap = mapData.getDifficultInfo();
                    }
                    //带冒号属性
                    String name = readTemp.substring(0 , readTemp.indexOf(":"));
                    String value = readTemp.substring(readTemp.lastIndexOf(":") + 1);
                    tempMap.put(name,value);
                    if ("CircleSize".equals(name)) {
                        keyCount = Integer.parseInt(value);
                    }
                }
                case 2->{
                    String name = readTemp.substring(0 , readTemp.indexOf(":"));
                    String value = readTemp.substring(readTemp.lastIndexOf(":") + 1);
                    if ("Title".equals(name))
                        mapData.setTitle(value);
                    if ("TitleUnicode".equals(name))
                        mapData.setTitleUnicode(value);
                    if ("Artist".equals(name))
                        mapData.setArtist(value);
                    if ("ArtistUnicode".equals(name))
                        mapData.setArtistUnicode(value);
                    if ("Creator".equals(name))
                        mapData.setCreator(value);
                    if ("Version".equals(name))
                        mapData.setMapVersion(value);
                    if ("Source".equals(name))
                        mapData.setSource(value);
                    if ("Tags".equals(name))
                        mapData.setTags(value);
                    if ("BeatmapID".equals(name))
                        mapData.setBeatMapId(Integer.parseInt(value.trim()));
                    if ("BeatmapSetID".equals(name))
                        mapData.setBeatMapSetId(Integer.parseInt(value.trim()));
                }
                case 4->{
                    //事件处理
                    String key = "";
                    if (readTemp.startsWith("//")){
                        //注释
                        eventIndex++;
                    }else {
                        mapData.getEventInfo().put(OsuMapData.eventDefaultInfo[eventIndex-1],readTemp);
                    }
                }
                case 5->{
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
                        if (getBaseBpm){
                            preferenceBpm = baseBpm;
                            getBaseBpm = false;
                        }

                    }

                    if (isExtendTiming){
                        //继承
                        //bpm时间点添加
                        timings.add(
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
                        timings.add(
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
                }
                case 6->{
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
                        else {
                            OsuHold osuHold = (OsuHold) note;
                            osuHold.setSoundFile(sampleSetPars[4]);
                        }
                    }
                    notes.add(note);
                }
            }
        }
        orbitCount = keyCount;

        return this;
    }

    @Override
    public ZXMap readMap() {
        ZXMap zxMap = new ZXMap();
        zxMap.orbitCount = orbitCount;
        zxMap.preferenceBpm = preferenceBpm;
        zxMap.notes.addAll(notes);
        zxMap.timings.addAll(timings);
        zxMap.metaData = (OsuMapData) mapData;
        return zxMap;
    }

    @Override
    public OsuMapData readMeta() {
        return (OsuMapData) mapData;
    }

    @Override
    public String getSuffix() {
        return "osu";
    }

}
