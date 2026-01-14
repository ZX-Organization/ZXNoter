package team.zxorg.zxnoter.sound.audiomixer;

import java.util.logging.Logger;

/**
 * DSP处理器 - 负责变速、变调等音频处理
 * 关键优化：使用最快的 Sonic 质量设置
 */
public class AudioDSPProcessor {
    private static final Logger logger = Logger.getLogger(AudioDSPProcessor.class.getName());

    private final int sampleRate;
    private final int channels;

    // 主处理器和备用处理器
    private Sonic primarySonic;
    private Sonic secondarySonic;

    // 当前参数
    private volatile float currentSpeed = 1.0f;
    private volatile float currentPitch = 1.0f;
    private volatile float currentRate = 1.0f;

    // 交叉淡化相关
    private volatile boolean isCrossfading = false;
    private volatile int crossfadeFramesRemaining = 0;
    private static final int CROSSFADE_FRAMES = 512;

    private final float[] crossfadeBuffer;
    private final Object processorLock = new Object();

    public AudioDSPProcessor(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;

        this.primarySonic = new Sonic(sampleRate, channels);
        this.secondarySonic = new Sonic(sampleRate, channels);

        // ===== 关键优化：使用质量 0（最快速度）=====
        // Quality 0 = 最快，适合实时播放
        // Quality 1 = 默认平衡
        // Quality 2 = 最高质量但慢
        primarySonic.setQuality(0);
        secondarySonic.setQuality(0);

        logger.info("DSP Processor initialized with Quality=0 (fastest)");

        this.crossfadeBuffer = new float[CROSSFADE_FRAMES * channels];
    }

    public void setSpeed(float speed) {
        if (Math.abs(speed - currentSpeed) < 0.001f) return;

        logger.fine(() -> String.format("DSP Speed Change: %.2f -> %.2f", currentSpeed, speed));

        synchronized (processorLock) {
            if (isCrossfading) {
                finishCrossfade();
            }

            Sonic temp = secondarySonic;
            secondarySonic = primarySonic;
            primarySonic = temp;

            primarySonic.setSpeed(speed);
            primarySonic.setPitch(currentPitch);
            primarySonic.setRate(currentRate);
            primarySonic.setQuality(0); // 确保新处理器也是最快模式

            isCrossfading = true;
            crossfadeFramesRemaining = CROSSFADE_FRAMES;
            currentSpeed = speed;
        }
    }

    public void setPitch(float pitch) {
        if (Math.abs(pitch - currentPitch) < 0.001f) return;
        logger.fine(() -> String.format("DSP Pitch Change: %.2f -> %.2f", currentPitch, pitch));

        synchronized (processorLock) {
            currentPitch = pitch;
            primarySonic.setPitch(pitch);
            if (isCrossfading) {
                secondarySonic.setPitch(pitch);
            }
        }
    }

    public void setRate(float rate) {
        synchronized (processorLock) {
            currentRate = rate;
            primarySonic.setRate(rate);
            if (isCrossfading) {
                secondarySonic.setRate(rate);
            }
        }
    }

    public void writeInput(float[] samples, int frames) {
        synchronized (processorLock) {
            primarySonic.write(samples, frames);

            if (isCrossfading) {
                secondarySonic.write(samples, frames);
            }
        }
    }

    public int readOutput(float[] output, int maxFrames) {
        synchronized (processorLock) {
            if (!isCrossfading) {
                return primarySonic.read(output, maxFrames);
            }

            // 交叉淡化模式
            int framesToRead = Math.min(maxFrames, crossfadeFramesRemaining);

            int primaryFrames = primarySonic.read(output, framesToRead);
            int secondaryFrames = secondarySonic.read(crossfadeBuffer, framesToRead);

            int actualFrames = Math.min(primaryFrames, secondaryFrames);

            // 交叉淡化混合
            for (int i = 0; i < actualFrames; i++) {
                float progress = (float) i / crossfadeFramesRemaining;
                float primaryGain = progress;
                float secondaryGain = 1.0f - progress;

                for (int ch = 0; ch < channels; ch++) {
                    int idx = i * channels + ch;
                    output[idx] = output[idx] * primaryGain +
                            crossfadeBuffer[idx] * secondaryGain;
                }
            }

            crossfadeFramesRemaining -= actualFrames;

            if (crossfadeFramesRemaining <= 0) {
                finishCrossfade();
            }

            return actualFrames;
        }
    }

    private void finishCrossfade() {
        isCrossfading = false;
        crossfadeFramesRemaining = 0;
        secondarySonic.flush();
    }

    public void flush() {
        synchronized (processorLock) {
            primarySonic.flush();
            secondarySonic.flush();
            isCrossfading = false;
            crossfadeFramesRemaining = 0;
        }
    }

    public float getSpeed() { return currentSpeed; }
    public float getPitch() { return currentPitch; }
    public float getRate() { return currentRate; }
    public boolean isCrossfading() { return isCrossfading; }
}