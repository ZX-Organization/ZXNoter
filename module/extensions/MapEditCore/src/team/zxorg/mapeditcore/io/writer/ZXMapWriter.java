package team.zxorg.mapeditcore.io.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.map.mapdata.MapDataSerializer;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.MapElementSerializer;

import java.io.*;

public class ZXMapWriter extends MapWriter{
    public static Gson ioGson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(IBaseData.class, new MapDataSerializer()).registerTypeAdapter(IMapElement.class, new MapElementSerializer()).create();


    public ZXMapWriter(ZXMap map) {
        super(map);
    }

    protected String toJson(ZXMap map){
        return ioGson.toJson(map);
    }
    @Override
    public void writeFile(File file) throws IOException {
        super.writeFile(file);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(toJson(map));
        writer.flush();
        writer.close();
    }

    @Override
    public String getSuffix() {
        return ".zx";
    }


}
