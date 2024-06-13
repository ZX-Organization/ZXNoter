package team.zxorg.zxnoter.ui;

import javafx.application.Platform;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.ui.base.FileManagerActivityPaneSkin;
import team.zxorg.zxnoter.ui.base.LoggerActivityPaneSkin;
import team.zxorg.zxnoter.ui.base.SetupActivityPaneSkin;
import team.zxorg.zxnoter.ui.base.WelcomeActivityPaneSkin;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;

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

        //System.out.println(ExtensionManager.getCurrentExtension(0));
        //注册基本UI
        //ActivityBar.registerSideBar("fileManager", FileManagerSideBar.class);
        //ActivityBar.registerSideBar("setup", SetupSideBar.class);
        ActivityPane.register(FileManagerActivityPaneSkin.class);
        ActivityPane.register(SetupActivityPaneSkin.class);
        ActivityPane.register(WelcomeActivityPaneSkin.class);
        ActivityPane.register(LoggerActivityPaneSkin.class);




    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
        Logger.info(extension.getLanguage("extension.zxnoterUiFrame.start"));

        //ActivityBar.updateConfigMenu();

        Platform.runLater(() -> {
            {
                ProjectView projectView = new ProjectView();
                projectView.show();
            }
        });


    }
}

