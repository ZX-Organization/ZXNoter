import team.zxorg.extensionloader.core.*;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.extensionloader.extension.ExtensionManager;

import java.nio.file.Path;
import java.util.Locale;

public class RunApp {
    public static Version VERSION = new Version(0, 0, 0, Version.ReleaseStatus.ALPHA);

    public static void main(String[] args) {
        // 初始化语言
        Language.setGlobalLanguage("version.name", VERSION.toString());
        Language.setGlobalLanguage("version.code", String.valueOf(VERSION.getCode()));
        Language.loadLanguage(Language.class.getClassLoader(), "languages/zh-cn.json5");
        Language.setLocale(Locale.CHINA);

        Resource.addEventListener(new ResourceEventListener() {
            @Override
            public void onReload() {
                for (Path lang : Resource.listResourceFiles("languages")) {
                    Language.loadLanguage(lang);
                }
                Language.reloadLanguages();
            }
        });
        Resource.reloadResourcePacks();

        VERSION.printInfo();
        Logger.info(Language.get(LanguageKey.MESSAGE_EXTENSION_LOADING));

        ExtensionManager extensionManager = new ExtensionManager();
        Path exceptionsPath = Path.of("./extensions");
        extensionManager.loadAllExtensions(exceptionsPath);


        extensionManager.initializeAllExtensions();
    }
}
