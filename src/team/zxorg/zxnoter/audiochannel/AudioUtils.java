package team.zxorg.zxnoter.audiochannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioUtils {
    /**
     * 音频流转换到目标格式
     *
     * @param audioInputStream 原始音频数据
     * @param destSampleRate   要转换的码率
     * @return 处理后的音频数据
     */
    public static AudioInputStream audioStreamConvert(AudioInputStream audioInputStream, float destSampleRate, int destSampleSizeInBits, int destChannels) {
        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, destSampleRate, destSampleSizeInBits, destChannels, destSampleSizeInBits / 8 * destChannels, destSampleRate, false);
        return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }
}
