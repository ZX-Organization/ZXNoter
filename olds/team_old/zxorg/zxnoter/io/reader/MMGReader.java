package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.info.UnLocalizedMapInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.map.ZXMap;

import java.io.IOException;
import java.nio.file.Path;


/**
 * @author xiang2333
 */
public class MMGReader implements MapReader {
    private final Path readPath;
    private UnLocalizedMapInfo unLocalizedMapInfo;

    public MMGReader(Path path) {
        this.readPath = path;
    }

    @Override
    public String getSupportFileExtension() {
        return "mmg";
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
