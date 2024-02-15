package team.zxorg.zxnoter.uiframe;

import javafx.application.Platform;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.zxnoter.uiframe.activitypane.ActivityPane;

import java.util.HashMap;

public class ZXNoter implements ExtensionEntrypoint {
    public static Extension extension;
    public static Configuration config;

    /**
     * 当前所有被注册的侧边栏类
     */
    protected static final HashMap<String, Class<? extends ActivityPane>> ActivityPaneClassMap = new HashMap<>();

    public static void registerActivityPane(Class<? extends ActivityPane> activityPaneClass) {
        ActivityPaneClassMap.put(activityPaneClass.getSimpleName(), activityPaneClass);
    }

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
    }

    @Override
    public void onAllInitialized(Extension extension, ExtensionManager manager) {
        Logger.info(extension.getLanguage("extension.zxnoterUiFrame.start"));

        //ActivityBar.updateConfigMenu();

        Platform.runLater(() -> {
            ProjectView projectView = new ProjectView();
            projectView.show();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.info("关闭程序");
        }));
    }
}

