package team.zxorg.zxnoter.uiframe;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.core.ZXLogger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionManager;

public class ZXNoterManager implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {



        ZXLogger.info(extension.getLanguage("extension.zxnoterUiFrame.initialize"));

        ZXLogger.info("ZXNoter启动");


        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");


        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            //ConfigManager.reload();

            ProjectView projectView = new ProjectView();
            projectView.show();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ZXLogger.info("关闭程序");
            }));

        });

    }
}

