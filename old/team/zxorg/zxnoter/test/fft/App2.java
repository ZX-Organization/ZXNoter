package team.zxorg.zxncore.test.fft;

import team.zxorg.zxncore.utils.FxUtils;
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

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class App2 extends Application {
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
            pane.setBackground(Background.fill(Color.WHITE));
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


        TargetDataLine targetDataLine;
        AudioFormat audioFormat = new AudioFormat(48000, 16, 1, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        /*{
            int i = 0;
            for (Mixer.Info info : mixerInfos) {
                System.out.println(i++ + " " + info.getName());
            }
        }*/

        Mixer mixer = AudioSystem.getMixer(mixerInfos[30]);
        targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();

        byte[] buffer = new byte[(int) (1024)];
        Image image = new Image(App2.class.getResource("Line.png").toString());

        ByteBuffer buf = ByteBuffer.wrap(buffer);
        buf.order(ByteOrder.LITTLE_ENDIAN);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();


        AudioV[] audioBuf = new AudioV[1800];
        for (int i = 0; i < 1800; i++) {
            audioBuf[i] = new AudioV();
        }

        new Thread(() -> {
            while (true) {
                targetDataLine.read(buffer, 0, buffer.length);
                System.arraycopy(audioBuf,0,audioBuf,4,1800-4);


                Platform.runLater(() -> {
                    //pos += 1000;//音频偏移
                    double w = canvas.getWidth();
                    double h = canvas.getHeight();

                    //gc.drawImage(canvas.snapshot(null,null), buffer.length / S / 2, 0);


                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    //gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());

                    /*for (int i = 0; i < 1800 / 4 - 1; i++) {
                        for (int j = 0; j < 4; j++) {
                            audioBuf[(i + 1) * 4 + j] = audioBuf[i * 4 + j];
                        }
                    }*/


                    double lastV = 0;
                    for (int x = 0; x < buffer.length / S / 2; x++) {


                        buf.position(x * S * 2);
                        AudioV av = new AudioV();
                        for (int i = 0; i < S; i++) {
                            double v = buf.getShort() / 80.;

                            av.t = Math.max(av.t, v);
                            av.b = Math.min(av.b, v);
                            //v = Math.max(v, 1);
                        }
                        //gc.drawImage(image, x, h / 2 - av.t, 1, av.t - av.b);

                        audioBuf[x] = av;
                    }


                    for (int i = 0; i < 1800; i++) {
                        gc.drawImage(image, i, h / 2 - audioBuf[i].t, 1, audioBuf[i].t - audioBuf[i].b);
                    }
                });

            /*v = h / 2. - v;
            gc.setStroke(Color.rgb(0, 100, 0));
            gc.strokeLine(x - 1, lastV, x, v);
            lastV = v;*/


        /*if (x % 6 == 0) {


            buf.position(0);
            for (int i = 0; i < N; i++) {
                component[i] = new Complex(buf.getShort() / 80., 0);
            }

            //傅里叶变换计算
            component = FFT.getFFT(component, N);//傅里叶变换
            Double[] x2 = Complex.toModArray(component);//计算傅里叶变换得到的复数数组的模值
            for (int i = 0; i < N / 2; i++) {
                x2[N / 2 + i] = x2[N / 2 + i] / N * 3;
                int c = (int) (x2[N / 2 + i] * 20);
                gc2.setLineWidth(6);
                gc2.setStroke(Color.rgb((Math.min(c, 255)), (c > 255 ? Math.min(c - 255, 255) : 0), (int) ((Math.min(c, 255)) * 0.3)));
                gc2.strokeLine(x, i, x, i);
            }


        }*/



            }
        }).start();


        // 创建一个Timeline对象，每秒执行30次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000. / 40), e -> {

        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //设置无限循环
        timeline.play(); //开始播放

    }


}
