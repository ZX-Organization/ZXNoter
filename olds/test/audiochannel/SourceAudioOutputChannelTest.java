package audiochannel;


import team.zxorg.zxnoter.audiochannel.Convolver;
import team.zxorg.zxnoter.audiochannel.channel.MemoryAudioInputChannel;
import team.zxorg.zxnoter.audiochannel.channel.SourceAudioOutputChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SourceAudioOutputChannelTest {
    static int[] processedSamples;

    public static void main(String[] args) throws Exception {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);
        MemoryAudioInputChannel memoryAudioChannel = new MemoryAudioInputChannel(audioFormat, Paths.get("testaudio/ゆびきり-1c-16I.wav"));

        int[] samples = new int[81920];
        SourceAudioOutputChannel sourceAudioOutputChannel = new SourceAudioOutputChannel(audioFormat, samples.length);
        sourceAudioOutputChannel.open();
        long lastSamples = 1862616;
        memoryAudioChannel.setFramePosition(lastSamples);


        // 加载混响脉冲响应
        AudioInputStream impulseResponseStream = AudioSystem.getAudioInputStream(new File("testaudio/Convolver.wav"));

        // 创建混响效果的卷积器
        Convolver convolver = new Convolver(audioFormat, impulseResponseStream);
        processedSamples = new int[samples.length];


        Object playLock = new Object();
        Thread playThread = new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (playLock){
                playLock.notify();
            }

            while (true) {

                synchronized (playLock) {
                    try {
                        playLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                int[] processedSamplesf;
                synchronized (playLock) {
                    processedSamplesf = Arrays.copyOf(processedSamples, processedSamples.length);
                }
                sourceAudioOutputChannel.writeSamples(processedSamplesf);
                synchronized (playLock) {
                    playLock.notify();
                }
            }
        });
        playThread.start();


        while (true) {
            Arrays.fill(samples, 0);



            synchronized (playLock) {
                playLock.notify();
            }

            memoryAudioChannel.read(samples, false, 0.5f);

            synchronized (playLock) {
                processedSamples = convolver.process(samples);
            }

            synchronized (playLock) {
                playLock.wait();
            }

        }
    }
}
