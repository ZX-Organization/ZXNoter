package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

public interface Writer {
    void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException;
    String getDefaultName();

}
