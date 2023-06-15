package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfos.ImdInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ImdWriter {
    /**
     * 写出zxMap到路径,不可编辑文件名(文件名保存一部分信息)
     * @param zxMap 要写出的zxMap
     * @param checkedLocalizedInfos 经过检查的本地化imd信息hashmap,不可有null
     * @param path 要写出到的路径
     */
    public static void writeOut(ZXMap zxMap,HashMap<ImdInfo,String> checkedLocalizedInfos, Path path) throws NoSuchFieldException, IOException {
        //检查本地化信息
        if (checkedLocalizedInfos.size() == ImdInfo.values().length) {
            Collection<String> values = checkedLocalizedInfos.values();
            for (String value:values){
                if (value == null) {
                    throw new NoSuchFieldException("丢失本地化信息!");
                }
            }
        }else {
            throw new NoSuchFieldException("丢失本地化字段!");
        }
        double baseBpm = Double.parseDouble(checkedLocalizedInfos.get(ImdInfo.ImdBpm));
        //检查本地化信息通过
        //计算最终文件大小
        int absoluteNotesSize = 0;
        for (BaseNote note: zxMap.notes){
            if (note instanceof ComplexNote complexNote){
                for (FixedOrbitNote ignored : complexNote.notes){
                    absoluteNotesSize ++;
                }
            }
            absoluteNotesSize ++;
        }
        int cap =
                4+4+zxMap.timingPoints.size() * 12 +//谱面长度(int4)+时间点数(int4)+所有时间点(bpm-double8+时间戳int4)
                2+4+absoluteNotesSize * 11;//03 03固定值(short2)+表格行数(int4)+所有物件(按键类型byte1+00固定值byte1+时间戳int4+轨道byte1+按键参数int4)

        ByteBuffer bf = ByteBuffer.allocate(cap);
        bf.order(ByteOrder.LITTLE_ENDIAN);
        bf.putInt(Integer.parseInt(checkedLocalizedInfos.get(ImdInfo.MapLength)));//谱面长度
        bf.putInt(zxMap.timingPoints.size());//时间点数

        //时间点
        for (Timing timing: zxMap.timingPoints){
            bf.putInt((int)timing.timingStamp);//时间戳
            bf.putDouble(timing.bpmRatio * baseBpm);//bpm
        }

        bf.putShort((short) 771);// 03 03
        bf.putInt(Integer.parseInt(checkedLocalizedInfos.get(ImdInfo.TabRows)));//表格行数

        //所有物件
        for (BaseNote note : zxMap.notes){
            if (note instanceof FixedOrbitNote fixedOrbitNote){
                //组合键写出
                if (fixedOrbitNote instanceof ComplexNote complexNote){
                    //类型处高位6 2 A顺序头 中 尾,低位正常

                    //头
                    BaseNote head = complexNote.notes.get(0);
                    int headType = 96 + head.getImdNoteType();//头按键类型6x
                    bf.put((byte) headType);
                    writeNoteDataWithoutType(bf,head);

                    for (int i = 1; i < complexNote.notes.size()-1; i++) {
                        BaseNote tempNote = complexNote.notes.get(i);
                        //身体
                        int bodyType = 32 + tempNote.getImdNoteType();
                        bf.put((byte) bodyType);//身体按键类型2x
                        writeNoteDataWithoutType(bf,tempNote);
                    }

                    //尾
                    BaseNote end = complexNote.notes.get(complexNote.notes.size()-1);
                    int endType = -96 + end.getImdNoteType();//尾按键类型Ax
                    bf.put((byte) endType);
                    writeNoteDataWithoutType(bf,end);
                    continue;
                }

                //一般键写出
                bf.put((byte) note.getImdNoteType());//按键类型
                writeNoteDataWithoutType(bf,note);
            }
        }
        FileOutputStream fos;
        if (path.endsWith(".imd")){
            //自拟文件名(丢失部分数据)
            fos = new FileOutputStream(path.toAbsolutePath().toFile());
        }else {
            //自动生成文件名
            String fileName = checkedLocalizedInfos.get(ImdInfo.ImdTitle)+
                    "_"+checkedLocalizedInfos.get(ImdInfo.ImdKeyCount)+
                    "k_"+checkedLocalizedInfos.get(ImdInfo.ImdVersion)+".imd";
            if (fileName.contains("?"))
                fileName=fileName.replaceAll("\\?","");
            fos = new FileOutputStream(path.toAbsolutePath()+"/"+fileName);
        }
        fos.write(bf.array());
        fos.flush();
        fos.close();
    }

    /**
     * 私有按键数据写入缓冲区方法(不写按键类型)10字节
     * @param bf 要写入的字节缓冲区
     * @param note 要写入的按键
     */
    private static void writeNoteDataWithoutType(ByteBuffer bf,BaseNote note){
        bf.put((byte) 0);//00固定值
        bf.putInt((int)note.timeStamp);//时间戳
        bf.put((byte) note.getOrbit());//轨道
        //参数
        if (note instanceof SlideNote slideNote){
            //滑键
            bf.putInt(slideNote.slideArg);
        } else if (note instanceof LongNote longNote) {
            //长键
            bf.putInt((int) longNote.sustainedTime);
        }else {
            //单键
            bf.putInt(0);
        }
    }

    /**
     * 检查imd反本地化信息,并将可转化的信息本地化
     * 不可转换:自动计算值
     * 不可转换且不可计算:null
     * @param zxMap 要检查imd本地化信息的zxMap
     * @return 返回全部写出所需的本地化信息hashmap,缺失为null
     */
    public static HashMap<ImdInfo,String> checkLocalizedInfos(ZXMap zxMap){
        //处理本地化信息
        UnLocalizedMapInfo unLocalizedMapInfo = zxMap.unLocalizedMapInfo;
        //列举写出需要的信息
        ImdInfo[] infos = ImdInfo.values();
        HashMap<ImdInfo , String> localizeMap = new HashMap<>();
        //初始化带key无value的信息表
        for (ImdInfo imdInfo : infos){
            localizeMap.put(imdInfo,null);
        }
        //列举需要的的本地化信息
        Set<ImdInfo> localizedInfoSet = localizeMap.keySet();
        //列举目前有的反本地化信息,判断有无和上面所需信息表反本地化信息相同的信息,直接使用
        for (ImdInfo localizedInfo : localizedInfoSet){
            //通过本地化信息表查询所有反本地化信息
            String tempValue = unLocalizedMapInfo.getInfo(localizedInfo.unLocalize());
            if (tempValue == null) {
                //反本地化信息表中没有则代表null,使用默认值或计算
                switch (localizedInfo){
                    case MapLength -> {
                        tempValue = String.valueOf(zxMap.notes.get(zxMap.notes.size()-1).timeStamp);
                        break;
                    }
                    case TimingCount -> {
                        tempValue = String.valueOf(zxMap.timingPoints.size());
                        break;
                    }
                    case TabRows -> {
                        //自动计算表格行数
                        int tabRows = 0;
                        for (BaseNote note : zxMap.notes){
                            if (note instanceof ComplexNote complexNote){
                                for (BaseNote ignored : complexNote.notes){
                                    tabRows ++;
                                }
                            }else {
                                tabRows ++;
                            }
                        }
                        tempValue = String.valueOf(tabRows);
                        break;
                    }
                    case ImdVersion -> {
                        tempValue = ImdInfo.ImdVersion.getDefaultValue();
                    }
                    case ImdBgPath -> {
                        //自动使用标题名
                        tempValue = unLocalizedMapInfo.getInfo("Title");
                    }
                }
            }
            localizeMap.put(localizedInfo , tempValue);
        }
        return localizeMap;
    }
}
