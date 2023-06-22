package audiochannel.audiomixer_t.channel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 音频句柄
 */
public abstract class AudioSampleInputStream {
    protected final float sampleRate;//采样率
    protected final int channels;//声道数
    protected final int frameSize;//帧大小
    protected final AudioBitDepth audioBitDepth;//采样类型
    protected ByteBuffer inputStreamBuffer;//帧缓冲区
    protected int frameBufferSize = 4096;//缓冲区帧数量


    protected AudioSampleInputStream(float sampleRate, int channels, AudioBitDepth audioBitDepth, boolean isLittleEndian) {
        this.audioBitDepth = audioBitDepth;
        this.sampleRate = sampleRate;
        this.channels = channels;
        frameSize = channels * audioBitDepth.getByteLength();
        init(isLittleEndian);
    }

    protected AudioSampleInputStream(float sampleRate, int channels, AudioBitDepth audioBitDepth, boolean isLittleEndian, int frameBufferSize) {
        this.audioBitDepth = audioBitDepth;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.frameBufferSize = frameBufferSize;
        frameSize = channels * audioBitDepth.getByteLength();
        init(isLittleEndian);
    }

    /**
     * 初始化
     */
    private void init(boolean isLittleEndian) {
        inputStreamBuffer = ByteBuffer.allocate(frameSize * frameBufferSize);
        inputStreamBuffer.order((isLittleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN));
        inputStreamBuffer.position(inputStreamBuffer.capacity());
    }

    /**
     * 填充frameBuffer缓冲区
     */
    abstract void fillFrameBuffer();

    /**
     * 处理采样
     *
     * @param sample        原始采样值
     * @param audioBitDepth 采样位深
     * @return 处理后的采样值
     */
    protected Number handleSample(Number sample, AudioBitDepth audioBitDepth, int bufferPosition) {
        return sample;
    }

    /**
     * 处理帧
     *
     * @return 处理后的采样值
     */
    protected void handleFrame(int bufferFramePosition) {

    }

    /**
     * 从帧缓冲区读取一个原始采样
     *
     * @return 一个采样
     */
    public Number readSample() {
        return readSample(audioBitDepth);
    }

    /**
     * 从帧缓冲区读取一个指定深度的采样
     *
     * @return 一个采样
     */
    public Number readSample(AudioBitDepth toAudioBitDepth) {
        //System.out.println("容量:"+frameBuffer.remaining());
        //填充帧缓冲区
        if (inputStreamBuffer.remaining() == 0) {
            fillFrameBuffer();
        }
        if (inputStreamBuffer.position() % frameSize == 0)
            handleFrame(inputStreamBuffer.position() / frameSize);
        return handleSample(audioBitDepth.getSample(inputStreamBuffer, toAudioBitDepth), toAudioBitDepth, inputStreamBuffer.position());
    }

    /**
     * 从帧缓冲区读取一个值范围为0.0-1.0的浮点采样
     *
     * @return 一个采样
     */
    public float readFloatSample() {
        int v = readSample(AudioBitDepth.INTEGER_32_BIT).intValue();
        return (float) v / Integer.MAX_VALUE;
    }

    /**
     * 读取采样
     *
     * @return 采样数组 1为单声道 2为双声道
     */
    public void readSample(Number[] buffer, int len) {
        for (int l = 0; l < len; l++) {
            for (int p = 0; p < channels; p++) {
                buffer[l * channels + p] = readSample();
            }
        }
    }

}
