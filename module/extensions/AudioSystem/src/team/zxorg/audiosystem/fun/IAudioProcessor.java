package team.zxorg.audiosystem.fun;

/**
 * 音频处理器接口
 */
public interface IAudioProcessor {

    /**
     * 处理
     *
     * @param samples    采样缓冲区
     * @param channels   通道数
     * @param sampleRate 采样率
     */
    void process(float[] samples, int channels, int sampleRate);

}