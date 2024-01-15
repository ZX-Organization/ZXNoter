package team.zxorg.extendedtemplate;

import com.google.gson.Gson;
import team.zxorg.api.ExtensionInitializer;
import team.zxorg.core.ZXLogger;

public class ExtensionTemplate implements ExtensionInitializer {
    @Override
    public void onInitialize() {
        ZXLogger.info("扩展模板 初始化完毕.");
        ZXLogger.info(new Gson().toJson(this));
    }
}
