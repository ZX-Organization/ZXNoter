package team.zxorg.skin;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.skin.uis.ExpressionCalculator;
import team.zxorg.skin.uis.UISPerspectiveTransform;
import team.zxorg.skin.uis.UISSkin;
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

            ExpressionCalculator expressionCalculator = new ExpressionCalculator();
            expressionCalculator.setCanvasSize(1920, 1080);
            UISSkin skin = new UISSkin(Path.of("D:\\malody\\skin\\Xiang Vma-3.3\\pc-4K.mui"), expressionCalculator);
            System.out.println(skin.getComponent("_black-1"));
            System.out.println(skin.getComponent("_black-1").getImage("tex"));
            System.out.println(skin.getComponent("_black-1").getPath("bg/bg-4k.png"));
        });
    }

}
