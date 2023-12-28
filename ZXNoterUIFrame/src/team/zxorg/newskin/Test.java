package team.zxorg.newskin;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.newskin.uis.UISSkin;
import team.zxorg.zxncore.ZXLogger;

import java.nio.file.Path;

public class Test {
    public static void main(String[] args) {
        ZXLogger.info("内部测试类");
        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");
        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");


            UISSkin skin = new UISSkin(Path.of("D:\\malody\\skin\\ttb5原始 -最新测试\\script-key-4K.mui"));
            skin.getExpressionCalculator().setCanvasSize(1920, 1080);
        });
    }

}
