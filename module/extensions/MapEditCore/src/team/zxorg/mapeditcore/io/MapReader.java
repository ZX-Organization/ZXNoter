package team.zxorg.mapeditcore.io;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class MapReader{
    /**
     * 对应文件
     */
    protected File file;
    /**
     * 当前读取下标
     */
    protected int readingNoteIndex;
    /**
     * 时间点信息
     */
    protected ArrayList<Timing> timings;
    /**
     * 物件信息
     */
    protected ArrayList<Note> notes;
    protected IBaseData mapData;
    /**
     * 参考基准bpm
     */
    protected double preferenceBpm;

    /**
     * 重置note读取位置
     */
    public void resetNote(){
        readingNoteIndex = 0;
    };

    /**
     * 指定从哪里开始读取note
     * @param notePosition 位置
     */
    public void positionNote(int notePosition){
        if (notePosition < notes.size())
            readingNoteIndex = notePosition;
        else throw new RuntimeException("位置超出上限");
    };

    /**
     * 跳过指定数量note
     * @param noteIndex 数量
     */
    public void skipNote(int noteIndex){
        int pos = readingNoteIndex+ noteIndex;
        if (pos < notes.size())
            readingNoteIndex = pos;
        else throw new RuntimeException("位置超出上限");
    };
    public void close(){
        timings.clear();
        notes.clear();
    };
    /**
     * 读取文件
     * @param file 文件
     */
    public abstract  <T> T readFile(File file) throws IOException;

    /**
     * 读取为zxMap
     */
    public ZXMap readMap()throws IOException {
        if (file == null) {
            throw new IOException("请先设置读取文件");
        }
        return new ZXMap();
    };
    public Note readNote() {
        if (readingNoteIndex < notes.size())
            return notes.get(readingNoteIndex++);
        else return null;
    };
    public abstract <T>T readMeta()throws IOException;
    public abstract String getSuffix();
    public File getPath(){return file;};
    protected void ready(){
        notes = new ArrayList<>();
        timings = new ArrayList<>();
    };

}
