package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ZXNoter全局配置类
 */
public class ZXConfig {
    public static JSONObject root;

    public static JSONObject getScopeConfig(String scope) {
        return (JSONObject) root.computeIfAbsent(scope, k -> new JSONObject());
    }

    static {
        try {
            System.out.println("载入全局配置");
            root = JSON.parseObject(Files.newInputStream(Paths.get("configuration.json")));
        } catch (IOException e) {
            System.out.println("载入全局配置失败");
            throw new RuntimeException(e);
        }
    }
}
