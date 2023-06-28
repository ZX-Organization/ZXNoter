package team.zxorg.zxnoter.audiochannel.channel;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

public abstract class AudioInputChannel {
    public ByteBuffer sampleBuffer;//采样缓冲区
    public AudioFormat audioFormat;//音频格式
    public boolean isPaused = false;//暂停状态

    public AudioInputChannel(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    abstract boolean fillSampleBuffer();//填充帧缓冲区

    public void read(int[] data, boolean isAddUp) {
        read(data, isAddUp, 1f);
    }

    public void read(int[] data, boolean isAddUp, float multiplier) {
        if (isPaused)
            return;
        int sample;
        for (int i = 0; i < data.length; i++) {
            if (!sampleBuffer.hasRemaining()) {
                if (!fillSampleBuffer()) {//填充缓冲区
                    isPaused = true;
                    return;
                }
            }
            sample = getIntSample(sampleBuffer, audioFormat.getSampleSizeInBits());
            if (isAddUp)//判断是否叠加
                data[i] += sample * multiplier;
            else
                data[i] = (int) (sample * multiplier);
        }
    }

    public int getIntSample(ByteBuffer buffer, int audioBitDepth) {
        if (audioBitDepth == 8)
            return (int) buffer.get() << 24;
        else if (audioBitDepth == 16)
            return (int) buffer.getShort() << 16;
        else if (audioBitDepth == 32)
            return buffer.getInt();
        else
            throw new IllegalArgumentException();
    }

    /**
     * 获取当前播放帧位置
     *
     * @return 位置
     */
    public long getFramePosition() {
        return -1;
    }

    /**
     * 设置当前播放帧位置
     *
     * @param position 帧位置
     * @return 设置状态
     */
    public boolean setFramePosition(long position) {
        return false;
    }

}
