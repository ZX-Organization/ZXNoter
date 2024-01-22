package team.zxorg.extensionloader.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class PathSerializer implements JsonSerializer<Path>, JsonDeserializer<Path> {
    @Override
    public Path deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
            return null;
        }
        String str = json.getAsString();
        return Path.of(str);
    }

    @Override
    public JsonElement serialize(Path src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
