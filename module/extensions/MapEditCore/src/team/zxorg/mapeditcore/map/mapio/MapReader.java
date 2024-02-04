package team.zxorg.mapeditcore.map.mapio;

import team.zxorg.mapeditcore.map.MapMetaData;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.note.Note;

import java.nio.file.Path;

public abstract class MapReader{
    protected final Path filePath;
    protected boolean isReadingNote;
    protected int readNoteIndex;

    protected MapReader(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * 读取为zxMap
     */
    protected ZXMap readMap(){return new ZXMap();};
    public abstract Note readNote();
    public abstract MapMetaData readMeta();
    public abstract String getSuffix();
    public Path getPath(){return filePath;};
    protected abstract void ready();

}
