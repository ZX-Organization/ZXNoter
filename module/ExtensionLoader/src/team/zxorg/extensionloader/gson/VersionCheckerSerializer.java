package team.zxorg.extensionloader.gson;

import com.google.gson.*;
import team.zxorg.extensionloader.core.VersionChecker;

import java.lang.reflect.Type;

public class VersionCheckerSerializer implements JsonSerializer<VersionChecker>, JsonDeserializer<VersionChecker> {
    @Override
    public VersionChecker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
            return null;
        }
        String str = json.getAsString();
        return new VersionChecker(str);
    }

    @Override
    public JsonElement serialize(VersionChecker src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
