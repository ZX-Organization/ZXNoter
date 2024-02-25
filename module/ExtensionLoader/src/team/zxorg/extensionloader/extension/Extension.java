package team.zxorg.extensionloader.extension;

import org.apache.commons.lang3.SystemUtils;
import team.zxorg.extensionloader.core.*;
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
import java.util.StringJoiner;

/**
 * 扩展对象
 */
public class Extension {
    /**
     * 扩展配置信息
     */
    private final ExtensionInfo info;
    /**
     * 扩展管理器
     */
    private final ExtensionManager manager;
    /**
     * 扩展jar文件路径
     */
    private final Path jarPath;
    /**
     * 扩展jar文件路径
     */
    private final URL jarUrl;
    private final Configuration config;
    /**
     * 扩展入口点列表
     */
    private List<ExtensionEntrypoint> entrypointList;

    public List<ExtensionEntrypoint> getEntrypointList() {
        return entrypointList;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 扩展类加载器
     */
    private URLClassLoader classLoader;
    /**
     * 是否已加载
     */
    private boolean loaded;

    /**
     * 扩展是否已被载入
     *
     * @return 是否被载入
     */
    public boolean isLoaded() {
        return loaded;
    }

    public Extension(ExtensionManager manager, Path jarPath) {
        this.jarPath = jarPath;
        this.manager = manager;
        try {
            jarUrl = jarPath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_URL, jarPath, e));
        }

        //载入jar读取扩展信息
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl})) {
            info = GsonManager.fromJson(classLoader, "extension.json5", ExtensionInfo.class);
            config = new Configuration(info.id);
            String Extension_LANG = "extension." + info.id;
            Language.setGlobalLanguage(Extension_LANG + ".version", info.version.toString());
            for (Path language : info.languages)
                Language.loadLanguage(classLoader, language);

            {
                StringJoiner sj = new StringJoiner(", ");
                for (Language author : info.author)
                    sj.add(author.toString());
                Language.setGlobalLanguage(Extension_LANG + ".author", sj.toString());
            }

            {
                StringJoiner sj = new StringJoiner(", ");
                for (Language tag : info.tags)
                    sj.add(tag.toString());
                Language.setGlobalLanguage(Extension_LANG + ".tags", sj.toString());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_INFO_READ_FAILED, jarPath, e));
        }
    }

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
    public VersionChecker getApiVersion() {
        return info.depends.extensionApi;
    }

    /**
     * 是否支持当前平台
     *
     * @return 是否支持
     */
    public boolean isPlatformSupported() {
        //如果没有注明则默认支持所有平台
        if (info.platforms.isEmpty())
            return true;
        String platform = (SystemUtils.IS_OS_WINDOWS ? "windows" : SystemUtils.IS_OS_LINUX ? "linux" : SystemUtils.IS_OS_MAC ? "macos" : "unknown");
        return info.platforms.stream().anyMatch(p -> p.toLowerCase().equals(platform));
    }

    @Override
    public String toString() {
        return "(" + info.id + " " + info.version + ")";
    }

    public URL getJarUrl() {
        return jarUrl;
    }

    /**
     * 检查依赖扩展
     *
     * @return 检查结果
     */
    protected boolean dependencyCheck() {
        for (Map.Entry<String, VersionChecker> dependExtension : info.depends.extensions.entrySet()) {
            String dependExtensionId = dependExtension.getKey();
            Extension dependExt = manager.getExtension(dependExtensionId);
            if (dependExt == null) {
                Logger.warning(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_DEPEND_EXTENSION_LOST, getId(), dependExtensionId, dependExtension.getValue()));
                return false;
            }
            if (!dependExtension.getValue().isSupported(dependExt.getVersion())) {
                Logger.warning(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_DEPEND_EXTENSION_NOT_COMPATIBLE, getId(), dependExtensionId, dependExtension.getValue(), dependExt.getVersion()));
                return false;
            }
        }
        return true;
    }

    /**
     * 载入扩展
     *
     * @param classLoader 类加载器
     */
    protected void load(URLClassLoader classLoader) {
        this.classLoader = classLoader;
        entrypointList = new ArrayList<>();
        for (String entrypoint : info.entrypoints) {
            // 加载类
            Class<?> loadedClass;
            try {
                loadedClass = classLoader.loadClass(entrypoint);
            } catch (ClassNotFoundException e) {
                Logger.warning(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_ENTRYPOINT_NOT_FOUND, getId(), entrypoint));
                continue;
            }

            // 实例化程序入口
            try {
                if (loadedClass.getDeclaredConstructor().newInstance() instanceof ExtensionEntrypoint ei) {
                    this.entrypointList.add(ei);
                } else {
                    Logger.warning(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_ENTRYPOINT_NOT_IMPLEMENTED, getId(), entrypoint));
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                Logger.warning(getLanguage(LanguageKey.MESSAGE_EXTENSION_ERROR_ENTRYPOINT_INSTANCE_FAILED, getId(), entrypoint, e));
            }
        }

        loaded = true;

        // 载入扩展
        for (ExtensionEntrypoint initializer : entrypointList) {
            initializer.onLoaded(this, manager);
        }
    }

    /**
     * 初始化扩展
     */
    protected void initialize() {
        // 初始化扩展
        for (ExtensionEntrypoint initializer : entrypointList) {
            initializer.onInitialize(this, manager);
        }
    }


    protected void allInitialized() {
        for (ExtensionEntrypoint initializer : entrypointList) {
            initializer.onAllInitialized(this, manager);
        }
    }

    /**
     * 获取全局资源
     *
     * @param name 资源路径 'name'
     * @return 资源数据
     */
    public InputStream getGlobalClassResourceAsStream(String name) {
        return classLoader.getResourceAsStream(name);
    }

    /**
     * 获取全局资源
     *
     * @param name 资源路径 'name'
     * @return 资源数据
     */
    public URL getGlobalClassResource(String name) {
        return classLoader.getResource(name);
    }

    /**
     * 获取扩展下的资源
     *
     * @param name 资源路径 'assets/id/name'
     * @return 资源数据
     */
    public InputStream getExtensionResourceAsStream(String name) {
        return classLoader.getResourceAsStream("assets/" + info.id + "/" + name);
    }

    /**
     * 获取扩展下的资源
     *
     * @param name 资源路径 'assets/id/name'
     * @return 资源数据
     */
    public String getExtensionResourceAsString(String name) {
        try (InputStream is = getExtensionResourceAsStream(name)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取全局资源
     *
     * @param name 资源路径 'assets/id/name'
     * @return 资源数据
     */
    public URL getExtensionResource(String name) {
        return classLoader.getResource("assets/" + info.id + "/" + name);
    }

    /**
     * 获取语言
     *
     * @param key  语言键
     * @param args 语言参数
     * @return 语言内容
     */
    public String getLanguage(String key, Object... args) {
        return Language.get(key, args);
    }

    /**
     * 获取语言
     *
     * @param key  语言键
     * @param args 语言参数
     * @return 语言内容
     */
    public String getLanguage(Object key, Object... args) {
        return Language.get(key, args);
    }

    /**
     * 获取扩展配置类
     *
     * @return 配置类
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * 获取扩展jar路径
     *
     * @return jar路径
     */
    public Path getJarPath() {
        return jarPath;
    }


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
        Language name;
        /**
         * 描述
         */
        Language description;
        /**
         * 扩展图标
         */
        Path icon;
        /**
         * 开发者
         */
        List<Language> author;
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
         * 平台
         */
        List<String> platforms;

        /**
         * 扩展标签
         */
        List<Language> tags;

        /**
         * 语言列表
         */
        List<Path> languages;


        /**
         * 依赖类
         */
        public static class Depends {
            /**
             * 扩展API
             */
            VersionChecker extensionApi;
            /**
             * 依赖的扩展列表
             */
            Map<String, VersionChecker> extensions;
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

    /**
     * 实例化类
     *
     * @param name 类名
     * @return 被实例化的类
     */
    public <T> T newInstance(String name) {
        try {
            return (T) newInstance(getClassLoader().loadClass(name));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 实例化类
     *
     * @param clazz 类
     * @return 被实例化的类
     */
    public <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassCastException e) {
            return null;
        }
    }

    /**
     * 获取扩展资源
     *
     * @param path  资源路径
     * @param clazz 资源实例化类
     * @return 资源类
     */
    public <T> T getExtensionResource(String path, Class<T> clazz) {
        InputStream is = getExtensionResourceAsStream(path);
        if (is == null)
            return null;
        return GsonManager.fromJson(is, clazz);
    }
}
