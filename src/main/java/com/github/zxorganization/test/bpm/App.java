package com.github.zxorganization.test.bpm;

import com.github.zxorganization.utils.FxUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.github.zxorganization.test.bpm.Bpm.fft;

public class App extends Application {
    Stage stage;
    Canvas canvas = new Canvas();
    Canvas canvas2 = new Canvas();
    Timeline timeline;

    long audioPosMS = 0;

    double mouseDraggedX = 0;
    double mouseDraggedAudioPosMS = 0;
    double audioPixMS = 14;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        stage.setTitle("FFT BPM");

        VBox vBox = new VBox();
        {
            Pane pane = new Pane(canvas);
            pane.setBackground(Background.fill(Color.BLACK));
            pane.setPrefHeight(360);
            vBox.getChildren().add(pane);
            FxUtils.canvasBind(canvas);
        }
        {
            Pane pane = new Pane(canvas2);
            pane.setBackground(Background.fill(Color.BLACK));
            pane.setPrefHeight(360);
            vBox.getChildren().add(pane);
            FxUtils.canvasBind(canvas2);
        }


        //vBox.setBackground(Background.fill(Color.GRAY));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setWidth(600);
        stage.show();


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        //audioChannel.play();


        canvas.setOnMouseClicked(event -> {
        });


        canvas.setOnMousePressed(event -> {
            mouseDraggedX = event.getX();
            mouseDraggedAudioPosMS = audioPosMS;
        });

        canvas.setOnMouseDragged(event -> {
            audioPosMS = (long) (mouseDraggedAudioPosMS + ((mouseDraggedX - event.getX()) * audioPixMS));
        });

        canvas.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                audioPixMS /= 1.2;
            } else if (event.getDeltaY() < 0) {
                audioPixMS *= 1.2;
            }
        });


        // 创建一个Timeline对象，每秒执行30次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000. / 30), e -> {

            //pos = (int) (1d * audioChannel.getTime() / audioChannel.getAudioLength() * audioSize);//音频偏移

            double w = canvas.getWidth();
            double h = canvas.getHeight();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());


            double ys = h / fft.getFFTAccuracy() * 2;

            long drawText = 0;

            for (int x = 0; x < w; x++) {

                long t = audioPosMS + (long) (x * audioPixMS);
                float[] fftData = fft.fftGet(fft.fftTimeToPosition(t));

                for (int i = 0; i < fftData.length; i++) {
                    int v2 = (int) (fftData[i]);
                    gc.setFill(Color.rgb((Math.min(v2, 255)), (v2 > 255 ? Math.min(v2 - 255, 255) : 0), (v2 > 255 * 2 ? Math.min(v2 - 255 * 2, 255) : 0)));
                    gc.fillRect(x, i * ys, x , (i + 1) * ys);

                    //gc.setLineWidth(4);
                    //gc.setStroke(Color.rgb((Math.min(v2, 255)), (v2 > 255 ? Math.min(v2 - 255, 255) : 0), (v2 > 255 * 2 ? Math.min(v2 - 255 * 2, 255) : 0)));
                    //gc.strokeLine(x, i * ys, x, i * ys);
                }
                int pixS = 0;
                if (audioPixMS < 0.5) {
                    pixS = 10;
                } else if (audioPixMS < 1) {
                    pixS = 50;
                } else if (audioPixMS < 2) {
                    pixS = 100;
                } else if (audioPixMS < 10) {
                    pixS = 500;
                } else if (audioPixMS < 20) {
                    pixS = 1000;
                } else if (audioPixMS < 40) {
                    pixS = 5000;
                }
                if (t % pixS < audioPixMS && drawText != t) {
                    drawText = t;
                    gc.setLineWidth(4);
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(x - 3, 0, x - 3, h);


                    gc.setTextAlign(TextAlignment.RIGHT);
                    gc.setFont(Font.font(16));
                    gc.setFill(Color.GREEN);
                    gc.fillText("" + t, x - 4, 100);
                }

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
