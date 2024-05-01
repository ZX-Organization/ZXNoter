package team.zxorg.audiosystem;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

/**
 * 音频流节点
 */
public abstract class AudioStreamNode {
    /**
     * 音频流子节点
     */
    private AudioStreamNode child;


    private ByteBuffer byteBuffer;
    private short[] shortBuffer;
    private float[] floatBuffer;

    /**
     * 读取采样数据
     *
     * @param targetFormat 目标格式
     * @param buffer       缓冲区
     */
    public final void read(AudioFormat targetFormat, byte[] buffer) {
        if (child != null) child.read(targetFormat, buffer);
        if (this.byteBuffer == null) this.byteBuffer = ByteBuffer.allocate(buffer.length);

        handle(targetFormat, buffer);
    }

    /**
     * 处理音频
     *
     * @param targetFormat 目标格式
     * @param buffer       缓冲区
     */
    abstract void handle(AudioFormat targetFormat, byte[] buffer);

    /**
     * 处理音频
     *
     * @param targetFormat 目标格式
     * @param buffer       缓冲区
     */
    abstract void handle(AudioFormat targetFormat, short[] buffer);

    /**
     * 处理音频
     *
     * @param targetFormat 目标格式
     * @param buffer       缓冲区
     */
    abstract void handle(AudioFormat targetFormat, float[] buffer);
}
