package team.zxorg.mapeditcore.map.mapdata;

import com.google.gson.*;
import team.zxorg.mapeditcore.map.mapdata.datas.ImdMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.OsuMapData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.MapElementSerializer;
import team.zxorg.mapeditcore.mapElement.note.*;
import team.zxorg.mapeditcore.mapElement.timing.OsuTiming;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.lang.reflect.Type;

public class MapDataSerializer implements JsonDeserializer<IBaseData> {
    public static Gson mapDataGson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(IBaseData.class, new MapDataSerializer()).create();

    @Override
    public IBaseData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return mapDataGson.fromJson(json,
                switch (json.getAsJsonObject().get("type").getAsString()) {
                    case "ZXMapData"-> ZXMapData.class;
                    case "ImdMapData"-> ImdMapData.class;
                    case "OsuMapData"-> OsuMapData.class;
                    default ->
                            throw new IllegalStateException("Unexpected value: " + json.getAsJsonObject().get("type").getAsString());
                }
        );
    }
}
