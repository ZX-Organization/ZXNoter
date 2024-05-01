package team.zxorg.audiosystem;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileAudioStreamNode extends AudioStreamNode {

    private AudioInputStream audioStream;
    private ByteBuffer byteBuffer;

    public void openFile(File file) throws UnsupportedAudioFileException, IOException {
        audioStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false), AudioSystem.getAudioInputStream(file));
    }

    @Override
    void handle(AudioFormat targetFormat, byte[] buffer) {

    }

    @Override
    void handle(AudioFormat format, short[] buffer) {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(buffer.length * 2);
        }
        int bytesRead = 0;
        try {
            bytesRead = audioStream.read(byteBuffer.array(), 0, byteBuffer.capacity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bytesRead == -1) {
                // End of stream, handle accordingly
                return;
            }
            byteBuffer.position(0);
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = byteBuffer.getShort();
            }

    }

    @Override
    void handle(AudioFormat targetFormat, float[] buffer) {

    }
}
