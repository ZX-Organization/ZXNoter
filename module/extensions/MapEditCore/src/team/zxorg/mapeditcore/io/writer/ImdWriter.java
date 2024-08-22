package team.zxorg.mapeditcore.io.writer;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.ZXMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.ImdMapData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.Flick;
import team.zxorg.mapeditcore.mapElement.note.Hold;
import team.zxorg.mapeditcore.mapElement.note.MixNote;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImdWriter extends MapWriter {
    private final ByteBuffer bf;
    private ImdMapData data;

    public ImdWriter(ZXMap map) {
        super(map);
        int size = 0;
        size += 4;//总时长
        size += 4;//时间点数
        size += map.timings.size() * 12;//时间点数据
        size += 2;//03 03
        size += 4;//表格行数

        int objectCount = 0;
        //计算物件数量（包括子键）
        for (IMapElement note : map.notes) {
            if (note instanceof MixNote mixNote)
                objectCount += mixNote.getChildNotes().size();
            else objectCount++;
        }
        size += objectCount*11;
        bf = ByteBuffer.allocate(size);
        bf.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public void writeFile() throws IOException {
        //检查谱面数据类型
        if (map.metaData instanceof ImdMapData data1)
             this.data = data1;
        else if (map.metaData instanceof ZXMapData zxMapData)
            data = zxMapData.toImdMapData(map);
        else
            data = new ImdMapData();

        bf.putInt(data.getMapLength());//谱面长度
        bf.putInt(map.timings.size());//时间点数

        //时间点
        for (IMapElement timing: map.timings){
            bf.putInt(timing.getTime());//时间戳
            bf.putDouble(((Timing)timing).getBpm());//bpm
        }

        bf.putShort((short) 771);// 03 03
        bf.putInt(data.getTabRows());//表格行数
        int size = 0;
        //所有物件
        for (IMapElement note : map.notes){
            if (note instanceof Note baseNote){
                //组合键写出
                if (baseNote instanceof MixNote mixNote){
                    //类型处高位6 2 A顺序头 中 尾,低位正常
                    //头
                    if (mixNote.getChildNotes().size() !=0){
                        Note head = (Note) mixNote.getChildNotes().get(0);
                        int headType = 96 + head.getImdNoteType();//头按键类型6x
                        bf.put((byte) headType);
                        writeNoteDataWithoutType(bf,head);

                        for (int i = 1; i < mixNote.getChildNotes().size()-1; i++) {
                            Note tempNote = (Note) mixNote.getChildNotes().get(i);
                            //身体
                            int bodyType = 32 + tempNote.getImdNoteType();
                            bf.put((byte) bodyType);//身体按键类型2x
                            writeNoteDataWithoutType(bf,tempNote);
                        }

                        //尾
                        Note end = (Note) mixNote.getChildNotes().get(mixNote.getChildNotes().size()-1);
                        int endType = -96 + end.getImdNoteType();//尾按键类型Ax
                        bf.put((byte) endType);
                        writeNoteDataWithoutType(bf,end);
                        //System.out.println("已写->"+ (size+= complexNote.notes.size()));
                    }
                }else {
                    //一般键写出
                    bf.put((byte) ((Note)note).getImdNoteType());//按键类型
                    writeNoteDataWithoutType(bf, (Note) note);
                }
            }
        }
        FileOutputStream fos = new FileOutputStream(getPath());
        fos.write(bf.array());
        fos.flush();
        fos.close();
    }
    /**
     * 私有按键数据写入缓冲区方法(不写按键类型)10字节
     * @param bf 要写入的字节缓冲区
     * @param note 要写入的按键
     */
    private void writeNoteDataWithoutType(ByteBuffer bf,Note note){
        bf.put((byte) 0);//00固定值
        bf.putInt(note.getTime());//时间戳
        bf.put((byte) Math.rint(note.getPosition()/(1f/map.orbitCount)));//轨道
        //参数
        if (note instanceof Flick flick){
            //滑键
            int slideArg = 1;
            if (flick.getDirection() == 0){
                slideArg *= -1* (int)(Math.rint(flick.getSlideLength()/(1f/map.orbitCount)));
            }else
                slideArg *= (int)(Math.rint(flick.getSlideLength()/(1f/map.orbitCount)));
            bf.putInt(slideArg);
        } else if (note instanceof Hold hold) {
            //长键
            bf.putInt(hold.getDuration());
        }else {
            //单键
            bf.putInt(0);
        }
    }

    @Override
    public File getPath() {
        File file = null;
        boolean legal = true;
        if (directory.isDirectory()) legal = false;
        else if (directory.isFile()) file = directory;
        //检查文件名

        if (file == null||!file.getName().contains("_")) legal=false;
        else if (file.getName().length() - file.getName().replaceAll("_","").length() == 2){
            String[] ss = file.getName().replace(".imd","").split("_");
            if (!ss[0].contains(data.getMscPath().replace(".mp3",""))) legal = false;
            if (!ss[1].contains(map.orbitCount + "k")) legal = false;
            if (!ss[2].contains(data.getMapVersion())) legal = false;
            if (!ss[0].contains(data.getCreator())) legal = false;
        }
        if (!legal){
            String fileName = "";
            fileName+=data.getTitleUnicode();
            if (data.getCreator() != null) {
                fileName+="("+data.getCreator()+")";
            }
            fileName+="_"+map.orbitCount+"k_";
            fileName+= data.getMapVersion()+getSuffix();
            file = new File(directory + "/" + fileName);
        }
        return file;
    }

    @Override
    public ImdWriter setDirectory(File directory) {
        this.directory = directory;
        return this;
    }

    @Override
    public String getSuffix() {
        return ".imd";
    }
}
