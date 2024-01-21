package team.zxorg.extensionloader.extension;

import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Version;
import team.zxorg.extensionloader.gson.GsonManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
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
    public Version getVersion() {
        return info.version;
    }

    /**
     * 获取依赖的扩展api版本
     *
     * @return 版本
     */
    public Version getApiVersion() {
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
            info = GsonManager.fromJson(classLoader, "extension.json5", ExtensionInfo.class);

            Language.setGlobalLanguage("extension." + info.id + ".version", info.version.toString());

            if (info.author != null) {
                StringBuilder sb = new StringBuilder();
                for (String author : info.author)
                    sb.append(author).append(",");
                sb.deleteCharAt(sb.length() - 1);
                Language.setGlobalLanguage("extension." + info.id + ".author", sb.toString());
            }

            if (info.tags != null) {
                StringBuilder sb = new StringBuilder();
                for (String tag : info.tags)
                    sb.append(tag).append(",");
                sb.deleteCharAt(sb.length() - 1);
                Language.setGlobalLanguage("extension." + info.id + ".tags", sb.toString());
            }

            if (info.languages != null)
                for (String language : info.languages)
                    Language.loadLanguage(classLoader, language);
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
                Logger.warning(getLanguage(MESSAGE_ERROR + "entrypointNotFound", getId(), entrypoint));
                continue;
            }

            // 实例化程序入口
            try {
                if (loadedClass.getDeclaredConstructor().newInstance() instanceof ExtensionEntrypoint ei) {
                    this.entrypointList.add(ei);
                } else {
                    Logger.warning(getLanguage(MESSAGE_ERROR + "entrypointNotImplemented", getId(), entrypoint));
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                Logger.warning(getLanguage(MESSAGE_ERROR + "entrypointInstanceFailed", getId(), entrypoint, e));
            }
        }
    }

    /**
     * 初始化扩展
     */
    protected void initialize() {
        //检查依赖扩展
        for (Map.Entry<String, Version> dependExtension : info.depends.extensions.entrySet()) {
            String dependExtensionId = dependExtension.getKey();
            Extension dependExt = manager.getExtension(dependExtensionId);
            if (dependExt == null) {
                Logger.warning(getLanguage(MESSAGE_ERROR + "message.extension.error.dependExtensionLost", getId(), dependExtensionId, dependExtension.getValue()));
                return;
            }
            if (!dependExtension.getValue().isSupported(dependExt.getVersion())) {
                Logger.warning(getLanguage(MESSAGE_ERROR + "message.extension.error.dependExtensionNotCompatible", getId(), dependExtensionId, dependExtension.getValue(), dependExt.getVersion()));
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
     * 获取全局资源
     *
     * @param name 资源路径
     * @return 资源数据
     */
    public URL getGlobalResource(String name) {
        return classLoader.getResource(name);
    }

    /**
     * 获取assets下的资源
     *
     * @param name 资源路径 'assets.id.name'
     * @return 资源数据
     */
    public InputStream getResourceAsStream(String name) {
        return classLoader.getResourceAsStream("assets/" + info.id + "/" + name);
    }

    /**
     *  获取assets下的资源
     * @param name 资源路径 'assets.id.name'
     * @return 资源数据
     */
    public String getResourceAsString(String name) {
        try (InputStream is = getResourceAsStream(name)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取全局资源
     *
     * @param name 资源路径
     * @return 资源数据
     */
    public URL getResource(String name) {
        return classLoader.getResource("assets/" + info.id + "/" + name);
    }


    public String getLanguage(String key, Object... args) {
        return Language.get(key, args);
    }

    public static final String MESSAGE_ERROR = "message.extension.error.";


    /**
     * 扩展信息
     */
    private static class ExtensionInfo {

        /**
         * 扩展ID
         */
        String id;
        /**
         * 版本
         */
        Version version;
        /**
         * 名称
         */
        String name;
        /**
         * 描述
         */
        String description;
        /**
         * 扩展图标
         */
        String icon;
        /**
         * 开发者
         */
        List<String> author;
        /**
         * 联系方式
         */

        Contact contact;
        /**
         * 入口点
         */

        List<String> entrypoints;
        /**
         * 依赖
         */

        Depends depends;

        /**
         * 扩展标签
         */
        List<String> tags;

        /**
         * 语言列表
         */
        List<String> languages;

        /**
         * 依赖类
         */
        public static class Depends {
            /**
             * 扩展API
             */
            Version extensionApi;
            /**
             * 依赖的扩展列表
             */
            Map<String, Version> extensions;
        }

        /**
         * 联系方式类
         */
        public static class Contact {
            /**
             * 主页链接
             */
            String homepage;
            /**
             * 源链接
             */
            String source;
        }


    }

}
