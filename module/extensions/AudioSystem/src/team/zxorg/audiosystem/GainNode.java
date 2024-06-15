package team.zxorg.audiosystem;

import java.nio.FloatBuffer;

public class GainNode extends AudioNode {
    private float gain;

    public GainNode(float gain) {
        this.gain = gain;
    }

    @Override
    protected void initialize(AudioBuffer audioBuffer) {
        // 初始化资源，如分配内存、初始化变量等
    }

    @Override
    protected void process(AudioBuffer audioBuffer) {
        FloatBuffer buffer = audioBuffer.getMainBuffer();
        while (buffer.hasRemaining()) {
            buffer.put(buffer.position(), buffer.get(buffer.position()) * gain);
            buffer.position(buffer.position() + 1);
        }
    }

    @Override
    protected void cleanup() {
        // 清理资源，如释放内存、关闭文件等
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }
}