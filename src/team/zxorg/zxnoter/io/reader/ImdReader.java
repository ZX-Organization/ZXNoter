package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.info.map.ImdInfo;
import team.zxorg.zxnoter.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * imd读取器(节奏大师)
 */
public class ImdReader implements MapReader{
    private Path readPath;
    private ArrayList<Timing> timingPoints;
    private UnLocalizedMapInfo unLocalizedMapInfo;

    @Override
    public String getSupportFileExtension() {
        return "imd";
    }

    @Override
    public Path getReadPath() {
        return readPath;
    }

    @Override
    public ZXMap read(Path path) throws IOException {
        String fileName = path.getFileName().toString();
        //检查合法性
        boolean illegalFile = !fileName.endsWith(".imd") && !((fileName.length() - fileName.replaceAll("_", "").length()) == 2);
        if (illegalFile) {
            throw new RuntimeException("imd文件名非法");
        }

        InputStream inputStream = Files.newInputStream(path);
        byte[] data = inputStream.readAllBytes();
        ByteBuffer bf = ByteBuffer.wrap(data);
        inputStream.close();
        bf.order(ByteOrder.LITTLE_ENDIAN);

        //初始化
        ZXMap map = new ZXMap();
        unLocalizedMapInfo = new UnLocalizedMapInfo();
        ArrayList<BaseNote> allNotes = new ArrayList<>();

        //截取文件标题
        String title = fileName.substring(0, fileName.indexOf("_"));
        //谱面标题
        unLocalizedMapInfo.setInfo(ImdInfo.ImdTitle.unLocalize(), title);
        //图片路径
        unLocalizedMapInfo.setInfo(ImdInfo.ImdBgPath.unLocalize(), title + ".png");
        //音频路径
        unLocalizedMapInfo.setInfo(ImdInfo.ImdAudioPath.unLocalize(), title + ".mp3");
        //键数
        unLocalizedMapInfo.setInfo(ImdInfo.ImdKeyCount.unLocalize(), fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_")).replaceAll("k", ""));
        //版本
        unLocalizedMapInfo.setInfo(ImdInfo.ImdVersion.unLocalize(), fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".imd")));
        //图长度
        unLocalizedMapInfo.setInfo(ImdInfo.MapLength.unLocalize(), String.valueOf(bf.getInt()));

        //图时间点数
        int timingAmount = bf.getInt();
        unLocalizedMapInfo.setInfo(ImdInfo.TimingCount.unLocalize(), String.valueOf(timingAmount));

        //读取首时间点bpm作为基准bpm
        //从第13字节读取double
        bf.position(12);
        double baseBpm = bf.getDouble();
        unLocalizedMapInfo.setInfo(ImdInfo.valueOf("ImdBpm").unLocalize(),String.valueOf(baseBpm));

        //跳回首timingPoint处
        bf.position(8);
        double tempBpm = 0;
        timingPoints = new ArrayList<>(timingAmount);
        for (int i = 0; i < timingAmount; i++) {
            int timeStamp = bf.getInt();
            double bpm = bf.getDouble();
            if (bpm != tempBpm){
                tempBpm=bpm;
                timingPoints.add(
                        new Timing(
                                timeStamp, bpm,true,bpm
                        )
                );
            }
        }
        //03 03
        bf.getShort();
        //表格行数
        int tabRows = bf.getInt();
        unLocalizedMapInfo.setInfo(ImdInfo.TabRows.unLocalize(), String.valueOf(tabRows));
        //组合键转换模式

        ComplexNote tempComplexNote = null;
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
            byte orbit = bf.get();
            //物件参数 类型为长时代表持续时间 类型为滑时为滑动参数,参考有轨滑键文档
            int notePar = bf.getInt();

            FixedOrbitNote tempNote = null;
            //初始化物件
            switch (noteType){
                case 0->{
                    //单键
                    tempNote = new FixedOrbitNote(timeStamp,orbit);
                    break;
                }
                case 1->{
                    //滑键
                    tempNote = new SlideNote(timeStamp , orbit , notePar);
                    break;
                }
                case 2->{
                    //长键
                    tempNote = new LongNote(timeStamp , orbit , notePar);
                }
            }

            if (complexPar != 0)
                //组合参数不为零,处理组合键
                switch (complexPar){
                    case 0x06->{
                        //组合头,重新初始化缓存组合键,并将此首按键加入组合键中
                        tempComplexNote = new ComplexNote(timeStamp, orbit);
                        tempComplexNote.addNote(tempNote.clone());
                        break;
                    }
                    case 0x02->{
                        //组合键中间部分,直接加入缓存组合键中
                        tempComplexNote.addNote(tempNote.clone());
                        break;
                    }
                    case 0x0A->{
                        //组合键尾部,先将尾按键加入组合键,然后克隆组合键加入zxMap,然后清空缓存给下一个组合键使用
                        tempComplexNote.addNote(tempNote);
                        allNotes.add(tempComplexNote.clone());
                        tempComplexNote.clearNote();
                        tempComplexNote = null;
                    }
                }
            else {
                //组合参数为0,直接加入zxMap
                allNotes.add(tempNote);
            }
        }
        map.notes = allNotes;
        allNotes.sort(BaseNote::compareTo);
        completeInfo();
        map.unLocalizedMapInfo = unLocalizedMapInfo;
        map.timingPoints = timingPoints;

        return map;
    }

    @Override
    public void completeInfo() {
        for (ZXMInfo info:ZXMInfo.values())
            if (!unLocalizedMapInfo.allInfo.containsKey(info)){
                unLocalizedMapInfo.allInfo.put(info, info.getDefaultValue());
            }
            unLocalizedMapInfo.allInfo.put(ZXMInfo.TimingCount,String.valueOf(timingPoints.size()));
    }
}
