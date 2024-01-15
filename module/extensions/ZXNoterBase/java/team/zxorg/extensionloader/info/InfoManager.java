package team.zxorg.zxncore.info;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import team.zxorg.zxncore.info.root.ConfigurationInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 将json文件和java对象互相转换
 * 提供序列化和反序列化功能
 * 拥有管理全局信息对象的能力
 */
public class InfoManager {

    /**
     * 重载配置 (会重新载入资源文件)
     */
    private static Object read(Class<?> clazz, Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            //ZXLogger.info("载入全局配置");
            JSONObject root = JSON.parseObject(inputStream);
            return root.toJavaObject(clazz);
        } catch (IOException e) {
            //ZXLogger.severe("载入全局配置失败");
            throw new RuntimeException(e);
        }
    }
    /**
     * 保存配置
     */
    private static void save(Object o) {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get("configuration.json"))) {
            //ZXLogger.info("保存全局配置");
            JSON.writeTo(outputStream, o, JSONWriter.Feature.PrettyFormat);
        } catch (IOException e) {
            //ZXLogger.severe("保存全局配置失败");
            throw new RuntimeException(e);
        }
    }
}
