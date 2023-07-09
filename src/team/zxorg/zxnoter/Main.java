package team.zxorg.zxnoter;

import javafx.application.Application;
import team.zxorg.zxnoter.ui.ZXNApp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        ZXLogger.logger.info("ZXNoter启动");
        commandLineArgs = args;

        ZXLogger.logger.info("初始化图形系统");
        //初始化图形系统
        Application.launch(ZXNApp.class);

    }

}
