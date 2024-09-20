package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.IAudioProcessor;
import team.zxorg.extensionloader.core.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;

import static team.zxorg.audiosystem.AudioConvert.convertFloatToShortByte;

public class AudioOutputLine {
    IAudioProcessor audioProcessor;
    SourceDataLine line;
    float[] buffer;
    byte[] lineBuffer;
    int channels;
    int sampleRate;
    private final Thread AudioOutputThread = new Thread("AudioOutputLineThread") {
        @Override
        public void run() {
            while (!isInterrupted()) {
                //清除缓冲区
                Arrays.fill(buffer, 0f);

                //处理
                if (audioProcessor != null)
                    audioProcessor.process(buffer, channels, sampleRate);


                convertFloatToShortByte(buffer, lineBuffer);
                //写入音频线
                line.write(lineBuffer, 0, lineBuffer.length);
            }
        }
    };


    public void setAudioProcessor(IAudioProcessor audioProcessor) {
        this.audioProcessor = audioProcessor;
    }


    public void open(int sampleRate, int channels, int bufferSize) throws LineUnavailableException {
        this.sampleRate = sampleRate;
        this.channels = channels;
        bufferSize = bufferSize * channels;//所有通道的缓冲区大小
        buffer = new float[bufferSize];
        lineBuffer = new byte[buffer.length * 2];//音频线的字节缓冲区 short格式
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, channels, channels * 2, sampleRate, false);

        //释放原音频线
        if (line != null) {
            line.stop();
            line.close();
        }

        //使用系统默认源数据线路
        line = AudioSystem.getSourceDataLine(format);

        //启动线路
        line.open(format, lineBuffer.length);
        line.start();

        //启动线程
        if (!AudioOutputThread.isAlive())
            AudioOutputThread.start();

    }

    /**
     * 关闭
     */
    public void close() {
        if (AudioOutputThread.isAlive())
            AudioOutputThread.interrupt();
        Logger.info("AudioMixer closed");
    }
}
