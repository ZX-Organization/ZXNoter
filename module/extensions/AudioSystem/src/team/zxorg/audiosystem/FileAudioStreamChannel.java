package team.zxorg.audiosystem;

import team.zxorg.audiosystem.handler.AudioHandler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class FileAudioStreamChannel implements AudioHandler {

    private AudioInputStream audioStream;
    private ByteBuffer byteBuffer;

    public void openFile(File file) throws UnsupportedAudioFileException, IOException {
        audioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false), AudioSystem.getAudioInputStream(file));

    }


    @Override
    public void handle(AudioFormat format, FloatBuffer buffer) {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(buffer.capacity() * Short.BYTES);
        }
        byteBuffer.rewind();

        int bytesRead;
        try {
            bytesRead = audioStream.read(byteBuffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bytesRead == -1) {
            return;
        }
        byteBuffer.rewind();
        while (byteBuffer.hasRemaining()) {
            buffer.mark();
            float v = buffer.get();
            buffer.reset();
            float s = (float) byteBuffer.getShort() / Short.MAX_VALUE;
            //s*=0.2f;
            buffer.put(v + s);
        }
    }

}
