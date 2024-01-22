package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.event.ResourceEventListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Resource {
    private static final Path resourcesPath = Path.of("./resourcepacks");
    private static final Configuration config = new Configuration("resource");
    private static ResourceConfig resourceConfig;
    private static final HashMap<Path, Path> resources = new HashMap<>();
    /**
     * 启用的资源包列表
     */
    private static final ArrayList<ResourcePack> enableResourcePacks = new ArrayList<>();
    /**
     * 载入的资源包列表
     */
    private static final HashMap<Path, ResourcePack> loadedResourcePacks = new HashMap<>();

    private static final List<ResourceEventListener> eventListeners = new ArrayList<>();


    static {
        initialize();
    }

    /**
     * 初始化资源包
     */
    private static void initialize() {
        Logger.info(Language.get("message.resource.initialize"));
        resourceConfig = config.get(ResourceConfig.class);
        reloadResourcePacks();
    }

    /**
     * 重载所有资源包
     */
    private static void reloadResourcePacks() {
        //释放之前的资源包
        for (ResourcePack pack : loadedResourcePacks.values()) {
            pack.close();
        }

        //读取全部资源包
        loadedResourcePacks.clear();
        try {
            //重新读取资源包

            Files.list(resourcesPath).forEach(file -> {
                Logger.info(Language.get("message.resource.loading", file));
                ResourcePack pack;
                try {
                    pack = new ResourcePack(file);
                } catch (RuntimeException e) {
                    Logger.warning(e.getMessage());
                    return;
                }
                loadedResourcePacks.put(resourcesPath.relativize(file), pack);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //重新加载启用资源包
        enableResourcePacks.clear();
        //重新读取资源包
        for (String name : resourceConfig.resources) {
            ResourcePack resourcePack = loadedResourcePacks.get(Paths.get(name));
            if (resourcePack == null) {
                Logger.info(Language.get("message.resource.lost", name));
                continue;
            }
            enableResourcePacks.add(resourcePack);
        }


        HashMap<Path, Path> newResources = new HashMap<>();
        //为资源重新索引
        resources.clear();
        for (ResourcePack pack : enableResourcePacks) {
            Logger.info(Language.get("message.resource.application", pack.getName()));
            HashMap<Path, Path> indexed = application(pack.getPath());
            newResources.putAll(indexed);
        }


        for (ResourceEventListener listener : eventListeners)
            listener.onReload();
    }

    private static HashMap<Path, Path> application(Path root) {
        HashMap<Path, Path> result = new HashMap<>();
        try (Stream<Path> fileStream = Files.walk(root)) {
            fileStream.forEach(path -> result.put(root.relativize(path), path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 资源配置类
     */
    private static class ResourceConfig {
        List<String> resources;

        public ResourceConfig() {
            resources = new ArrayList<>();
        }
    }

    /**
     * 添加资源事件监听器
     *
     * @param listener 要添加的监听器
     */
    public static void addEventListener(ResourceEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * 移除资源事件监听器
     *
     * @param listener 被移除的监听器
     */
    public static void removeEventListener(ResourceEventListener listener) {
        eventListeners.remove(listener);
    }

}
