import team.zxorg.audiosystem.AudioBuffer;

import java.nio.file.Path;

public class AudioBufferTest {
    public static void main(String[] args) {
        AudioBuffer audioBuffer = new AudioBuffer(Path.of("docs/reference/jitaimei/0/audio.ogg"), 1, 44100);
        System.out.println(audioBuffer.getMainBuffer().remaining() / 44100f);
        /*while (audioBuffer.getBuffer().hasRemaining()) {
            System.out.printf("%.4f%n", audioBuffer.getBuffer().get());
        }*/
    }
}
