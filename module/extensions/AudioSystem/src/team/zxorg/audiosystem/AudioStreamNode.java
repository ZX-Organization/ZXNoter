package team.zxorg.audiosystem;

import team.zxorg.audiosystem.handler.AudioHandler;

import javax.sound.sampled.AudioFormat;
import java.nio.FloatBuffer;
import java.util.Objects;

public class AudioStreamNode implements AudioHandler {
    private final AudioStreamNode child;
    private final AudioHandler handler;

    public AudioStreamNode(AudioHandler handler) {
        this.handler = handler;
        child = null;
    }

    public AudioStreamNode(AudioHandler handler, AudioStreamNode child) {
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
