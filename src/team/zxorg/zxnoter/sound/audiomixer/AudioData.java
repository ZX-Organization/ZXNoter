package team.zxorg.zxnoter.sound.audiomixer;

/**
 * 静态音频数据容器 (只读)
 * 相当于 Unity 的 AudioClip 或资源的 Handle
 */
public class AudioData {
    public final float[] samples;
    public final int sampleRate;
    public final int channels;
    public final long totalLengthMs;

    public AudioData(float[] samples, int sampleRate, int channels) {
        this.samples = samples;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.totalLengthMs = (long) ((samples.length / (double) channels / sampleRate) * 1000.0);
    }
}