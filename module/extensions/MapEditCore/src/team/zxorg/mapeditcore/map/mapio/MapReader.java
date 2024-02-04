package team.zxorg.mapeditcore.map.mapio;

import team.zxorg.mapeditcore.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

public interface MapReader {
    ZXMap read(Path path) throws IOException;
    String getSuffix();

}
