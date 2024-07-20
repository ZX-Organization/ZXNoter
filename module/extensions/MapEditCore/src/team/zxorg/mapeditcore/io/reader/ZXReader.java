package team.zxorg.mapeditcore.io.reader;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static team.zxorg.mapeditcore.io.writer.ZXMapWriter.ioGson;


public class ZXReader extends MapReader {
    ZXMap map;
    @Override
    public ZXReader readFile(File file) throws IOException {
        this.file = file;
        if (file.getName().endsWith(".zx")){
            throw new IOException("读取到非zx谱面文件");
        }
        map = ioGson.fromJson(new FileReader(file), ZXMap.class);
        return this;
    }

    @Override
    public ZXMap readMap() {
        return map;
    }

    @Override
    public IBaseData readMeta() {
        return map.metaData;
    }

    @Override
    public String getSuffix() {
        return ".zx";
    }
}
