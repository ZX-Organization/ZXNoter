package team.zxorg.mapeditcore.io.msc;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

public class WavSource {
    /**
     * 数据格式化
     */
    DecimalFormat decimalFormat = new DecimalFormat("#.##%");
    /**
     * 音频格式
     */
    AudioFormat audioFormat;


    long audioLength;



    /**
     * 计算音频时长（ms）
     */
    long calculateTime(File audioFile, AudioFormat format) {
        //文件总大小（bytes）long fSize = audioFile.length();
        //采样率float sampleRate = format.getSampleRate();
        //通道数int channels = format.getChannels();
        //位深度int bitDepth = format.getSampleSizeInBits();
        //每秒大小（bits）double bitsPerMilliSecond = sampleRate*bitDepth*channels/1000;
        return (long) ((audioFile.length() * 8) / (format.getSampleRate() * format.getSampleSizeInBits() * format.getChannels() / 1000));
    }

    public PlayThread newPlayThread(File audioFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //音频输入流
        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);

        long fileSize = (int) audioFile.length();
        //初始化buffer
        ByteBuffer musicBuffer = ByteBuffer.allocate((int) fileSize);
        musicBuffer.order(ByteOrder.LITTLE_ENDIAN);
        //初始化音频格式
        audioFormat = ais.getFormat();

        //获取受数据行支持的音频格式DataLine.Info
        //DataLine.Info datalineInfo = new DataLine.Info(SourceDataLine.class,audioFormat);
        //获取源数据行
        SourceDataLine dataLine = AudioSystem.getSourceDataLine(audioFormat);

        //计算音频时长（ms）
        audioLength = calculateTime(audioFile, audioFormat);

        //1m
        byte[] readTemp = new byte[3*1024 * 1024];
        int readLength;
//        int sum = 0;
        while ((readLength = ais.read(readTemp)) != -1) {
//            sum += readLength;
            musicBuffer.put(readTemp, 0, readLength);
//            System.out.println(decimalFormat.format((float) (sum) / fileSize));
        }
        dataLine.open();
        dataLine.start();

        PlayThread playThread = new PlayThread(dataLine,musicBuffer);
        playThread.start();

        return playThread;
    }
}
