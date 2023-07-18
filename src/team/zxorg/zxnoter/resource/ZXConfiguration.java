package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.ZXLogger;

import java.io.IOException;
import java.nio.file.*;

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
            ZXLogger.info("载入全局配置");
            root = JSON.parseObject(Files.newInputStream(Paths.get("configuration.json")));
        } catch (IOException e) {
            ZXLogger.severe("载入全局配置失败");
            throw new RuntimeException(e);
        }



        ZXResources.clearPacks();//清除一遍资源

        ZXResources.loadInternalPacks();


        ZXResources.searchPacks(Path.of("./resource"));//搜索本地资源包

        for (ResourceType resourceType : ResourceType.values()) {
            ZXResources.reloadGlobalResource(resourceType);//应用本地资源包
        }

    }

}
