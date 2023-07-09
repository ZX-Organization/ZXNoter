package team.zxorg.zxnoter;

import javafx.application.Application;
import team.zxorg.zxnoter.ui.ZXNApp;

import java.util.logging.Logger;

public class Main {
    public static String[] commandLineArgs;

    public final static Logger logger = Logger.getLogger("ZXNoter");

    public static void main(String[] args) {
        commandLineArgs = args;
        //初始化图形系统
        Application.launch(ZXNApp.class);
    }

}
