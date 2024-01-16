package team.zxorg.extension;

import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 扩展管理器
 */
public class ExtensionManager {
    public static final ZXVersion EXTENSION_API_VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);
    HashMap<String, Extension> extensions = new HashMap<>();

    /**
     * 载入扩展
     *
     * @param extensionPath 扩展jar路径
     */
    private void loadExtension(Path extensionPath) {
        Extension extension = new Extension(extensionPath);
        if (!extension.getApiVersion().isSupported(EXTENSION_API_VERSION)) {
            ZXLogger.warning("扩展 " + extension + " 需要的扩展Api版本不受支持，无法加载");
            return;
        }
        if (extensions.containsKey(extension.getId())) {
            ZXLogger.warning("扩展 " + extension + " 重复载入，无法加载");
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
     * @param extensionsPath 扩展目录
     */
    public void loadExtensions(Path extensionsPath) {
        try {
            Files.list(extensionsPath).forEach(this::loadExtension);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializeAllExtension();
    }


}
