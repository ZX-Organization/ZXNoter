package team.zxorg.extensionloader.test.fft;

import team.zxorg.extensionloader.test.sound.audiomixer.AudioChannel;
import team.zxorg.extensionloader.test.sound.audiomixer.AudioMixer;
import team.zxorg.extensionloader.utils.FxUtils;
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
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class App extends Application {
    Stage stage;
    Canvas canvas = new Canvas();
    Canvas canvas2 = new Canvas();
    Timeline timeline;
    int pos = 0;//音频的显示位置

    int N = 512;
    int S = 200;
    Complex[] data = new Complex[N];
    AudioMixer audioMixer = new AudioMixer(44100, 64);

    public App() throws LineUnavailableException {
    }

    AudioChannel audioChannel;
    double maxAudioF = 0;

    double lastAudioF = 0;

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
        URL audioURL = new File("testresources/ゆびきり_drums_01.wav").toURL();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioURL);

        int audioLength = audioInputStream.available();
        double audioSampleRate = audioInputStream.getFormat().getSampleRate();


        long audioSize = audioInputStream.available();
        audioChannel = audioMixer.createChannel(audioMixer.addAudio(Path.of(audioURL.toURI())));
        audioChannel.setVolume(0.1f);


        Image image = new Image("file:///testresources/Line.png");


        ByteBuffer buf = ByteBuffer.wrap(audioInputStream.readAllBytes());
        buf.order(ByteOrder.LITTLE_ENDIAN);


        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        //audioChannel.play();


        canvas.setOnMouseClicked(event -> {
            if (audioChannel.getPlayState() == AudioChannel.PlayState.PLAY)
                audioChannel.pause();
            else
                audioChannel.play();
        });


        // 创建一个Timeline对象，每秒执行30次指定的操作
        timeline = new Timeline(new KeyFrame(Duration.millis(1000. / 10), e -> {

            //pos = (int) (1d * audioChannel.getTime() / audioChannel.getAudioLength() * audioSize);//音频偏移

            double w = canvas.getWidth();
            double h = canvas.getHeight();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());


            double lastV = 0;

            int lastT = 0;
            int lastF = 0;
            for (int x = 0; x < w; x++) {
                buf.position((pos + x * S) * 2);

                if (x % 400 == 0) {
                    gc.setTextAlign(TextAlignment.LEFT);
                    gc.setFont(Font.font(16));
                    gc.setFill(Color.GREEN);
                    gc.fillText("" + (int) ((buf.position() / 2 * 1000) / audioSampleRate), x, 100);
                }


                for (int i = 0; i < S; i++) {
                    double v = buf.getShort() / 200.;

                    //v = Math.max(v, 1);
                    gc.drawImage(image, x, (v < 0 ? h / 2 : h / 2 - v), 1, Math.abs(v));
                    /*v = h / 2. - v;
                    gc.setStroke(Color.rgb(0, 100, 0));
                    gc.strokeLine(x - 1, lastV, x, v);
                    lastV = v;*/
                }

                /*if (x % 4 == 0)*/
                {


                    buf.position((pos + x * S) * 2);
                    for (int i = 0; i < N; i++) {
                        data[i] = new Complex(buf.getShort() / 80., 0);
                    }

                    //傅里叶变换计算
                    int v2 = 0;
                    data = FFT.getFFT(data, N);//傅里叶变换
                    Double[] x2 = Complex.toModArray(data);//计算傅里叶变换得到的复数数组的模值
                    for (int i = 0; i < N / 2; i++) {
                        x2[N / 2 + i] = x2[N / 2 + i] / N * 3;
                        int c = (int) (x2[N / 2 + i] * 20);
                        gc2.setLineWidth(6);
                        gc2.setStroke(Color.rgb((Math.min(c, 255)), (c > 255 ? Math.min(c - 255, 255) : 0), (int) ((Math.min(c, 255)) * 0.3)));
                        gc2.strokeLine(x, i, x, i);
                        if (i > N / 4)
                            v2 += c;
                    }
                    v2 /= 45;


                    maxAudioF = Math.max(maxAudioF, v2);
                    //gc2.setLineWidth(6);
                    gc2.setLineWidth(1);
                    gc2.setStroke(Color.rgb((Math.min(v2, 255)), (v2 > 255 ? Math.min(v2 - 255, 255) : 0), (v2 > 255 * 2 ? Math.min(v2 - 255 * 2, 255) : 0)));
                    gc2.strokeLine(x, 280, x, 280);
                    buf.position((pos + x * S) * 2);
//&& lastF < x - 40 * 2
                    int 误差 = 0;
                    int fCount = 0;
                    int latestBpm = 0;

                    for (int c = 1200; c > 500; c -= 10) {


                        if (v2 > c && lastF < x - 40  /*v2 - lastAudioF > 60 && lastF < x - 100*/) {
                            lastF = x;
                            gc2.setTextAlign(TextAlignment.LEFT);
                            gc2.setFont(Font.font(12));
                            gc2.setFill(Color.GREEN);
                            //"" + (int) ((buf.position() / 2 * 1000) / audioSampleRate) +

                            fCount++;
                            int bpm = (int) Math.round(60000 / (((buf.position() / 2 * 1000) / audioSampleRate) - lastT));

                            误差 += Math.abs(latestBpm - bpm);

                            latestBpm = bpm;

                            gc2.fillText(" " + latestBpm, x + 10, 296);
                            gc2.setLineWidth(2);
                            gc2.strokeLine(x, 280, x, 290);
                            lastT = (int) ((buf.position() / 2 * 1000) / audioSampleRate);
                        }


                        System.out.println(误差);
                        //if (误差 < 40 && fCount > 4)
                            //break;
                        误差 = 0;
                        fCount = 0;

                    }
                    lastAudioF = v2;
                }

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); //设置无限循环
        timeline.play(); //开始播放

    }


}
