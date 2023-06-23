package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.mapInfo.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;

/**
 * mc读取器(malody)
 */
public class McReader implements MapReader{
    @Override
    public String getSupportFileExtension() {
        return null;
    }

    @Override
    public Path getReadPath() {
        return null;
    }

    @Override
    public ZXMap read(Path path) throws IOException {
        return null;
    }

    @Override
    public UnLocalizedMapInfo completeInfo() {
        return null;
    }
}
