package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.Main;
import team.zxorg.zxnoter.ZXLogger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ZXNoter全局配置类
 */
public class ZXConfiguration {
    public static JSONObject root;


    /**
     * 获取配置域
     *
     * @param scope 域
     * @return json配置对象
     */
    public static JSONObject getScopeConfig(String scope) {
        return (JSONObject) root.computeIfAbsent(scope, k -> new JSONObject());
    }

    /**
     * 重载配置 (会重新载入资源文件)
     */
    public static void reload() {
        try {
            ZXLogger.logger.info("载入全局配置");
            root = JSON.parseObject(Files.newInputStream(Paths.get("configuration.json")));
        } catch (IOException e) {
            ZXLogger.logger.severe("载入全局配置失败");
            throw new RuntimeException(e);
        }

        Path resourcePath = Paths.get("res/resources");
        if (!Files.exists(resourcePath)) {
            try {

                URL resourceUrl = ZXConfiguration.class.getResource("/resources");
                URI resourceUri = resourceUrl.toURI();
                Map<String, String> env = new HashMap<>();
                env.put("create", "true");
                FileSystem zipfs = FileSystems.newFileSystem(resourceUri, env);
                resourcePath = zipfs.getPath("/resources");
                ZXLogger.logger.info("使用内部资源 url:" + resourceUrl);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
                ZXLogger.logger.severe("ZXConfiguration:载入内部资源异常");
            }
        } else {
            ZXLogger.logger.info("ZXConfiguration:进入开发阶段");
        }

        ZXResources.clearPacks();//清除一遍资源

        ZXResources.searchPacks(resourcePath);//搜索资源包

        ZXResources.searchPacks(Path.of("./resource"));//搜索本地资源包

        ZXResources.reloadGlobalResource();//应用本地资源包
    }

}
