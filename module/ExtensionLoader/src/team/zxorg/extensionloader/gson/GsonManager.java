package team.zxorg.extensionloader.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Version;
import team.zxorg.extensionloader.core.VersionChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 支持更多类型的Gson管理类
 */
public class GsonManager {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Version.class, new VersionSerializer())
                .registerTypeAdapter(VersionChecker.class, new VersionCheckerSerializer())
                .registerTypeAdapter(Path.class, new PathSerializer())
                .registerTypeAdapter(Language.class, new LanguageSerializer())
                .setPrettyPrinting()
                .create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(Reader reader, Class<T> classOfT) {
        return checkNullValue(gson.fromJson(reader, classOfT));
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) {
        return checkNullValue(gson.fromJson(reader, typeOfT));
    }

    public static <T> T fromJson(JsonElement element, Class<T> classOfT) {
        return checkNullValue(gson.fromJson(element, classOfT));
    }

    public static <T> T fromJson(Path path, Class<T> classOfT) {
        try {
            return fromJson(new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8), classOfT);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJson(InputStream is, Class<T> classOfT) {
        return fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), classOfT);
    }

    public static <T> T fromJson(ClassLoader classLoader, String resourceName, Class<T> classOfT) {
        try (InputStream is = classLoader.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourceName);
            }
            return fromJson(new InputStreamReader(is), classOfT);
        } catch (IOException e) {
            throw new RuntimeException("Resource read failed: " + resourceName + " " + e);
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return checkNullValue(gson.fromJson(json, classOfT));
    }

    public static <T> T fromJson(String json, Type type) {
        return checkNullValue(gson.fromJson(json, type));
    }

    /**
     * 检查对象空值
     *
     * @param obj 对象
     */
    public static <T> T checkNullValue(T obj) {
        if (obj instanceof NullValueHandler nullValueHandler)
            nullValueHandler.handleNullValues();
        else {
            try {
                // 获取对象的所有字段
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    // 设置字段可访问
                    field.setAccessible(true);
                    // 根据类型进行赋值


                    if (field.get(obj) == null) {
                        Class<?> fieldType = field.getType();
                        if (fieldType == String.class) {
                            field.set(obj, Language.get("common.notSpecified"));
                        } else if (fieldType == Integer.class || fieldType == int.class) {
                            field.set(obj, 0);
                        } else if (fieldType == Long.class || fieldType == long.class) {
                            field.set(obj, 0L);
                        } else if (fieldType == List.class) {
                            field.set(obj, new ArrayList<>());
                        } else if (fieldType == Map.class) {
                            field.set(obj, new HashMap<>());
                        } else if (fieldType == Set.class) {
                            field.set(obj, new HashSet<>());
                        } else {
                            Object nestedObj = fieldType.getDeclaredConstructor().newInstance();
                            field.set(obj, checkNullValue(nestedObj));
                        }
                    }
                }

            } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }

    public static JsonElement parseJson(String json) {
        return JsonParser.parseString(json);
    }

    public static JsonElement parseJson(Path json) {
        try {
            return JsonParser.parseReader(new InputStreamReader(Files.newInputStream(json)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonElement parseJson(ClassLoader classLoader, String resourceName) {
        try (InputStream is = classLoader.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourceName);
            }
            return JsonParser.parseReader(new InputStreamReader(is));
        } catch (IOException e) {
            throw new RuntimeException("Resource read failed: " + resourceName + " " + e);
        }
    }
}
