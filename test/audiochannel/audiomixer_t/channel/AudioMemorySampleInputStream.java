package audiochannel.audiomixer_t.channel;

import com.zedo.audiomixer_t.AudioUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Path;

public class AudioMemorySampleInputStream extends FixedLengthAudioSampleInputStream {

    public AudioMemorySampleInputStream(AudioInputStream audioInputStream, float sampleRate, int channels, int destSampleSizeInBits) throws IOException {
        super(sampleRate, channels, audioInputStream.getFrameLength(), AudioBitDepth.get(audioInputStream.getFormat()), (int) (audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameSize()));
        AudioUtils.audioStreamConvert(audioInputStream, sampleRate, destSampleSizeInBits, channels).read(inputStreamBuffer.array());
        inputStreamFrameBufferPosition = 0;
    }

    public static AudioMemorySampleInputStream build(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        AudioFormat audioFormat = audioInputStream.getFormat();
        AudioMemorySampleInputStream audioMemorySampleInputStream = new AudioMemorySampleInputStream(audioInputStream, audioFormat.getSampleRate(), audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
        audioInputStream.close();
        return audioMemorySampleInputStream;
    }

    public static AudioMemorySampleInputStream build(Path audioFile) throws UnsupportedAudioFileException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(audioFile.toFile()));
        AudioMemorySampleInputStream audioMemorySampleInputStream = build(bufferedInputStream);
        bufferedInputStream.close();
        return audioMemorySampleInputStream;
    }

    @Override
    void fillFrameBuffer() {

    }

    @Override
    void framePositionChangeEvent(long framePosition) {

    }

    @Override
    void setFramePosition(long newFramePosition) {
        inputStreamBuffer.position((int) (newFramePosition * frameSize));
    }



}
