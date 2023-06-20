package team.zxorg.zxnoter.audiochannel;

import top.zedo.audiochannel.channel.AudioInputChannel;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;

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
        /*//将所有通道电平混合
        for (AudioChannel channel : mixerChannels) {
            channel.read(mixerBuffer, true);
        }
        mixerHandler.handleSample(readMixerBuffer());//处理电平
        Arrays.fill(mixerBuffer, 0);*/
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
