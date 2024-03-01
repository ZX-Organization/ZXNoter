package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.extensionloader.gson.GsonManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private static final List<Configuration> allConfigs = new ArrayList<>();
    protected static final Configuration config = new Configuration("extensionLoader");
    private final Map<Class<?>, Object> configObject = new HashMap<>();
    private final Map<Class<?>, List<ConfigEventListener>> eventListeners = new HashMap<>();
    private final Path configPath;


    public Configuration(String id) {
        configPath = Path.of("config", id);
        allConfigs.add(this);
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
        @SuppressWarnings("unchecked")
        T result = (T) configObject.get(clazz);

        if (result == null) {
            Path jsonPath = getConfigPath(clazz);
            boolean isNewConfig = false;
            if (Files.exists(jsonPath)) {
                //System.out.println("读取配置: " + jsonPath);
                result = GsonManager.fromJson(jsonPath, clazz);
            } else {
                isNewConfig = true;
                //System.out.println("配置不存在，创建新配置: " + getConfigPath(clazz));
                try {
                    result = clazz.getDeclaredConstructor().newInstance();
                    GsonManager.checkNullValue(result);
                } catch (Exception e) {
                    throw new RuntimeException("Error creating instance of class: " + clazz.getName(), e);
                }
            }
            configObject.put(clazz, result);
            if (isNewConfig)
                save(clazz);
            //检查特定字段 并赋值
            if (result instanceof ConfigData configData) {
                try {
                    Field field = ConfigData.class.getDeclaredField("config");
                    if (field.get(result) == null) {
                        field.setAccessible(true);
                        field.set(result, this);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                }
                configData.loaded();
            }
        }

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

    /**
     * 保存所有需要保存的配置
     */
    public static void save() {
        for (Configuration config : allConfigs) {
            for (Object object : config.configObject.values()) {
                if (object instanceof ConfigData configData) {
                    if (configData.needSave) {
                        //System.out.println("自动保存配置: " + configData.getClass().getSimpleName());
                        configData.save();
                    }
                }
            }
        }
    }

}
