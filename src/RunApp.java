import team.zxorg.extensionloader.core.ZXLanguage;
import team.zxorg.extensionloader.core.ZXLogger;
import team.zxorg.extensionloader.core.ZXVersion;
import team.zxorg.extensionloader.extension.ExtensionManager;

import java.nio.file.Path;
import java.util.Locale;

public class RunApp {
    public static ZXVersion VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);

    public static void main(String[] args) {
        // 初始化语言
        ZXLanguage.setGlobalLanguage("version.name", VERSION.toString());
        ZXLanguage.setGlobalLanguage("version.code", String.valueOf(VERSION.getCode()));
        ZXLanguage.loadLanguage(ZXLanguage.class.getClassLoader(), "languages/zh-cn.json5");
        ZXLanguage.setLocale(Locale.CHINA);

        VERSION.printInfo();
        ZXLogger.info(ZXLanguage.get("message.extension.loading"));

        ExtensionManager extensionManager = new ExtensionManager();
        Path exceptionsPath = Path.of("./extensions");
        extensionManager.loadAllExtensions(exceptionsPath);
        ZXLanguage.reloadLanguages();


        extensionManager.initializeAllExtensions();
    }
}
