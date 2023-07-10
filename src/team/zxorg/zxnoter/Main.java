package team.zxorg.zxnoter;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.util.Logging;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.ui.main.ZXStage;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        ZXLogger.info("ZXNoter启动");
        commandLineArgs = args;

        ZXLogger.info("初始化图形系统");

        //Logging.getJavaFXLogger().disableLogging();
        // 创建自定义的 PrintStream 实例

        PlatformImpl.startup(() -> {
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            ZXConfiguration.reload();
            ZXStage zxStage = new ZXStage();
            ZXLogger.info("显示ZXN-UI窗口");
            zxStage.show();
        });

    }

}
