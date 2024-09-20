package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.IAudioProcessor;
import team.zxorg.audiosystem.fun.IPlayController;
import team.zxorg.audiosystem.fun.IPlayTime;

public class BufferedAudioPlayer implements IAudioProcessor, IPlayTime {
    // 音频缓冲区
    private final float[] buffer;
    // 当前帧位置
    private int position = 0;
    // 采样率
    private int sampleRate;
    // 通道数
    private int channels;


    public BufferedAudioPlayer(float[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public void process(float[] samples, int channels, int sampleRate) {
        // 缓存采样率和通道数，避免在循环内重复设置
        this.channels = channels;
        this.sampleRate = sampleRate;

        int bufferLength = buffer.length;  // 缓存缓冲区长度
        int positionInBuffer = position * channels;

        for (int i = 0; i < samples.length; i++) {
            // 循环处理样本
            samples[i] += buffer[positionInBuffer + i];

            // 如果到达缓冲区末尾，重置位置并退出处理
            if (positionInBuffer + i >= bufferLength) {
                position = 0;
                return;
            }
        }

        // 更新当前位置
        position += samples.length / channels;
    }

    @Override
    public long getTime() {
        return position * 1000L / sampleRate;
    }

    @Override
    public void setTime(long time) {
        position = (int) (time * sampleRate / 1000);
        // 限制 position 在有效范围内
        if (position < 0) {
            position = 0;
        } else if (position >= buffer.length / channels) {
            position = buffer.length / channels - 1;
        }
    }

    @Override
    public long getDuration() {
        return buffer.length * 1000L / ((long) channels * sampleRate);
    }

}
