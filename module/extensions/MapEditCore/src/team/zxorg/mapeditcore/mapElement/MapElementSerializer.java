package team.zxorg.mapeditcore.mapElement;

import com.google.gson.*;
import org.ietf.jgss.GSSName;
import team.zxorg.mapeditcore.mapElement.note.*;

import java.lang.reflect.Type;

public class MapElementSerializer implements JsonDeserializer<IMapElement> {
    public static Gson eleGson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(IMapElement.class, new MapElementSerializer()).create();

    @Override
    public IMapElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return eleGson.fromJson(json,
                switch (json.getAsJsonObject().get("type").getAsString()) {
                    case "Note"-> Note.class;
                    case "Hold"-> Hold.class;
                    case "Flick"-> Flick.class;
                    case "MixNote"-> MixNote.class;
                    case "OsuNote"-> OsuNote.class;
                    case "OsuHold"-> OsuHold.class;
                    default ->
                            throw new IllegalStateException("Unexpected value: " + json.getAsJsonObject().get("type").getAsString());
                }
        );
    }

}
