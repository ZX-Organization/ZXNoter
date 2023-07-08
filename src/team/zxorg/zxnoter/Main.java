package team.zxorg.zxnoter;

import javafx.application.Application;
import team.zxorg.zxnoter.ui.ZXNApp;

public class Main {
    public static String[] commandLineArgs;

    public static void main(String[] args) {
        commandLineArgs = args;

        //初始化图形系统
        Application.launch(ZXNApp.class);
    }

}
