package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfo.OsuInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public interface Writer {
    void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException;
    String getDefaultName();

}
