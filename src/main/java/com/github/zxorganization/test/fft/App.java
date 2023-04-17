package com.github.zxorganization.test.fft;

import com.github.zxorganization.utils.FxUtils;
import com.sun.javafx.application.LauncherImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class App extends Application {
    Stage stage;
    Canvas canvas = new Canvas();
    Timeline timeline;
    int pos=1024;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;

        Pane pane = new Pane(canvas);
        pane.setBackground(Background.fill(Color.DARKGRAY));
        pane.setPrefHeight(600);

        VBox vBox = new VBox(pane);
        FxUtils.canvasBind(canvas);
        //vBox.setBackground(Background.fill(Color.GRAY));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();




        /*App app=new App();
        System.out.println("APP "+app);*/

       /* TargetDataLine targetDataLine;
        AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        *//*{
            int i = 0;
            for (Mixer.Info info : mixerInfos) {
                System.out.println(i++ + " " + info.getName());
            }
        }*//*

        Mixer mixer = AudioSystem.getMixer(mixerInfos[30]);
        targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
*/

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(App.class.getResource("angela - 全力 Summer!.wav"));

        Image image = new Image(App.class.getResource("Line.png").toString());


        ByteBuffer buf = ByteBuffer.wrap(audioInputStream.readAllBytes());
        buf.order(ByteOrder.LITTLE_ENDIAN);
/*
        double[] data = {-0.35668879080953375, -0.6118094913035987, 0.8534269560320435, -0.6699697478438837, 0.35425500561437717,
                0.8910250650549392, -0.025718699518642918, 0.07649691490732002};
        int N = 8;
        Complex[] input = new Complex[N];
        for (int i = 0; i <= N - 1; i++) {
            input[i] = new Complex(data[i], 0);
        }


        //傅里叶变换计算
        Double[] x;//声明复数数组
        Double[] x1 = new Double[N];
        for (int i = 0; i <= N - 1; i++) {
            input[i] = new Complex(data[i], 0);
        }//将实数数据转换为复数数据
        input = FFT.getFFT(input, N);//傅里叶变换
        x = Complex.toModArray(input);//计算傅里叶变换得到的复数数组的模值
        for (int i = 0; i <= N - 1; i++) {
            //的模值数组除以N再乘以2
            x1[i] = x[i] / N * 2;
           // System.out.println(x1[i]);
        }*/

        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 创建一个Timeline对象，每秒执行60次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 60), e -> {
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            buf.position(++pos*40);
            for (int i = 0; i < w; i++) {


                int v = buf.getShort()/100;


                 gc.drawImage(image,i,h / 2 - v,1, v*2);
                /*gc.setStroke(Color.RED);
                gc.setLineWidth(1); //设置线条宽度为5像素
                gc.strokeLine(i, h / 2 + v, i, h / 2 - v);
                gc.stroke();*/
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //设置无限循环
        timeline.play(); //开始播放

    }


}
