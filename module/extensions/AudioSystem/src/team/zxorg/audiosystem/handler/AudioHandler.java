package team.zxorg.audiosystem.handler;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;


public interface AudioHandler {

    /**
     * 处理音频
     *
     * @param targetFormat 目标格式
     * @param buffer       缓冲区
     */
    void handle(AudioFormat targetFormat, FloatBuffer buffer);
}
