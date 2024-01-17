package team.zxorg.extension;

import team.zxorg.api.ExtensionEntrypoint;
import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;
import team.zxorg.gson.ZXGson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return "扩展[id: " + info.id + ", 版本: " + info.version + "]";
    }

    private final ExtensionInfo info;
    List<ExtensionEntrypoint> initializers;
    ExtensionManager manager;
    URL jarUrl;

    public Extension(ExtensionManager manager, Path jarPath) {
        this.manager = manager;
        ZXLogger.info("即将加载扩展: " + jarPath);

        try {
            jarUrl = jarPath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("扩展载入异常: " + e);
        }

        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl});
        try (InputStream is = classLoader.getResourceAsStream("extension.json")) {
            if (is == null)
                throw new FileNotFoundException("extension.json");
            InputStreamReader reader = new InputStreamReader(is);
            info = ZXGson.fromJson(reader, ExtensionInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("读取扩展信息失败: " + e);
        }
        ZXLogger.info("读取扩展信息: " + this);


    }


    protected void loadJar(URLClassLoader classLoader) {
        initializers = new ArrayList<>();
        for (String entrypoint : info.entrypoints) {
            // 加载类
            Class<?> loadedClass; // 替换为你的类的完整路径
            try {
                loadedClass = classLoader.loadClass(entrypoint);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("未找到入口 " + entrypoint + " " + e);
            }

            // 实例化程序入口
            try {
                if (loadedClass.newInstance() instanceof ExtensionEntrypoint ei) {
                    initializers.add(ei);
                } else {
                    ZXLogger.warning("入口 " + entrypoint + " 没有实现 ExtensionEntrypoint 接口");
                }
            } catch (InstantiationException | IllegalAccessException e) {
                ZXLogger.warning("实例化入口 " + entrypoint + " 时发生错误 " + e);
            }
        }
    }

    /**
     * 初始化扩展
     */
    protected void initialize() {
        Collection<String> extensionIdList = manager.getExtensionIdList();

        //检查依赖扩展
        for (String dependExtension : info.depends.extensions) {
            if (!extensionIdList.contains(dependExtension)) {
                ZXLogger.warning("依赖扩展 " + dependExtension + " 未找到，无法加载扩展 " + info.id);
                return;
            }
        }

        // 初始化扩展
        for (ExtensionEntrypoint initializer : initializers) {
            initializer.onInitialize(this, manager);
        }
    }
}
