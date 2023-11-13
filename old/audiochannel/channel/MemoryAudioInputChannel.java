package team.zxorg.zxncore.audiochannel.channel;

import team.zxorg.zxncore.audiochannel.AudioUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class MemoryAudioInputChannel extends AudioInputChannel {
    public MemoryAudioInputChannel(AudioFormat audioFormat, Path audioPath) throws IOException, UnsupportedAudioFileException {
        super(audioFormat);
        AudioInputStream sourceAudioInputStream = AudioSystem.getAudioInputStream(audioPath.toFile());
        AudioInputStream audioInputStream= AudioUtils.audioStreamConvert(sourceAudioInputStream,audioFormat);

        sampleBuffer = ByteBuffer.allocateDirect(audioInputStream.available());
        sampleBuffer.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));
        byte[] buf = new byte[2048];
        int len = 0;
        while (audioInputStream.available() > 0) {
            len = audioInputStream.read(buf);
            sampleBuffer.put(buf, 0, len);
        }
        sampleBuffer.flip();
    }

    @Override
    boolean fillSampleBuffer() {
        return false;
    }


    /**
     * 获取当前播放帧位置
     *
     * @return 位置
     */
    @Override
    public long getFramePosition() {
        return sampleBuffer.position() / audioFormat.getFrameSize();
    }

    /**
     * 设置当前播放帧位置
     *
     * @param position 帧位置
     * @return 设置状态
     */
    @Override
    public boolean setFramePosition(long position) {
        sampleBuffer.position((int) (position * audioFormat.getFrameSize()));
        return true;
    }
}
