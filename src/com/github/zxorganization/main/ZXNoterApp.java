package com.github.zxorganization.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class ZXNoterApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    /**
     * 启动ZXNoter
     * @param args 命令行参数
     */
    public void runApp(String[] args) {
            launch(args);
    }
}
