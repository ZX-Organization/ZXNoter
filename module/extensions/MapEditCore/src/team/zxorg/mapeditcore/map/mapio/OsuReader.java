package team.zxorg.mapeditcore.map.mapio;

import team.zxorg.mapeditcore.map.MapMetaData;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.note.Note;

import java.nio.file.Path;

public class OsuReader extends MapReader{

    public OsuReader(Path filePath) {
        super(filePath);
    }

    @Override
    public ZXMap readMap() {
        return null;
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
