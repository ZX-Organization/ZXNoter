package team.zxorg.zxnoter.uiframe;

import javafx.application.Platform;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.uiframe.base.FileManagerSideBar;
import team.zxorg.zxnoter.uiframe.base.SetupSideBar;
import team.zxorg.zxnoter.uiframe.component.ActivityBar;

public class ZXNoter implements ExtensionEntrypoint {
    public static Extension extension;
    public static Configuration config;


    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        ZXNoter.extension = extension;
        config = extension.getConfig();
        /*try {

            Class<?> loadedClass = extension.getClassLoader().loadClass("team.zxorg.zxnoter.uiframe.base.FileManagerSideBar");
            System.out.println(loadedClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/

        //注册基本UI
        ActivityBar.registerItem("fileManager", FileManagerSideBar.class);
        ActivityBar.registerItem("setup", SetupSideBar.class);
    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
        Logger.info(extension.getLanguage("extension.zxnoterUiFrame.start"));

        Platform.runLater(() -> {
            ProjectViewStage projectViewStage = new ProjectViewStage();
            projectViewStage.show();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.info("关闭程序");
        }));
    }
}

