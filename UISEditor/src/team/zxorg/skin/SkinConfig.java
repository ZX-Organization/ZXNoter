package team.zxorg.skin;

import com.google.gson.Gson;
import team.zxorg.zxncore.ZXLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class SkinConfig {
    public static Gson gson = new Gson();
    public static SkinConfig data;
    private static Path configPath = Path.of("./config.json");

    static {
        if (Files.exists(configPath)) {
            ZXLogger.info("载入配置: " + configPath.toAbsolutePath());
            try {
                data = gson.fromJson(Files.newBufferedReader(configPath), SkinConfig.class);
            } catch (IOException e) {
                ZXLogger.warning("配置载入失败: " + e);
            }
        } else {
            data = new SkinConfig();
            ZXLogger.info("创建新配置文件");
        }
    }

    public String lastOpenDir;
    public List<String> historical;

    public SkinConfig() {
        historical = new ArrayList<>();
        lastOpenDir = "";
    }

    public static void save() {
        ZXLogger.info("保存配置: " + configPath.toAbsolutePath());
        try {
            Files.writeString(configPath, gson.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
