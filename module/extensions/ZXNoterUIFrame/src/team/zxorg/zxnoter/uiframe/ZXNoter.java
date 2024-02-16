package team.zxorg.zxnoter.uiframe;

import javafx.application.Platform;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.uiframe.activitypane.ActivityBarPane;
import team.zxorg.zxnoter.uiframe.base.FileManagerActivityPane;
import team.zxorg.zxnoter.uiframe.base.SetupActivityPane;
import team.zxorg.zxnoter.uiframe.base.WelcomeActivityPane;

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
        //ActivityBar.registerSideBar("fileManager", FileManagerSideBar.class);
        //ActivityBar.registerSideBar("setup", SetupSideBar.class);
        ActivityBarPane.register(FileManagerActivityPane.class);
        ActivityBarPane.register(SetupActivityPane.class);
        ActivityBarPane.register(WelcomeActivityPane.class);
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

