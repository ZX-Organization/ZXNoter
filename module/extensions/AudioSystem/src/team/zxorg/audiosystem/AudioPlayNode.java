package team.zxorg.audiosystem;

import java.nio.FloatBuffer;
import java.nio.file.Path;

public class AudioPlayNode extends AudioNode {
    Path file;
    private AudioBuffer audio;
    private long index;

    public AudioPlayNode(Path file) {
        this.file = file;
        //audio = new AudioBuffer(file, getChannels(), getSampleRate());
        index = 0;
    }


    @Override
    protected void initialize(AudioBuffer audioBuffer) {
        audio = new AudioBuffer(file, getChannels(), getSampleRate());
        index = 0;
    }

    @Override
    protected void process(AudioBuffer audioBuffer) {
        FloatBuffer buffer = audio.getMainBuffer();
        for (int i = 0; i < buffer.remaining(); i++) {
            buffer.put(i, buffer.get(i) + audio.getMainBuffer().get());
        }
    }

    @Override
    protected void cleanup() {

    }
}
