package team.zxorg.mapeditcore.mapElement;

import com.google.gson.*;
import team.zxorg.mapeditcore.mapElement.note.*;
import team.zxorg.mapeditcore.mapElement.timing.OsuTiming;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.lang.reflect.Type;

import static team.zxorg.mapeditcore.io.writer.ZXMapWriter.ioGson;


public class MapElementSerializer implements JsonDeserializer<IMapElement> {

    @Override
    public IMapElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ioGson.fromJson(json,
                switch (json.getAsJsonObject().get("type").getAsString()) {
                    case "Note"-> Note.class;
                    case "Hold"-> Hold.class;
                    case "Flick"-> Flick.class;
                    case "MixNote"-> MixNote.class;
                    case "OsuNote"-> OsuNote.class;
                    case "OsuHold"-> OsuHold.class;
                    case "Timing"-> Timing.class;
                    case "OsuTiming"-> OsuTiming.class;
                    default ->
                            throw new IllegalStateException("Unexpected value: " + json.getAsJsonObject().get("type").getAsString());
                }
        );
    }

}
