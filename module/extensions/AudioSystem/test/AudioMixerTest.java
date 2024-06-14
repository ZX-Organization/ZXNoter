package team.zxorg.audiosystem.test;

import team.zxorg.old.AudioMixer;
import team.zxorg.old.FileAudioStreamChannel;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Path;

public class AudioMixerTest {
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException {
        Path file = Path.of("docs/reference/jitaimei/0/audio.ogg");
        AudioMixer mixer = new AudioMixer();
        mixer.setBufferSize(2048);
        mixer.open();

        FileAudioStreamChannel fileAudioStreamNode = new FileAudioStreamChannel();
        fileAudioStreamNode.openFile(file.toFile());

        FileAudioStreamChannel fileAudioStreamNode2 = new FileAudioStreamChannel();
        fileAudioStreamNode2.openFile(file.toFile());

        mixer.getMixNode().addNode(fileAudioStreamNode);
        Thread.sleep(2000);
        mixer.getMixNode().addNode(fileAudioStreamNode2);
    }
}
