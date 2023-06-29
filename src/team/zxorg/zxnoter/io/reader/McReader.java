package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.mapInfo.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;

import java.io.IOException;
import java.nio.file.Path;

/**
 * mc读取器(malody)
 */
public class McReader implements MapReader{
    UnLocalizedMapInfo unLocalizedMapInfo;
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
    public void completeInfo() {
        for (ZXMInfo info:ZXMInfo.values())
            if (!unLocalizedMapInfo.allInfo.containsKey(info)){
                unLocalizedMapInfo.allInfo.put(info, info.getDefaultValue());
            }
    }
}
