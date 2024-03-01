package team.zxorg.extensionloader.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ClassSerializer implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

    @Override
    public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive) || !((JsonPrimitive) json).isString()) {
            return null;
        }
        String className = json.getAsString();
        System.out.println(className);
        try {
            return ClassSerializer.class.getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
