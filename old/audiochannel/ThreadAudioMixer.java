package team.zxorg.zxnoter.audiochannel;


import team.zxorg.zxnoter.audiochannel.channel.AudioInputChannel;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ThreadAudioMixer extends AudioMixer {
    public interface AudioMixerHandler {
        void handleSample(int[] buf);
    }

    ArrayList<AudioInputChannel> mixerChannels = new ArrayList<>();
    AudioMixerHandler mixerHandler;//混音处理

    Thread mixerThread = new Thread(() -> {
        while (true) {
            mixer();
        }
    });

    public ThreadAudioMixer(AudioFormat audioFormat, int bufferSize, AudioMixerHandler mixerHandler) {
        super(audioFormat, bufferSize);
        this.mixerHandler = mixerHandler;
    }

    public void addAudioChannel(AudioInputChannel channel){
        mixerChannels.add(channel);
    }

    @Override
    void mixer() {
        //将所有通道电平混合
        for (AudioInputChannel channel : mixerChannels) {
            channel.read(mixerByteBuffer, true);
        }
        mixerHandler.handleSample(mixerByteBuffer);//处理电平
        Arrays.fill(mixerByteBuffer, 0);
    }


    public void start() {
        if (!mixerThread.isAlive())
            mixerThread.start();
    }

    @Override
    public void close() throws IOException {
        if (mixerThread.isAlive())
            mixerThread.interrupt();
    }
}
