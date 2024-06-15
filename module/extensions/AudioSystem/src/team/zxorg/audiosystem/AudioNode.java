package team.zxorg.audiosystem;

import java.nio.FloatBuffer;
public abstract class AudioNode {
    private AudioBuffer audioBuffer;
    private AudioNode child;
    private boolean initialized = false;

    public int getChannels() {
        return audioBuffer != null ? audioBuffer.getChannels() : 0;
    }

    public int getSampleRate() {
        return audioBuffer != null ? audioBuffer.getSampleRate() : 0;
    }

    public AudioNode getChild() {
        return child;
    }

    public void setChild(AudioNode child) {
        this.child = child;
        if (child != null && audioBuffer != null && initialized) {
            child.initialize_(audioBuffer);
        }
    }

    protected final void initialize_(AudioBuffer audioBuffer) {
        this.audioBuffer = audioBuffer;
        initialize(audioBuffer);
        initialized = true;
        if (child != null) {
            child.initialize_(audioBuffer);
        }
    }

    protected final void process_() {
        if (audioBuffer != null) {
            try {
                process(audioBuffer);
                audioBuffer.rewind();
                if (child != null) {
                    child.process_();
                }
            } catch (Exception e) {
                e.printStackTrace();
                cleanup_();
            }
        }
    }

    protected final void cleanup_() {
        cleanup();
        initialized = false;
        if (child != null) {
            child.cleanup_();
        }
    }

    protected abstract void initialize(AudioBuffer audioBuffer);

    protected abstract void process(AudioBuffer audioBuffer);

    protected abstract void cleanup();
}