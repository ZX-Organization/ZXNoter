package team.zxorg.mapeditcore.io.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.map.mapdata.MapDataSerializer;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.MapElementSerializer;

import java.io.*;

public abstract class MapWriter {
    protected File file;
    protected ZXMap map;

    public MapWriter(ZXMap map) {
        this.map = map;
    }

    public void writeFile(File file) throws IOException {
        this.file = file;
    }

    public abstract String getSuffix();
    public File getPath(){return file;};

}
