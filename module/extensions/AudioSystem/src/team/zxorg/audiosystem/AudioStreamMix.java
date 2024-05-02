package team.zxorg.audiosystem;

import team.zxorg.audiosystem.handler.AudioFloatHandler;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 音频流混音节点
 */
public class AudioStreamMix implements AudioFloatHandler {


    /**
     * 混合的音频流节点
     */
    private final List<AudioFloatHandler> nodes = new ArrayList<>();

    @Override
    public void handle(AudioFormat format, FloatBuffer buffer) {
        //枚举所有节点
        for (var node : nodes) {
            buffer.rewind();
            node.handle(format, buffer);
        }
       /* buffer.rewind();
        while (buffer.hasRemaining()) {
            buffer.mark();
            float v = buffer.get();
            System.out.println(v);
           *//* buffer.reset();
            buffer.put(v * 0.2f);*//*
        }*/
    }


    /**
     * 获取节点列表
     *
     * @return 节点列表
     */
    public List<AudioFloatHandler> getNodes() {
        return nodes;
    }

    /**
     * 增加节点
     *
     * @param node 节点
     */
    public void addNode(AudioFloatHandler node) {
        nodes.add(node);
    }

    /**
     * 移除节点
     *
     * @param node 节点
     */
    public void removeNode(AudioFloatHandler node) {
        nodes.remove(node);
    }
}
