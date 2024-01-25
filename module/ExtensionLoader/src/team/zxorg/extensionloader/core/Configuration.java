package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.extensionloader.gson.GsonManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    protected static final Configuration config = new Configuration("extensionLoader");
    private final Map<Class<?>, Object> configObject = new HashMap<>();
    private final Map<Class<?>, List<ConfigEventListener>> eventListeners = new HashMap<>();
    private final Path configPath;

    public Configuration(String id) {
        configPath = Path.of("config", id);
    }

    private Path getConfigPath(Class<?> clazz) {
        return configPath.resolve(clazz.getSimpleName() + ".json");
    }

    /**
     * 获取配置
     *
     * @param clazz 配置类
     * @return 配置对象
     */
    public <T> T get(Class<T> clazz) {
        Object value = configObject.computeIfAbsent(clazz, k -> {
            Path jsonPath = getConfigPath(clazz);
            if (Files.exists(jsonPath))
                return GsonManager.fromJson(jsonPath, clazz);
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Error creating instance of class: " + clazz.getName(), ex);
            }
        });

        @SuppressWarnings("unchecked")
        T result = (T) value;
        return result;
    }

    /**
     * 为指定配置类添加事件监听器
     *
     * @param clazz    配置类
     * @param listener 事件监听器
     */
    public void addEventListener(Class<?> clazz, ConfigEventListener listener) {
        eventListeners.computeIfAbsent(clazz, k -> new ArrayList<>()).add(listener);
    }

    /**
     * 为指定配置类移除事件监听器
     *
     * @param clazz    配置类
     * @param listener 事件监听器
     */
    public void removeEventListener(Class<?> clazz, ConfigEventListener listener) {
        eventListeners.computeIfAbsent(clazz, k -> new ArrayList<>()).remove(listener);
    }

    /**
     * 保存配置
     */
    public void save(Class<?> clazz) {
        try {
            Files.createDirectories(configPath);
            Files.write(getConfigPath(clazz), GsonManager.toJson(configObject.get(clazz)).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ConfigEventListener listener : eventListeners.getOrDefault(clazz, List.of())) {
            listener.ConfigSaved();
        }
    }

    /**
     * 保存全部配置
     */
    public void saveAll() {
        for (Class<?> clazz : configObject.keySet()) {
            save(clazz);
        }
    }


}
