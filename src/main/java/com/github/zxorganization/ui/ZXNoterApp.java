package com.github.zxorganization.ui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class ZXNoterApp extends Application {
    @Override
    public void start(Stage primaryStage) {

        //HashMap<Pattern, ArrayList<>>;// osu\mc\imd ->> zxnoter.java
        

        System.out.println(Pattern.compile("^osu$|^abc[0-9]$").matcher("osu").find());;


        try {

            ZXNoterApp.class.getConstructor(String.class, String.class, Path.class).newInstance("sadas", "",Path.of(""));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


        EventHandler handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event);
            }
        };


        primaryStage.show();
        primaryStage.addEventHandler(MouseEvent.ANY, handler);
        JSONObject jsonObject = new JSONObject();
    }

    void registerSideBarUI(String name) {

    }

    /**
     * 启动ZXNoter
     *
     * @param args 命令行参数
     */
    public void runApp(String[] args) {
        launch(args);
    }
}
