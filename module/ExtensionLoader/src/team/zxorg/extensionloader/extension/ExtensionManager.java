package team.zxorg.extensionloader.extension;

import team.zxorg.extensionloader.core.ZXLanguage;
import team.zxorg.extensionloader.core.ZXLogger;
import team.zxorg.extensionloader.core.ZXVersion;

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
    private final HashMap<String, Extension> extensions = new HashMap<>();

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
            ZXLogger.warning(ZXLanguage.get(Extension.MESSAGE_ERROR + "dependApiNotCompatible", extension.getId(), extension.getApiVersion(), EXTENSION_API_VERSION));
            return;
        }

        if (extensions.containsKey(extension.getId())) {
            ZXLogger.warning(ZXLanguage.get(Extension.MESSAGE_ERROR + "idConflict", extension.getId(), extensionPath.toAbsolutePath(), extensions.get(extension.getId()).jarPath.toAbsolutePath()));
            return;
        }
        extensions.put(extension.getId(), extension);
    }

    /**
     * 初始化所有扩展
     */
    public void initializeAllExtensions() {
        for (Extension extension : extensions.values()) {
            extension.initialize();
        }
    }

    /**
     * 载入所有扩展
     *
     * @param extensionsPath 扩展目录
     */
    public void loadAllExtensions(Path extensionsPath) {
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
    }
}
