package team.zxorg.audiosystem;

import team.zxorg.audiosystem.handler.AudioFloatHandler;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;
import java.util.Objects;

public class AudioStreamNode implements AudioFloatHandler {
    private final AudioStreamNode child;
    private final AudioFloatHandler handler;

    public AudioStreamNode(AudioFloatHandler handler) {
        this.handler = handler;
        child = null;
    }

    public AudioStreamNode(AudioFloatHandler handler, AudioStreamNode child) {
        this.handler = Objects.requireNonNull(handler);
        this.child = Objects.requireNonNull(child);
    }

    @Override
    public void handle(AudioFormat targetFormat, FloatBuffer buffer) {
        buffer.rewind();
        handler.handle(targetFormat, buffer);
        if (child != null) child.handle(targetFormat, buffer);
    }
}
