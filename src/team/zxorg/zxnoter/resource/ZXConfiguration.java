package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            System.out.println("ZXConfiguration:载入全局配置");
            root = JSON.parseObject(Files.newInputStream(Paths.get("configuration.json")));
        } catch (IOException e) {
            System.out.println("ZXConfiguration:载入全局配置失败");
            throw new RuntimeException(e);
        }

        ZXResources.searchPacks();//搜索本地资源包
        ZXResources.reloadGlobalResource();//应用本地资源包
    }

}