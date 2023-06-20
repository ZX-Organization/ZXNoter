package team.zxorg.zxnoter.audiochannel.channel;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class AudioOutputChannel {
    private final byte[] bytesOutputBuffer;
    private final ByteBuffer outputBuffer;
    protected final AudioFormat audioFormat;
    private final int audioBitDepth;

    public AudioOutputChannel(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
        audioBitDepth = audioFormat.getSampleSizeInBits();
        bytesOutputBuffer = new byte[2048 * (audioBitDepth / 8)];
        outputBuffer = ByteBuffer.wrap(bytesOutputBuffer);
        outputBuffer.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));

    }

    public AudioOutputChannel(AudioFormat audioFormat, int bufferSize) {
        this.audioFormat = audioFormat;
        audioBitDepth = audioFormat.getSampleSizeInBits();
        bytesOutputBuffer = new byte[bufferSize * (audioBitDepth / 8)];
        outputBuffer = ByteBuffer.wrap(bytesOutputBuffer);
        outputBuffer.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));
    }

    public abstract void writeBytes(byte[] data);

    public abstract void open();

    public abstract void close();

    public void writeSamples(int[] samples) {
        writeSamples(samples, 0, samples.length);
    }

    public void writeSamples(int[] samples, int pos, int len) {
        outputBuffer.position(0);
        for (int i = pos; i < pos + len - 1; i++) {
            if (audioBitDepth == 8)
                outputBuffer.put((byte) (samples[i] >> 24));
            else if (audioBitDepth == 16)
                outputBuffer.putShort((short) (samples[i] >> 16));
            else if (audioBitDepth == 32)
                outputBuffer.putInt(samples[i]);
            else
                throw new IllegalArgumentException();
        }
        writeBytes(bytesOutputBuffer);
        outputBuffer.flip();
    }
}
