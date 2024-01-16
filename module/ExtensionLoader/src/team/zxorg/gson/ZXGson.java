package team.zxorg.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.core.ZXVersion;

import java.io.Reader;

public class ZXGson {
    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(ZXVersion.class, new VersionSerializer())
                .setPrettyPrinting()
                .create();
    }
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    public static <T> T fromJson(Reader reader, Class<T> classOfT) {
        return gson.fromJson(reader, classOfT);
    }

    public static <T> T fromJson(String string, Class<T> classOfT) {
        return gson.fromJson(string, classOfT);
    }
}
