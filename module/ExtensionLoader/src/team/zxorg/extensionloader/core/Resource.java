package team.zxorg.extensionloader.core;

import org.apache.commons.lang3.time.StopWatch;
import team.zxorg.extensionloader.event.ResourceEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Resource {
    private static final Path resourcesPath = Path.of("./resourcepacks");

    private static final ResourceConfig config = Configuration.config.get(ResourceConfig.class);
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
        Logger.info(Language.get(LanguageKey.MESSAGE_RESOURCE_PACK_INITIALIZE));
    }

    /**
     * 重载所有资源包
     */
    public static void reloadResourcePacks() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //释放之前的资源包
        for (ResourcePack pack : loadedResourcePacks.values()) {
            pack.close();
        }

        //读取全部资源包
        loadedResourcePacks.clear();
        try (Stream<Path> fileStream = Files.list(resourcesPath)) {
            //重新读取资源包
            for (Path packPath : fileStream.toList()) {
                Logger.info(Language.get(LanguageKey.MESSAGE_RESOURCE_PACK_LOADING, packPath));
                ResourcePack pack;
                try {
                    pack = new ResourcePack(packPath);
                } catch (RuntimeException e) {
                    Logger.warning(e.getMessage());
                    continue;
                }
                loadedResourcePacks.put(resourcesPath.relativize(packPath), pack);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //重新加载启用资源包
        enableResourcePacks.clear();
        //重新读取资源包
        for (String name : config.resources) {
            ResourcePack resourcePack = loadedResourcePacks.get(Paths.get(name));
            if (resourcePack == null) {
                Logger.info(Language.get(LanguageKey.MESSAGE_RESOURCE_PACK_LOST, name));
                continue;
            }
            enableResourcePacks.add(resourcePack);
        }


        HashMap<Path, Path> newResources = new HashMap<>();
        //为资源重新索引
        resources.clear();
        for (ResourcePack pack : enableResourcePacks) {
            Logger.info(Language.get(LanguageKey.MESSAGE_RESOURCE_PACK_APPLICATION, pack.getName()));
            HashMap<Path, Path> indexed = application(pack.getPath());
            newResources.putAll(indexed);
        }

        resources.putAll(newResources);


        for (ResourceEventListener listener : eventListeners)
            listener.onReload();

        stopWatch.stop();
        Logger.info(Language.get(LanguageKey.MESSAGE_RESOURCE_PACK_RELOADED, stopWatch.getTime()));

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

    public static String getResourceToString(Path path) {
        try (InputStream in = getResourceToInputStream(path)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourceToString(String path) {
        return getResourceToString(Paths.get(path));
    }

    public static InputStream getResourceToInputStream(Path path) {
        try {
            return Files.newInputStream(getResourceToPath(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getResourceToInputStream(String path) {
        return getResourceToInputStream(Path.of(path));
    }

    public static Path getResourceToPath(String path) {
        return getResourceToPath(Path.of(path));
    }

    public static Path getResourceToPath(Path path) {
        Path file = resources.get(path);
        if (file == null)
            throw new RuntimeException(Language.get(LanguageKey.MESSAGE_RESOURCE_NOT_FOUND, path));
        return file;
    }

    public static URL getResourceToUrl(String path) {
        return getResourceToUrl(Path.of(path));
    }

    public static URL getResourceToUrl(Path path) {
        try {
            return getResourceToPath(path).toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> listResourceFiles(String findPath) {
        return listResourceFiles(Path.of(findPath));
    }

    /**
     * 获取全局资源中指定路径下的所有子文件
     *
     * @param findPath 要寻找的目录
     * @return 目录下的子文件路径列表
     */
    public static List<Path> listResourceFiles(Path findPath) {
        List<Path> childFiles = new ArrayList<>();
        for (Map.Entry<Path, Path> entry : resources.entrySet()) {
            Path virtualPath = entry.getKey();
            if (virtualPath.startsWith(findPath) && !virtualPath.equals(findPath)) {
                childFiles.add(virtualPath);
            }
        }
        return childFiles;
    }
    /**
     * 资源配置类
     */
    public static class ResourceConfig extends ConfigData {
        LinkedHashSet<String> resources;
    }

}
