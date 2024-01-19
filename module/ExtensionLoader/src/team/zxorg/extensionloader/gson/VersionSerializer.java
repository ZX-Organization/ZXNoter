package team.zxorg.extensionloader.gson;

import com.google.gson.*;
import team.zxorg.extensionloader.core.ZXVersion;

import java.lang.reflect.Type;

public class VersionSerializer implements JsonSerializer<ZXVersion>, JsonDeserializer<ZXVersion> {
    @Override
    public JsonElement serialize(ZXVersion src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public ZXVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
           return null;
        }
        String versionString = json.getAsString();
        return new ZXVersion(versionString);
    }
}
