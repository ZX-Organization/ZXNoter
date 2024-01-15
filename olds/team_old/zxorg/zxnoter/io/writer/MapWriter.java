package team.zxorg.zxnoter.io.writer;

import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author xiang2333
 */
public interface MapWriter {
    void writeOut(ZXMap zxMap, Path path) throws NoSuchFieldException, IOException;
    String getDefaultName();

}
