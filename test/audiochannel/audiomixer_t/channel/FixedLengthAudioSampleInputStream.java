package audiochannel.audiomixer_t.channel;

public abstract class FixedLengthAudioSampleInputStream extends AudioSampleInputStream {
    protected final long frameLength;//全部帧长度
    protected long inputStreamFrameBufferPosition;//当前缓冲区读取全部数据的位置
    protected long framePosition;//当前帧位置

    protected FixedLengthAudioSampleInputStream(float sampleRate, int channels, long frameLength, AudioBitDepth audioBitDepth) {
        super(sampleRate, channels, audioBitDepth, true);
        this.frameLength = frameLength;
    }

    protected FixedLengthAudioSampleInputStream(float sampleRate, int channels, long frameLength, AudioBitDepth audioBitDepth, int frameBufferSize) {
        super(sampleRate, channels, audioBitDepth, true, frameBufferSize);
        this.frameLength = frameLength;
    }

    /**
     * 获取整个音频的时长
     *
     * @return 时长(ms)
     */
    public long getAudioTimeLength() {
        return Math.round(frameLength / sampleRate * 1000);
    }


    /**
     * 获取现在的音频时长位置
     *
     * @return 时长(ms)
     */
    public long getAudioTimeNow() {
        return Math.round(framePosition / (double) frameLength * getAudioTimeLength());
    }

    /**
     * 帧位置发生变化事件
     *
     * @param framePosition 当前的帧位置
     */
    abstract void framePositionChangeEvent(long framePosition);

    /**
     * 设置当前的帧位置
     *
     * @param position 帧位置
     */
    abstract void setFramePosition(long position);

    /**
     * 设置当前的时间位置
     *
     * @param time 时间(ms)
     */
    public void setTimePosition(long time) {
        setFramePosition(timeToFramePosition(time));
    }

    /**
     * 时间到帧位置
     *
     * @param time 目标时间(ms)
     * @return 帧位置
     */
    public long timeToFramePosition(long time) {
        return Math.round((sampleRate / 1000f) * time);
    }

    @Override
    protected void handleFrame(int bufferFramePosition) {
        //计算精确的帧位置
        framePosition = ((inputStreamFrameBufferPosition - frameBufferSize) + bufferFramePosition);
        framePositionChangeEvent(framePosition);
    }

}
