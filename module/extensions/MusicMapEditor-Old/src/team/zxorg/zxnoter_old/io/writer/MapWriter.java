package team.zxorg.zxnoter_old.io.writer;

import team.zxorg.zxnoter_old.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

public interface MapWriter {
    void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException;
    String getDefaultName();

}
