package team.zxorg.extension;

import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

/**
 * 扩展管理器
 */
public class ExtensionManager {
    public static final ZXVersion EXTENSION_API_VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);
    private HashMap<String, Extension> extensions = new HashMap<>();

    /**
     * 获取扩展
     *
     * @param id 扩展id
     * @return 扩展对象
     */
    public Extension getExtension(String id) {
        return extensions.get(id);
    }

    /**
     * 获取已有的扩展表
     *
     * @return 所有扩展id
     */
    public Collection<String> getExtensionIdList() {
        return extensions.keySet();
    }

    /**
     * 载入扩展
     *
     * @param extensionPath 扩展jar路径
     */
    private void loadExtension(Path extensionPath) {
        Extension extension = new Extension(this, extensionPath);
        if (!extension.getApiVersion().isSupported(EXTENSION_API_VERSION)) {
            ZXLogger.warning("无法加载扩展 (" + extension.getId() + ")，因为所需的扩展 API 版本 " + extension.getApiVersion() + " 与当前版本 " + EXTENSION_API_VERSION + " 不兼容。");
            return;
        }
        if (extensions.containsKey(extension.getId())) {
            ZXLogger.warning("无法加载扩展 (" + extension.getId() + ")，因为其与已安装的扩展冲突，此扩展路径为 " + extensionPath + "。");
            return;
        }
        extensions.put(extension.getId(), extension);
    }

    /**
     * 初始化所有扩展
     */
    private void initializeAllExtension() {
        for (Extension extension : extensions.values()) {
            extension.initialize();
        }
    }

    /**
     * 载入所有扩展
     *
     * @param extensionsPath 扩展目录
     */
    public void loadExtensions(Path extensionsPath) {
        try {
            Files.list(extensionsPath).forEach(this::loadExtension);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        URL[] jarFiles = new URL[extensions.size()];
        int index = 0;
        for (Extension extension : extensions.values()) {
            jarFiles[index++] = (extension.jarUrl);
        }

        URLClassLoader classLoader = new URLClassLoader(jarFiles);

        for (Extension extension : extensions.values()) {
            extension.loadJar(classLoader);
        }
        initializeAllExtension();
    }


}
