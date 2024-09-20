package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.AudioStream;
import team.zxorg.audiosystem.fun.IAudioProcessor;
import team.zxorg.audiosystem.fun.IPlayTime;

public class AudioPlayProcessor implements IAudioProcessor, IPlayTime {
    private final AudioStream audioStream;
    private int position = 0;

    public AudioPlayProcessor(AudioStream audioStream) {
        this.audioStream = audioStream;
    }

    @Override
    public void process(float[] samples, int channels, int sampleRate) {
        int length = samples.length;
        int bytesRead = audioStream.read(samples, position, length);
        if (bytesRead < length) {
            // 处理缓冲区未满的情况
            position = 0;
        } else {
            position += bytesRead / channels;
        }
    }

    @Override
    public long getTime() {
        return position * 1000L / audioStream.getSampleRate();
    }

    @Override
    public void setTime(long time) {
        position = (int) (time * audioStream.getSampleRate() / 1000);
    }

    @Override
    public long getDuration() {
        return audioStream.getDuration();
    }
}
