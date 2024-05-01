package team.zxorg.audiosystem;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 音频流混音节点
 */
public class AudioStreamMixNode extends AudioStreamNode {
    /**
     * 采样缓冲区
     */
    private short[] sampleBuffer;
    /**
     * 混合的音频流节点
     */
    private final List<AudioStreamNode> nodes = new ArrayList<>();

    @Override
    void handle(AudioFormat targetFormat, byte[] buffer) {

    }

    @Override
    void handle(AudioFormat format, short[] buffer) {
        if (sampleBuffer == null)
            sampleBuffer = new short[buffer.length];


        //读取节点
        for (var node : nodes) {
            node.read(format, sampleBuffer);
            //叠加采样
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] += sampleBuffer[i];
            }
        }
    }

    @Override
    void handle(AudioFormat targetFormat, float[] buffer) {

    }

    /**
     * 获取节点列表
     *
     * @return 节点列表
     */
    public List<AudioStreamNode> getNodes() {
        return nodes;
    }

    /**
     * 增加节点
     *
     * @param node 节点
     */
    public void addNode(AudioStreamNode node) {
        nodes.add(node);
    }

    /**
     * 移除节点
     *
     * @param node 节点
     */
    public void removeNode(AudioStreamNode node) {
        nodes.remove(node);
    }
}
