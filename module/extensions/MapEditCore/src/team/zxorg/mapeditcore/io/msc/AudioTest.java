package team.zxorg.mapeditcore.io.msc;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioTest {

    public static void main(String[] args) {
        //音频文件
        File audioFile = new File("docs/Music/(Angelfish)エンゼルフィッシュ（extend version）-明透.wav");
        try {
            WavPlayer player = new WavPlayer(audioFile);
            player.setTime(160*1000);
            player.setVolume(-2f);
            player.start();

            for (int i = 0; i < 2; i++) {
                Thread.sleep(5000);
                player.pause();
                Thread.sleep(2000);
                player.start();
            }
            player.stop();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*try {
            //音频输入流
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
            //获取音频流格式
            AudioFormat audioFormat = ais.getFormat();
            //System.out.println(audioFormat);
            //PCM_SIGNED 44100.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian
            //获取受数据行支持的音频格式DataLine.Info
            DataLine.Info datalineInfo = new DataLine.Info(SourceDataLine.class,audioFormat);
            //获取源数据行
            SourceDataLine dataLine = AudioSystem.getSourceDataLine(audioFormat);
            //SourceDataLine dataLine = AudioSystem.getLine(datalineInfo);
            //打开数据行，可以获得资源和操作
            dataLine.open();
            //允许io操作
            dataLine.start();

            //在此往dataline写入数据来播放音频
            byte[] temp = new byte[256];
            int length;
//            System.out.println(calculateTime(audioFile,audioFormat));
            *//*while ((length=ais.read(temp)) != -1){
                dataLine.write(temp,0,length);
            }*//*
            dataLine.stop();
            dataLine.close();


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }*/
    }

}
