package team.zxorg.old;

import team.zxorg.old.handler.AudioHandler;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;

/**
 * 音频通道处理器
 */
public abstract class AudioChannelProcessor implements AudioHandler {
    /**
     * 音频流子节点
     */
    private AudioChannelProcessor child;

    public AudioChannelProcessor(AudioChannelProcessor child) {
        this.child = child;
    }

    protected void read(AudioFormat audioFormat, FloatBuffer buffer) {
        handle(audioFormat, buffer);
        if (child != null) child.read(audioFormat, buffer);
    }

    @Override
    public void handle(AudioFormat targetFormat, FloatBuffer buffer) {

    }

    /**
     * 读取采样数据
     *
     * @param format 目标格式
     * @param buffer 缓冲区
     */
  /*  protected final void handle(AudioFormat format, ByteBuffer buffer) {
        if (child != null) child.handle(format, buffer);

        if (this instanceof AudioByteHandler byteHandler) {
            byteHandler.handle(format, buffer.array());
        }
        if (this instanceof AudioFloatHandler floatHandler) {
            if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
                ShortBuffer conversion = buffer.asShortBuffer();
                if (floatBuffer == null)
                    floatBuffer = FloatBuffer.allocate(buffer.capacity() * 4);
                for (int i = 0; i < buffer.capacity(); i++) {
                    floatBuffer.put(conversion.get(i) / (float) Short.MAX_VALUE);
                }
                conversion.flip();
                conversion.position(0);
                floatHandler.handle(format, floatBuffer.array());
                for (int i = 0; i < buffer.capacity(); i++) {
                    conversion.put((short) Math.round(floatBuffer.get() * Short.MAX_VALUE));
                }
            } else
                floatHandler.handle(format, buffer.asFloatBuffer().array());
        }
        if (this instanceof AudioShortHandler shortHandler) {
            if (format.getEncoding() == AudioFormat.Encoding.PCM_FLOAT) {
                FloatBuffer conversion = buffer.asFloatBuffer();
                if (shortBuffer == null)
                    shortBuffer = ShortBuffer.allocate(buffer.capacity() / 2);

                for (int i = 0; i < buffer.capacity(); i++) {
                    shortBuffer.put((short) (conversion.get(i) * Short.MAX_VALUE));
                }
                conversion.flip();
                conversion.position(0);
                shortHandler.handle(format, shortBuffer.array());
                for (int i = 0; i < buffer.capacity(); i++) {
                    conversion.put(Math.round(shortBuffer.get() / (float) Short.MAX_VALUE));
                }
            } else
                shortHandler.handle(format, buffer.asShortBuffer().array());
        }
    }*/
}
