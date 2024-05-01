package team.zxorg.audiosystem.test;

import team.zxorg.audiosystem.AudioMixer;
import team.zxorg.audiosystem.FileAudioStreamNode;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Path;

public class AudioMixerTest {
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException {
        Path file = Path.of("C:\\Users\\zedoC\\Music\\1_nonoc - Believe in You_(Instrumental).wav");
        AudioMixer mixer = new AudioMixer();
        mixer.setBufferSize(2048);
        mixer.open();

        FileAudioStreamNode fileAudioStreamNode = new FileAudioStreamNode();
        fileAudioStreamNode.openFile(file.toFile());

        FileAudioStreamNode fileAudioStreamNode2 = new FileAudioStreamNode();
        fileAudioStreamNode2.openFile(file.toFile());

        mixer.getMixNode().addNode(fileAudioStreamNode);
        Thread.sleep(2000);
        mixer.getMixNode().addNode(fileAudioStreamNode2);
    }
}
