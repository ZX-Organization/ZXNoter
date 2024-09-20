package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.IAudioProcessor;

/**
 * 增益处理器
 */
public class GainProcessor implements IAudioProcessor {
    private float gain = 1f;

    public GainProcessor() {
    }

    public GainProcessor(float gain) {
        this.gain = gain;
    }

    /**
     * 获取增益值
     *
     * @return 增益值
     */
    public float getGain() {
        return gain;
    }

    /**
     * 设置增益
     *
     * @param gain 增益值 默认1f
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    @Override
    public void process(float[] samples,int channels,int sampleRate) {
        for (int i = 0; i < samples.length; i++) {
            samples[i] *= gain;
        }
    }
}