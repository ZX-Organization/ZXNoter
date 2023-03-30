package com.github.zxorganization.ui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.application.Application;
import javafx.stage.Stage;

public class ZXNoterApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.show();
        JSONObject jsonObject=new JSONObject();
    }

    /**
     * 启动ZXNoter
     * @param args 命令行参数
     */
    public void runApp(String[] args) {
            launch(args);
    }
}
