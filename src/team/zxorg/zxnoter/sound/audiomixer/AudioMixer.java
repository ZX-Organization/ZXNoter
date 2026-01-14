package team.zxorg.zxnoter.sound.audiomixer;

import javax.sound.sampled.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioMixer {
    private static final Logger logger = Logger.getLogger(AudioMixer.class.getName());

    private final int sampleRate;
    private final int bufferSizeFrames;
    private final SourceDataLine line;
    private final List<AudioChannel> activeChannels = new CopyOnWriteArrayList<>();
    private final Thread mixerThread;
    private volatile boolean running = true;
    private volatile float masterVolume = 1.0f;

    public AudioMixer(int sampleRate, int bufferSizeFrames) throws LineUnavailableException {
        this.sampleRate = sampleRate;
        // 如果用户传入的 bufferSizeFrames 很大 (比如为了安全不卡顿)，
        // 我们在逻辑上接受它，但必须限制硬件层的缓冲区，否则延迟会非常大。
        this.bufferSizeFrames = bufferSizeFrames;

        AudioFormat format = new AudioFormat(sampleRate, 16, 2, true, false);
        line = AudioSystem.getSourceDataLine(format);

        // --- 关键优化：限制硬件缓冲区上限 ---
        // 50ms 是一个很好的基准值
        int desiredHwBufferBytes = (int)(sampleRate * 0.05) * 4;

        // 即使 bufferSizeFrames 很大，我们也不希望硬件缓冲区超过 200ms
        // 17640 bytes @ 44.1k stereo 16bit ~= 100ms
        int maxHwBufferBytes = 35280; // ~200ms

        if (desiredHwBufferBytes < bufferSizeFrames * 4) {
            desiredHwBufferBytes = bufferSizeFrames * 4 * 2;
        }

        // 强制 Clamp，防止 "1秒延迟"
        if (desiredHwBufferBytes > maxHwBufferBytes) {
            desiredHwBufferBytes = maxHwBufferBytes;
            logger.warning("Hardware buffer clamped to " + maxHwBufferBytes + " bytes to prevent high latency.");
        }

        line.open(format, desiredHwBufferBytes);
        line.start();

        logger.info("AudioMixer Init: " + sampleRate + "Hz, HW Buffer=" + desiredHwBufferBytes + " bytes");

        mixerThread = new Thread(this::processMixLoop, "AudioMixer-Core");
        mixerThread.setPriority(Thread.MAX_PRIORITY);
        mixerThread.start();
    }

    private void processMixLoop() {
        // 使用局部较小的处理块，即使 bufferSizeFrames 很大
        // 这样可以更频繁地检查状态，虽然 line.write 仍然是阻塞的
        int processChunkSize = Math.min(bufferSizeFrames, 1024);

        float[] mixBufferL = new float[processChunkSize];
        float[] mixBufferR = new float[processChunkSize];
        float[] chBufferL = new float[processChunkSize];
        float[] chBufferR = new float[processChunkSize];
        byte[] outputBytes = new byte[processChunkSize * 4];

        while (running) {
            java.util.Arrays.fill(mixBufferL, 0.0f);
            java.util.Arrays.fill(mixBufferR, 0.0f);

            int bufferedBytes = line.getBufferSize() - line.available();
            int hardwarePendingFrames = bufferedBytes / 4;

            for (AudioChannel channel : activeChannels) {
                if (channel.getPlayState() == AudioChannel.PlayState.CLOSE) {
                    activeChannels.remove(channel);
                    continue;
                }
                channel.pullAudio(chBufferL, chBufferR, processChunkSize, hardwarePendingFrames);

                for (int i = 0; i < processChunkSize; i++) {
                    mixBufferL[i] += chBufferL[i];
                    mixBufferR[i] += chBufferR[i];
                }
            }

            int byteIdx = 0;
            float vol = masterVolume;
            for (int i = 0; i < processChunkSize; i++) {
                float l = mixBufferL[i] * vol;
                float r = mixBufferR[i] * vol;
                // Hard limiter
                if (l < -1.0f) l = -1.0f; else if (l > 1.0f) l = 1.0f;
                if (r < -1.0f) r = -1.0f; else if (r > 1.0f) r = 1.0f;

                short sl = (short) (l * 32767);
                short sr = (short) (r * 32767);
                outputBytes[byteIdx++] = (byte) (sl & 0xFF);
                outputBytes[byteIdx++] = (byte) ((sl >> 8) & 0xFF);
                outputBytes[byteIdx++] = (byte) (sr & 0xFF);
                outputBytes[byteIdx++] = (byte) ((sr >> 8) & 0xFF);
            }

            line.write(outputBytes, 0, outputBytes.length);
        }
        line.drain();
        line.close();
    }

    // --- 其他方法保持不变 ---
    public AudioData loadAudio(Path audioFile) {
        try {
            float[] samples = FFmpeg.read(audioFile, sampleRate);
            if (samples == null || samples.length == 0) return null;
            return new AudioData(samples, sampleRate, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AudioChannel createChannel(AudioData data) {
        if (data == null) return null;
        AudioChannel ch = new AudioChannel(data);
        activeChannels.add(ch);
        return ch;
    }

    public void removeChannel(AudioChannel channel) {
        if (channel != null) {
            channel.close();
            activeChannels.remove(channel);
        }
    }

    public void stopAllChannels() {
        for (AudioChannel ch : activeChannels) ch.stop();
    }

    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(2.0f, volume));
    }
    public float getMasterVolume() { return masterVolume; }
    public int getSampleRate() { return sampleRate; }
    public void close() {
        running = false;
        try { mixerThread.join(1000); } catch (InterruptedException e) {}
    }
}