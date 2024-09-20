package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.AudioStream;

public class BufferedAudioStream implements AudioStream {
    private final int sampleRate;
    private final int channels;
    private final float[] audioData;

    public BufferedAudioStream(float[] audioData, int sampleRate, int channels) {
        this.audioData = audioData;
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    @Override
    public int read(float[] buffer, int offset, int length) {
        // 从音频数据中读取 length 个样本到 buffer 中
        System.arraycopy(audioData, offset, buffer, 0, length);
        return length;
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public int getChannels() {
        return channels;
    }

    @Override
    public long getDuration() {
        return (audioData.length / channels) * 1000L / sampleRate;
    }
}
