package audiochannel.audiomixer_t;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class AudioMixer {
    private final ArrayList<byte[]> audioDataList = new ArrayList<>();//音频数据表

    private final SourceDataLine line;//播放数据总线

    /**
     * 采样率转换
     *
     * @param data           原始音频数据
     * @param destSampleRate 要转换的码率
     * @return 处理后的音频数据
     */
    public static AudioInputStream sampleRateConvert(byte[] data, float destSampleRate) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean bigEndian = false;

        AudioFormat newFormat = new AudioFormat(destSampleRate, sampleSizeInBits, channels, true, bigEndian);
        return AudioSystem.getAudioInputStream(newFormat, audioStream);
    }

    /**
     * 音频混音器对象
     *
     * @param sampleRate   码率
     * @param mixerBufSize 音频缓冲区大小 必须是2的倍数
     */
    public AudioMixer(float sampleRate, int mixerBufSize) throws LineUnavailableException {

        line = AudioSystem.getSourceDataLine(new AudioFormat(sampleRate, 16, 2, true, false));
        //缓冲区大小
        line.open();
        line.start();

        Thread mixerPlayThread = new Thread(() -> {

        }, "AudioMixerThread");
        mixerPlayThread.start();

    }

    private void writeToLine(short[] buf) {
        byte[] pcmBuf = new byte[buf.length * 2];
        for (int i = 0; i < buf.length; i++) {
            System.arraycopy(BytesUtils.short2bytesA(buf[i]), 0, pcmBuf, i * 2, 2);
        }
        line.write(pcmBuf, 0, pcmBuf.length);
    }


    /**
     * 打开音频
     *
     * @param audioFile 音频文件 wav
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(Path audioFile) throws IOException {
        return addAudio(Files.readAllBytes(audioFile));
    }

    /**
     * 打开音频
     *
     * @param audioData 音频数据 wav
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(byte[] audioData) {

        if (!audioDataList.contains(audioData))
            audioDataList.add(audioData);
        return audioDataList.indexOf(audioData);
    }


    /**
     * 关闭音频
     *
     * @param handle 音频句柄
     */
    public void removeAudio(int handle) {
        audioDataList.set(handle, null);
    }

    /**
     * 创建音频频道
     *
     * @param audioHandle 音频句柄
     */
    /*public AudioChannel createChannel(int audioHandle) throws UnsupportedAudioFileException, IOException {

        synchronized (this) {
            AudioChannel newChannel = new AudioChannel(sampleRateConvert(audioDataList.get(audioHandle), (int) line.getFormat().getSampleRate()));
            audioChannelList.add(newChannel);
            return newChannel;
        }

    }*/

    /**
     * 移除音频通道
     *
     * @param channel 要移除的通道
     */
    /*public void removeChannel(AudioChannel channel) {
        synchronized (this) {
            audioChannelList.remove(channel);
        }
    }*/


}
