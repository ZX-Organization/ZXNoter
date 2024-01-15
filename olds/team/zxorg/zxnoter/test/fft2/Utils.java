package team.zxorg.zxnoter.test.fft2;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Utils {
    public static AudioInputStream sampleRateConvert(byte[] data, float destSampleRate) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean bigEndian = false;

        AudioFormat newFormat = new AudioFormat(destSampleRate, sampleSizeInBits, channels, true, bigEndian);
        return AudioSystem.getAudioInputStream(newFormat, audioStream);
    }
}
