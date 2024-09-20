package team.zxorg.audiosystem;

import team.zxorg.audiosystem.fun.IAudioProcessor;

import java.util.Arrays;

public class SonicProcessor implements IAudioProcessor {
    private final float[] buffer = new float[2048];
    Sonic sonic;
    IAudioProcessor audioProcessor;

    public SonicProcessor(IAudioProcessor audioProcessor) {
        this.audioProcessor = audioProcessor;

        //sonic.writeFloatToStream(, );
    }

    @Override
    public void process(float[] samples, int channels, int sampleRate) {
        if (sonic == null) {
            sonic = new Sonic(sampleRate, channels);
            sonic.setQuality(1);
        }

        //写入sonic
        while (samples.length > sonic.samplesAvailable() * channels) {
            Arrays.fill(buffer, 0);
            audioProcessor.process(buffer, channels, sampleRate);
            sonic.writeFloatToStream(buffer, buffer.length / channels);
        }

        //读取sonic
        sonic.readFloatFromStream(samples, samples.length);

    }

    /**
     * 设置播放速度
     *
     * @param v         1f为原速
     * @param transpose 变调
     */
    public void setSpeed(double v, boolean transpose) {
        sonic.setRate(transpose ? v : 1f);
        sonic.setSpeed(transpose ? 1f : v);
    }

    private void update() {
        //sonic.writeFloatToStream(, );
    }

}
