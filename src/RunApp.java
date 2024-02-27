import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.core.Version;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.extensionloader.library.LibraryManager;

import java.nio.file.Path;
import java.util.Locale;

public class RunApp extends ExtensionManager {
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


        RunApp extensionManager = new RunApp();
        Path exceptionsPath = Path.of("./extensions");

        ClassLoader libraryClassLoader = LibraryManager.loadingAllLibrary(RunApp.class.getClassLoader(), Path.of("./libraries"));
        extensionManager.loadAllExtensions(libraryClassLoader, exceptionsPath);
        Configuration.save();

    }
}
