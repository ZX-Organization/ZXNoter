package team.zxorg.extensionloader.extension;

import team.zxorg.extensionloader.core.ZXLanguage;
import team.zxorg.extensionloader.core.ZXLogger;
import team.zxorg.extensionloader.core.ZXVersion;
import team.zxorg.extensionloader.gson.ZXGson;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扩展对象
 */
public class Extension {
    /**
     * 获取扩展id
     *
     * @return id
     */
    public String getId() {
        return info.id;
    }

    /**
     * 获取扩展的版本信息
     *
     * @return 版本
     */
    public ZXVersion getVersion() {
        return info.version;
    }

    /**
     * 获取依赖的扩展api版本
     *
     * @return 版本
     */
    public ZXVersion getApiVersion() {
        return info.depends.extensionApi;
    }

    @Override
    public String toString() {
        return "(" + info.id + " " + info.version + ")";
    }

    /**
     * 扩展配置信息
     */
    private final ExtensionInfo info;
    /**
     * 扩展入口点列表
     */
    List<ExtensionEntrypoint> entrypointList;
    /**
     * 扩展管理器
     */
    ExtensionManager manager;
    /**
     * 扩展jar文件路径
     */
    URL jarUrl;
    /**
     * 扩展jar文件路径
     */
    Path jarPath;
    /**
     * 扩展类加载器
     */
    URLClassLoader classLoader;

    public Extension(ExtensionManager manager, Path jarPath) {
        this.jarPath = jarPath;
        this.manager = manager;
        //ZXLogger.info("正在读取扩展: " + jarPath);
        try {
            jarUrl = jarPath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(getLanguage(MESSAGE_ERROR + "url", jarPath, e));
        }
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl})) {
            info = ZXGson.fromJson(classLoader, "extension.json", ExtensionInfo.class);

            if (info.languages != null)
                for (String language : info.languages)
                    ZXLanguage.loadLanguage(classLoader, language);
        } catch (Exception e) {
            throw new RuntimeException(getLanguage(MESSAGE_ERROR + "infoReadFailed", jarPath, e));
        }
    }


    /**
     * 载入扩展
     *
     * @param classLoader 类加载器
     */
    protected void loadJar(URLClassLoader classLoader) {
        this.classLoader = classLoader;
        entrypointList = new ArrayList<>();
        for (String entrypoint : info.entrypoints) {
            // 加载类
            Class<?> loadedClass;
            try {
                loadedClass = classLoader.loadClass(entrypoint);
            } catch (ClassNotFoundException e) {
                ZXLogger.warning(getLanguage(MESSAGE_ERROR + "entrypointNotFound", getId(), entrypoint));
                continue;
            }

            // 实例化程序入口
            try {
                if (loadedClass.getDeclaredConstructor().newInstance() instanceof ExtensionEntrypoint ei) {
                    this.entrypointList.add(ei);
                } else {
                    ZXLogger.warning(getLanguage(MESSAGE_ERROR + "entrypointNotImplemented", getId(), entrypoint));
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                ZXLogger.warning(getLanguage(MESSAGE_ERROR + "entrypointInstanceFailed", getId(), entrypoint, e));
            }
        }
    }

    /**
     * 初始化扩展
     */
    protected void initialize() {
        //检查依赖扩展
        for (Map.Entry<String, ZXVersion> dependExtension : info.depends.extensions.entrySet()) {
            String dependExtensionId = dependExtension.getKey();
            Extension dependExt = manager.getExtension(dependExtensionId);
            if (dependExt == null) {
                ZXLogger.warning(getLanguage(MESSAGE_ERROR + "message.extension.error.dependExtensionLost", getId(), dependExtensionId, dependExtension.getValue()));
                return;
            }
            if (!dependExtension.getValue().isSupported(dependExt.getVersion())) {
                ZXLogger.warning(getLanguage(MESSAGE_ERROR + "message.extension.error.dependExtensionNotCompatible", getId(), dependExtensionId, dependExtension.getValue(), dependExt.getVersion()));
                return;
            }
        }
        // 初始化扩展
        for (ExtensionEntrypoint initializer : entrypointList) {
            initializer.onInitialize(this, manager);
        }
    }


    /**
     * 获取全局资源
     *
     * @param name 资源路径
     * @return 资源数据
     */
    public InputStream getGlobalResourceAsStream(String name) {
        return classLoader.getResourceAsStream(name);
    }

    /**
     * 获取assets下的资源
     *
     * @param name 资源路径 'assets.id.name'
     * @return 资源数据
     */
    public InputStream getResourceAsStream(String name) {
        return classLoader.getResourceAsStream("assets." + info.id + "." + name);
    }


    public String getLanguage(String key, Object... args) {
        return ZXLanguage.get(key, args);
    }

    public static final String MESSAGE_ERROR = "message.extension.error.";
}
