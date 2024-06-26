package team.zxorg.zxnoter.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.config.root.DefaultConfiguration;

import java.io.IOException;
import java.io.InputStream;

public class ZXDefaultConfigManager {
    public static DefaultConfiguration configuration;
    static {
        try(InputStream defaultConfig = DefaultConfiguration.class.getResourceAsStream("/base/default-config.json")){
            ZXLogger.info("载入默认配置");
            JSONObject root = JSON.parseObject(defaultConfig);
            configuration = root.toJavaObject(DefaultConfiguration.class);
        } catch (IOException e) {
            ZXLogger.severe("载入默认配置失败");
            throw new RuntimeException(e);
        }
    }
}
