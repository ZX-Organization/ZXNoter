import team.zxorg.core.ZXLanguage;
import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;
import team.zxorg.extension.ExtensionManager;

import java.nio.file.Path;
import java.util.Locale;

public class RunApp {
    public static ZXVersion VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);

    public static void main(String[] args) {
        // 初始化语言
        ZXLanguage.setGlobalLanguage("version.name", VERSION.toString());
        ZXLanguage.setGlobalLanguage("version.code", String.valueOf(VERSION.getCode()));
        ZXLanguage.loadLanguage(RunApp.class.getClassLoader(), "languages/zh-cn.json");
        ZXLanguage.setLocale(Locale.CHINA);

        VERSION.printInfo();
        ZXLogger.info(ZXLanguage.get("message.extension.loading"));

        ExtensionManager extensionManager = new ExtensionManager();


        Path exceptionsPath = Path.of("./extensions");
        extensionManager.loadExtensions(exceptionsPath);


    }
}
