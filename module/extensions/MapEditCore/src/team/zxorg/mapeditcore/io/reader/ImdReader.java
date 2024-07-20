package team.zxorg.mapeditcore.io.reader;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.datas.ImdMapData;
import team.zxorg.mapeditcore.mapElement.note.Flick;
import team.zxorg.mapeditcore.mapElement.note.Hold;
import team.zxorg.mapeditcore.mapElement.note.MixNote;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImdReader extends MapReader{
    private ByteBuffer bf;
    private int mapLength;
    private int timingAmount;
    private int tabRaws;
    private int orbitCount;

    @Override
    public ImdReader readFile(File file) throws IOException {
        this.file = file;
        InputStream inputStream = new FileInputStream(file);
        byte[] data = inputStream.readAllBytes();
        bf = ByteBuffer.wrap(data);
        inputStream.close();
        bf.order(ByteOrder.LITTLE_ENDIAN);
        ready();

        readingNoteIndex = 0;
        //获得文件名
        String fileName = file.getName();
        //初始化
        int maxOrbit = Integer.parseInt(
                fileName.substring(
                        fileName.indexOf("_") + 1,
                        fileName.lastIndexOf("_")
                ).replaceAll("k", "")
        );
        orbitCount = maxOrbit;

        //读取首时间点bpm作为基准bpm
        //从第13字节读取double
        bf.position(12);

        //设置参考基准bpm
        preferenceBpm = bf.getDouble();

        //跳回首timingPoint处
        //开始读取全部timing
        bf.position(8);
        double tempBpm = 0;
        for (int i = 0; i < timingAmount; i++) {
            int time = bf.getInt();
            double bpm = bf.getDouble();
            if (bpm != tempBpm){
                tempBpm=bpm;
                timings.add(
                        new Timing(
                                time, bpm, 1
                        )
                );
            }
        }

        //03 03
        bf.getShort();

        //表格行数
        int tabRows = bf.getInt();

        //组合键转换模式
        MixNote mixNote = null;

        //11字节为一组,读取所有按键
        for (int i = 0; i < tabRows; i++) {
            //按键类型(非得放一个字节里,然后后面跟一个没用字节,不nt吗这是)
            byte noteTypePar = bf.get();

            //跳1字节
            bf.get();

            //按键时间戳
            int timeStamp = bf.getInt();

            //按键组合参数 6头 2中 A尾
            byte complexPar = (byte) ((noteTypePar & 0xf0) >> 4);

            //按键类型 0单 1滑 2长
            byte noteType = (byte) (noteTypePar & 0x0f);

            //物件轨道
            byte orbit = (byte) (bf.get()+1);

            //物件参数 类型为长时代表持续时间 类型为滑时为滑动参数,参考有轨滑键文档
            int notePar = bf.getInt();

            Note tempNote = null;
            //初始化物件
            switch (noteType){
                case 0->//单键
                        tempNote = new Note(timeStamp,orbit,maxOrbit);
                case 1->//滑键
                        tempNote = new Flick(timeStamp , orbit,maxOrbit , notePar);
                case 2->//长键
                        tempNote = new Hold(timeStamp , orbit,maxOrbit , notePar);
            }

            if (complexPar != 0){
                //组合参数不为零,处理组合键
                switch (complexPar){
                    case 0x06->{
                        //组合头,重新初始化缓存组合键,并将此首按键加入组合键中
                        mixNote = new MixNote(timeStamp, orbit,maxOrbit);
                        mixNote.addNote(tempNote.clone());
                    }
                    case 0x02->{
                        //组合键中间部分,直接加入缓存组合键中
                        if (mixNote != null) {
                            mixNote.addNote(tempNote.clone());
                        }else {
                            throw new RuntimeException("读取到非法物件");
                        }
                    }
                    case 0x0A->{
                        //组合键尾部,先将尾按键加入组合键,然后克隆组合键加入zxMap,然后清空缓存给下一个组合键使用
                        if (mixNote != null) {
                            mixNote.addNote(tempNote);
                        }else
                            throw new RuntimeException("读取到非法物件");
                        notes.add(mixNote.clone());
                        mixNote.clearNote();
                        mixNote = null;
                    }
                }
            } else //组合参数为0,直接加入zxMap
                notes.add(tempNote);
        }
        //排序一遍物件
        notes.sort(Note::compareTo);
//        System.out.println(notes);
        return this;
    }

    @Override
    public ZXMap readMap(){
        ZXMap map = new ZXMap();
        map.notes.addAll(notes);
        map.timings.addAll(timings);
        map.preferenceBpm = preferenceBpm;
        map.orbitCount = orbitCount;
        map.metaData = readMeta();
        return map;
    }

    @Override
    public ImdMapData readMeta() {
        String fileName = file.getName();
        ImdMapData mapData = new ImdMapData();

        //检查文件名
        boolean illegalFile = !fileName.endsWith(".imd") && !((fileName.length() - fileName.replaceAll("_", "").length()) == 2);
        if (illegalFile) {
            throw new RuntimeException("imd文件名非法,请检查文件名格式是否为'songname_nk_ez.imd'");
        }else {
            //截取文件标题
            String titleUnicode = fileName. substring(0, fileName.indexOf("_"));
            mapData.setTitleUnicode(titleUnicode);
            //版本
            String version = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".imd"));
            mapData.setMapVersion(version);

            //读取表格行数和图长度
            mapData.setTabRows(tabRaws);
            mapData.setMapLength(mapLength);

            this.mapData = mapData;

        }
        return mapData;
    }

    @Override
    public String getSuffix() {return ".imd";}

    @Override
    protected void ready() {
        super.ready();
        mapLength = bf.getInt();
        timingAmount = bf.getInt();
        bf.position(8+timingAmount*12 + 2 );
        tabRaws = bf.getInt();
    }
}
