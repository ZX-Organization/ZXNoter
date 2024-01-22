package team.zxorg.extensionloader.gson;

import com.google.gson.*;
import team.zxorg.extensionloader.core.Language;

import java.lang.reflect.Type;

public class LanguageSerializer implements JsonSerializer<Language>, JsonDeserializer<Language> {
    @Override
    public Language deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
            return null;
        }
        String str = json.getAsString();
        return new Language(str);
    }

    @Override
    public JsonElement serialize(Language src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());

    }
}
