package team.zxorg.zxnoter.ui;

import javafx.application.Platform;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.ui.base.FileManagerActivityPane;
import team.zxorg.zxnoter.ui.base.SetupActivityPane;
import team.zxorg.zxnoter.ui.base.WelcomeActivityPane;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityBarPane;
import team.zxorg.zxnoter.ui.component.titlebar.menu.MenuItemInfo;

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

        System.out.println(ExtensionManager.getCurrentExtension(0));
        //注册基本UI
        //ActivityBar.registerSideBar("fileManager", FileManagerSideBar.class);
        //ActivityBar.registerSideBar("setup", SetupSideBar.class);
        ActivityBarPane.register(FileManagerActivityPane.class);
        ActivityBarPane.register(SetupActivityPane.class);
        ActivityBarPane.register(WelcomeActivityPane.class);
        System.out.println("111\n1222\n333");
        Logger.info("111\n1222\n333");
        Logger.logExceptionStackTrace(new RuntimeException());

        for (Extension res : manager.getExtensions()) {
            MenuItemInfo item = res.getExtensionResource("ui/menu/titleBar.json5", MenuItemInfo.class);
            System.out.println(res);
            System.out.println(item);
        }

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

