package team.zxorg.extensionloader.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import team.zxorg.extensionloader.core.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * 支持更多类型的Gson管理类
 */
public class GsonManager {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Version.class, new VersionSerializer())
                .setPrettyPrinting()
                .create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(Reader reader, Class<T> classOfT) {
        return gson.fromJson(reader, classOfT);
    }

    public static <T> T fromJson(InputStream is, Class<T> classOfT) {
        return GsonManager.fromJson(new InputStreamReader(is), classOfT);
    }

    public static <T> T fromJson(ClassLoader classLoader, String resourceName, Class<T> classOfT) {
        try (InputStream is = classLoader.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourceName);
            }
            return GsonManager.fromJson(new InputStreamReader(is), classOfT);
        } catch (IOException e) {
            throw new RuntimeException("Resource read failed: " + resourceName + " " + e);
        }
    }

    public static <T> T fromJson(String string, Class<T> classOfT) {
        return gson.fromJson(string, classOfT);
    }
    public static <T> T fromJson(String string, Type type) {
        return gson.fromJson(string, type);
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