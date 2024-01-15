package team.zxorg.loader;

import com.google.gson.Gson;
import team.zxorg.api.ExtensionInitializer;
import team.zxorg.core.ZXLogger;
import team.zxorg.info.ExtensionInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 扩展对象
 */
public class Extension {
    ExtensionInfo info;
    HashMap<String, Class<?>> publicClass;
    List<ExtensionInitializer> initializers;
    URLClassLoader classLoader;

    public Extension(URL jarUrl) {
        ZXLogger.info("即将加载扩展: " + jarUrl);
        classLoader = new URLClassLoader(new URL[]{jarUrl});
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("extension.json")) {
            if (is == null)
                throw new FileNotFoundException("extension.json");
            InputStreamReader reader = new InputStreamReader(is);
            info = gson.fromJson(reader, ExtensionInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("读取扩展信息失败: " + e);
        }
        ZXLogger.info("读取扩展信息: " + info.getName() + " - " + info.getVersion());

        initializers = new ArrayList<>();
        for (String entrypoint : info.getEntrypoints()) {
            // 加载类
            Class<?> loadedClass = null; // 替换为你的类的完整路径
            try {
                loadedClass = classLoader.loadClass(entrypoint);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("未找到入口 " + entrypoint + " " + e);
            }

            // 实例化程序入口
            try {
                if (loadedClass.newInstance() instanceof ExtensionInitializer ei) {
                    initializers.add(ei);
                } else {
                    throw new RuntimeException("入口 " + entrypoint + " 没有实现 ExtensionInitializer 接口");
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("实例化入口 " + entrypoint + " 时发生错误 " + e);
            }
        }


    }

    /**
     * 初始化扩展
     */
    public void initialize() {
        for (ExtensionInitializer initializer : initializers) {
            initializer.onInitialize();
        }
    }
}
