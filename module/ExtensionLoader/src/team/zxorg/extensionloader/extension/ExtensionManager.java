package team.zxorg.extensionloader.extension;

import org.apache.commons.lang3.time.StopWatch;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Version;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 扩展管理器
 */
public class ExtensionManager {
    public static final Version EXTENSION_API_VERSION = new Version(0, 0, 0, Version.ReleaseStatus.ALPHA);
    private final HashMap<String, Extension> extensionMap = new HashMap<>();

    /**
     * 获取扩展
     *
     * @param id 扩展id
     * @return 扩展对象
     */
    public Extension getExtension(String id) {
        return extensionMap.get(id);
    }

    /**
     * 获取所有扩展
     *
     * @return 所有扩展对象
     */
    public Collection<Extension> getExtensions() {
        return extensionMap.values();
    }

    /**
     * 获取已有的扩展表
     *
     * @return 所有扩展id
     */
    public Collection<String> getExtensionIdList() {
        return extensionMap.keySet();
    }

    /**
     * 载入扩展
     *
     * @param extensionPath 扩展jar路径
     */
    private void loadExtension(Path extensionPath) {
        Extension extension = new Extension(this, extensionPath);
        if (!extension.getApiVersion().isSupported(EXTENSION_API_VERSION)) {
            Logger.warning(Language.get(LanguageKey.MESSAGE_EXTENSION_ERROR_DEPEND_API_NOT_COMPATIBLE, extension.getId(), extension.getApiVersion(), EXTENSION_API_VERSION));
            return;
        }

        if (!extension.isPlatformSupported()) {
            Logger.warning(Language.get(LanguageKey.MESSAGE_EXTENSION_ERROR_PLATFORM_NOT_SUPPORTED, extension.getId(), extension.getApiVersion(), EXTENSION_API_VERSION));
            return;
        }

        if (extensionMap.containsKey(extension.getId())) {
            Logger.warning(Language.get(LanguageKey.MESSAGE_EXTENSION_ERROR_ID_CONFLICT, extension.getId(), extensionPath.toAbsolutePath(), extensionMap.get(extension.getId()).getJarPath().toAbsolutePath()));
            return;
        }

        extensionMap.put(extension.getId(), extension);
    }


    /**
     * 载入所有扩展
     *
     * @param extensionsPath 扩展目录
     */
    protected void loadAllExtensions( ClassLoader parent,Path extensionsPath) {

        Logger.info(Language.get(LanguageKey.MESSAGE_EXTENSION_LOADING));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //读取所有扩展
        try (Stream<Path> extensionPath = Files.list(extensionsPath)) {
            for (Path path : extensionPath.toList()) {
                loadExtension(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //依赖检查
        Iterator<Map.Entry<String, Extension>> iterator = extensionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Extension> entry = iterator.next();
            Extension extension = entry.getValue();
            if (!extension.dependencyCheck()) {
                iterator.remove();
            }
        }

        //载入所有扩展
        URL[] jarFiles = new URL[extensionMap.size()];
        int index = 0;
        for (Extension extension : extensionMap.values()) {
            jarFiles[index++] = (extension.getJarUrl());
        }

        URLClassLoader classLoader = new URLClassLoader("ExtensionClassLoader", jarFiles, parent);

        // 载入扩展
        for (Extension extension : extensionMap.values()) {
            extension.load(classLoader);
        }

        // 初始化扩展
        for (Extension extension : extensionMap.values()) {
            extension.initialize();
        }

        // 扩展全部初始化完毕
        for (Extension extension : extensionMap.values()) {
            extension.allInitialized();
        }

        stopWatch.stop();
        Logger.info(Language.get(LanguageKey.MESSAGE_EXTENSION_LOADED, extensionMap.size(), stopWatch.getTime()));

    }
}
