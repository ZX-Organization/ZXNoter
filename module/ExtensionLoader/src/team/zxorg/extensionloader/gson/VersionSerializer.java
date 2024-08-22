package team.zxorg.extensionloader.gson;

import com.google.gson.*;
import team.zxorg.extensionloader.core.Version;

import java.lang.reflect.Type;

public class VersionSerializer implements JsonSerializer<Version>, JsonDeserializer<Version> {
    @Override
    public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
            return null;
        }
        String versionString = json.getAsString();
        return new Version(versionString);
    }

    @Override
    public JsonElement serialize(Version src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
