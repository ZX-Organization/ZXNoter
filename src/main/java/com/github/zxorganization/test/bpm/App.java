package com.github.zxorganization.test.bpm;

import com.github.zxorganization.sound.audiomixer.AudioChannel;
import com.github.zxorganization.sound.audiomixer.AudioMixer;
import com.github.zxorganization.test.fft.Complex;
import com.github.zxorganization.test.fft.FFT;
import com.github.zxorganization.utils.FxUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class App extends Application {
    Stage stage;
    Canvas canvas = new Canvas();
    Canvas canvas2 = new Canvas();
    Timeline timeline;
    int pos = 937505;//音频的显示位置

    int N = 512;
    int S = 80;

    public App() throws LineUnavailableException {
    }


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


        Image image = new Image(App.class.getResource("../Line.png").toString());


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        //audioChannel.play();


        canvas.setOnMouseClicked(event -> {
        });


        // 创建一个Timeline对象，每秒执行30次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000. / 10), e -> {

            //pos = (int) (1d * audioChannel.getTime() / audioChannel.getAudioLength() * audioSize);//音频偏移

            double w = canvas.getWidth();
            double h = canvas.getHeight();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());

            for (int x = 0; x < w; x++) {

                /*gc.setTextAlign(TextAlignment.LEFT);
                    gc.setFont(Font.font(16));
                    gc.setFill(Color.GREEN);
                    gc.fillText("" + (int) ((buf.position() / 2 * 1000) / audioSampleRate), x, 100);*/

                // gc.drawImage(image, x, (v < 0 ? h / 2 : h / 2 - v), 1, Math.abs(v));


                    /*gc2.setLineWidth(1);
                    gc2.setStroke(Color.rgb((Math.min(v2, 255)), (v2 > 255 ? Math.min(v2 - 255, 255) : 0), (v2 > 255 * 2 ? Math.min(v2 - 255 * 2, 255) : 0)));
                    gc2.strokeLine(x, 280, x, 280);*/


                    /*gc.setTextAlign(TextAlignment.LEFT);
                    gc2.fillText(" " + latestBpm, x + 10, 296);
                    gc2.setLineWidth(2);
                    gc2.strokeLine(x, 280, x, 290);*/


            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //设置无限循环
        timeline.play(); //开始播放

    }


}
