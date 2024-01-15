package audiochannel.audiomixer_t.channel;

import com.zedo.audiomixer_t.AudioUtils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 音频句柄
 */
public class AudioFileSampleInputStream extends FixedLengthAudioSampleInputStream {
    protected final AudioInputStream audioInputStream;


    public AudioFileSampleInputStream(AudioInputStream audioInputStream, float sampleRate, int destSampleSizeInBits, int channels) {
        super(sampleRate, channels, audioInputStream.getFrameLength(), AudioBitDepth.get(audioInputStream.getFormat()));
        this.audioInputStream = AudioUtils.audioStreamConvert(audioInputStream, sampleRate, destSampleSizeInBits, channels);
        inputStreamFrameBufferPosition = 0;
    }

    public AudioFileSampleInputStream(AudioInputStream audioInputStream, float sampleRate, int destSampleSizeInBits, int channels, int bufferSize) {
        super(sampleRate, channels, audioInputStream.getFrameLength(), AudioBitDepth.get(audioInputStream.getFormat()), bufferSize);
        this.audioInputStream = AudioUtils.audioStreamConvert(audioInputStream, sampleRate, destSampleSizeInBits, channels);
        inputStreamFrameBufferPosition = 0;
    }

    public AudioFileSampleInputStream(AudioInputStream audioInputStream) {
        super(audioInputStream.getFormat().getSampleRate(), audioInputStream.getFormat().getChannels(), audioInputStream.getFrameLength(), AudioBitDepth.get(audioInputStream.getFormat()));
        this.audioInputStream = AudioUtils.audioStreamConvert(audioInputStream, sampleRate, audioBitDepth.getBitLength(), channels);
        inputStreamFrameBufferPosition = 0;
    }

    public static AudioFileSampleInputStream build(Path audioFile) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.toFile());
        byte[] audioByteData = new byte[audioInputStream.available()];
        AudioFormat audioFormat = audioInputStream.getFormat();
        long frameLength = audioInputStream.getFrameLength();
        audioInputStream.read(audioByteData);
        audioInputStream.close();
        ByteArrayInputStream audioByteArrayInputStream = new ByteArrayInputStream(audioByteData);
        return new AudioFileSampleInputStream(new AudioInputStream(audioByteArrayInputStream, audioFormat, frameLength));
    }

    @Override
    void fillFrameBuffer() {
        try {
            if (audioInputStream.available() <= 0)
                return;
            inputStreamBuffer.position(0);
            inputStreamFrameBufferPosition += audioInputStream.read(inputStreamBuffer.array()) / frameSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    void setFramePosition(long newFramePosition) {
        try {
            audioInputStream.reset();
            inputStreamFrameBufferPosition = newFramePosition;
            audioInputStream.skip(newFramePosition * frameSize);
            inputStreamBuffer.position(inputStreamBuffer.capacity());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected Number handleSample(Number sample, AudioBitDepth audioBitDepth, int bufferPosition) {
        return sample.longValue() * 1.;
    }

    @Override
    void framePositionChangeEvent(long framePosition) {
        //System.out.println("当前位置:" + getAudioTimeNow() + "ms");
        //System.out.println("全部时长:" + getAudioTimeLength() + "ms");
    }
}
