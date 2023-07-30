package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.ZXLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        try (InputStream inputStream = Files.newInputStream(Paths.get("configuration.json"))) {
            ZXLogger.info("载入全局配置");
            root = JSON.parseObject(inputStream);
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

    public static void saveConfig() {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get("configuration.json"))) {
            ZXLogger.info("保存全局配置");
            JSON.writeTo(outputStream, root);
        } catch (IOException e) {
            ZXLogger.severe("保存全局配置失败");
            throw new RuntimeException(e);
        }
    }

    public static JSONObject getLast() {
        return root.getJSONObject("last");
    }
}
