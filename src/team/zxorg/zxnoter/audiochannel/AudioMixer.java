package team.zxorg.zxnoter.audiochannel;

import javax.sound.sampled.AudioFormat;
import java.io.Closeable;
import java.io.IOException;

public abstract class AudioMixer implements Closeable {
    AudioFormat audioFormat;//音频格式
    int[] mixerByteBuffer;

    /*public ByteBuffer readMixerBuffer() {
        mixerByteBuffer.asIntBuffer().put(mixerBuffer);
        mixerByteBuffer.flip();
        mixerByteBuffer.position(0);
        mixerByteBuffer.clear();
        return mixerByteBuffer;
    }*/

    public AudioMixer(AudioFormat audioFormat, int bufferSize) {
        this.audioFormat = audioFormat;
        mixerByteBuffer = new int[bufferSize];
    }

    abstract void mixer();//混音

    @Override
    public void close() throws IOException {

    }
}
