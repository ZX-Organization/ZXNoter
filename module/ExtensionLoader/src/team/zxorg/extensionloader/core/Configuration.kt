package team.zxorg.extensionloader.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import team.zxorg.extensionloader.gson.GsonManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration<T> {
    private final T configObject;
    private final Path configPath;


    public Configuration(String id) {

        String json;
        configPath = Path.of("config", id + ".json");
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }
            if (Files.exists(configPath)) json = Files.readString(configPath);
            else json = "{}";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取泛型类型
        TypeToken type = new TypeToken<T>() {
        };

        System.out.println(type.getRawType());
        System.out.println(type.getType());

        T[] values = (T[]) new T[2];

        System.out.println(values[0].getClass());

        configObject = (T) new Gson().fromJson(json, type);

        if (configObject instanceof T a) {
            System.out.println("AAAAAAA" + a.getClass());
        }
        System.out.println(configObject.getClass());
    }

    public T getConfig() {
        return configObject;
    }

    public void save() {
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }

            Files.writeString(configPath, GsonManager.toJson(configObject));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
