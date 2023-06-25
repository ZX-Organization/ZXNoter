package team.zxorg.zxnoter.audiomixer;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AudioUtils {

    public static byte[] reSampling(byte[] data, int targetSampleRate) throws IOException, UnsupportedAudioFileException {

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));

        AudioFormat srcFormat = audioIn.getFormat();

        AudioFormat dstFormat = new AudioFormat(srcFormat.getEncoding(),
                targetSampleRate,
                srcFormat.getSampleSizeInBits(),
                srcFormat.getChannels(),
                srcFormat.getFrameSize(),
                srcFormat.getFrameRate(),
                srcFormat.isBigEndian());


        AudioInputStream convertedIn = AudioSystem.getAudioInputStream(dstFormat, audioIn);

        int numReads = -1;

        int BUFF_SIZE = targetSampleRate / 2;

        byte[] buff = new byte[BUFF_SIZE];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((numReads = convertedIn.read(buff)) != -1) {
            outputStream.write(buff);
        }
        return outputStream.toByteArray();
    }


    public static byte[] reSamplingFileBytes(byte[] data, int targetSampleRate) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));

        AudioFormat srcFormat = audioIn.getFormat();

        AudioFormat dstFormat = new AudioFormat(srcFormat.getEncoding(),
                targetSampleRate,
                srcFormat.getSampleSizeInBits(),
                srcFormat.getChannels(),
                srcFormat.getFrameSize(),
                srcFormat.getFrameRate(),
                srcFormat.isBigEndian());

        AudioInputStream convertedIn = AudioSystem.getAudioInputStream(dstFormat, audioIn);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AudioSystem.write(convertedIn, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

}
