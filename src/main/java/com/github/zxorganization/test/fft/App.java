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
import java.util.Random;

public class App extends Application {
    Stage stage;
    Canvas canvas = new Canvas();
    Canvas canvas2 = new Canvas();
    Timeline timeline;
    int pos = 0;//音频的显示位置

    int N = 512;
    int S = 100;
    Complex[] data = new Complex[N];

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        VBox vBox = new VBox();
        {
            Pane pane = new Pane(canvas);
            pane.setBackground(Background.fill(Color.BLACK));
            pane.setPrefHeight(300);
            vBox.getChildren().add(pane);
            FxUtils.canvasBind(canvas);
        }
        {
            Pane pane = new Pane(canvas2);
            pane.setBackground(Background.fill(Color.BLACK));
            pane.setPrefHeight(300);
            vBox.getChildren().add(pane);
            FxUtils.canvasBind(canvas2);
        }


        //vBox.setBackground(Background.fill(Color.GRAY));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setWidth(1800);
        stage.show();




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

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(App.class.getResource("ここなつ - キミヱゴサーチ_01.wav"));

        Image image = new Image(App.class.getResource("Line.png").toString());


        ByteBuffer buf = ByteBuffer.wrap(audioInputStream.readAllBytes());
        buf.order(ByteOrder.LITTLE_ENDIAN);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();


        // 创建一个Timeline对象，每秒执行30次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000. / 10), e -> {

            pos += 1000;//音频偏移


            double w = canvas.getWidth();
            double h = canvas.getHeight();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());


            double lastV = 0;
            for (int x = 0; x < w; x++) {
                buf.position((pos + x * S) * 2);


                for (int i = 0; i < S; i++) {
                    double v = buf.getShort() / 200.;
                    //v = Math.max(v, 1);
                    gc.drawImage(image, x, (v < 0 ? h / 2 : h / 2 - v), 1, Math.abs(v));
                    /*v = h / 2. - v;
                    gc.setStroke(Color.rgb(0, 100, 0));
                    gc.strokeLine(x - 1, lastV, x, v);
                    lastV = v;*/
                }

                if (x % 6 == 0) {


                    buf.position((pos + x * S) * 2);
                    for (int i = 0; i < N; i++) {
                        data[i] = new Complex(buf.getShort() / 80., 0);
                    }

                    //傅里叶变换计算
                    data = FFT.getFFT(data, N);//傅里叶变换
                    Double[] x2 = Complex.toModArray(data);//计算傅里叶变换得到的复数数组的模值
                    for (int i = 0; i < N / 2; i++) {
                        x2[N / 2 + i] = x2[N / 2 + i] / N * 3;
                        int c = (int) (x2[N / 2 + i] * 20);
                        gc2.setLineWidth(6);
                        gc2.setStroke(Color.rgb((Math.min(c, 255)), (c > 255 ? Math.min(c - 255, 255) : 0), (int) ((Math.min(c, 255)) * 0.3)));
                        gc2.strokeLine(x, i , x, i );
                    }



                }

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //设置无限循环
        timeline.play(); //开始播放

    }


}
