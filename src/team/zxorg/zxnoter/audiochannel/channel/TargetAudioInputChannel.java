package team.zxorg.zxnoter.audiochannel.channel;

import javax.sound.sampled.*;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TargetAudioInputChannel extends AudioInputChannel implements Closeable {
    TargetDataLine targetDataLine;
    byte[] buffer;

    /**
     * 获取系统默认的输入设备
     *
     * @param audioFormat
     * @throws LineUnavailableException
     */
    public TargetAudioInputChannel(AudioFormat audioFormat) throws LineUnavailableException {
        super(audioFormat);
        buffer = new byte[4096];
        sampleBuffer = ByteBuffer.wrap(buffer);
        sampleBuffer.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));
        //AudioSystem.getTargetLineInfo()
        targetDataLine = AudioSystem.getTargetDataLine(null);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
    }

    @Override
    boolean fillSampleBuffer() {
        targetDataLine.read(buffer, 0, buffer.length);
        sampleBuffer.position(0);
        return true;
    }

    @Override
    public void close() throws IOException {
        targetDataLine.stop();
    }
}
