package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.info.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.info.map.ZXMInfo;

import java.io.IOException;
import java.nio.file.Path;

/**
 * mc读取器(malody)
 * @author xiang2333
 */
public class McReader implements MapReader{
    private UnLocalizedMapInfo unLocalizedMapInfo;
    private final Path readPath;
    public McReader (Path path){
        readPath=path;
    }
    @Override
    public String getSupportFileExtension() {
        return "mc";
    }

    @Override
    public Path getReadPath() {
        return readPath;
    }

    @Override
    public ZXMap read() throws IOException {
        return null;
    }

    @Override
    public void completeInfo() {
        for (ZXMInfo info:ZXMInfo.values()) {
            if (!unLocalizedMapInfo.allInfo.containsKey(info)){
                unLocalizedMapInfo.allInfo.put(info, info.getDefaultValue());
            }
        }
    }
}
