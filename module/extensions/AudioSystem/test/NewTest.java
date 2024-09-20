import team.zxorg.audiosystem.AudioFIle;
import team.zxorg.audiosystem.AudioOutputLine;
import team.zxorg.audiosystem.BufferedAudioPlayer;
import team.zxorg.audiosystem.fun.IAudioProcessor;

import java.nio.file.Path;

public class NewTest {
    public static void main(String[] args) throws Exception {
        AudioOutputLine audioOutput = new AudioOutputLine();
        audioOutput.open(48000, 2, 2048);

        float[] data = AudioFIle.read(Path.of("C:\\Users\\zedoC\\Music\\棗いつき - Secret of my heart.mp3"), 48000, 2);

        IAudioProcessor processor = new BufferedAudioPlayer(data);
        audioOutput.setAudioProcessor(processor);


      /*

        AudioProcessor processor = new AudioPlayProcessor(data);
        SonicProcessor sonicProcessor = new SonicProcessor(processor);

        audioOutput.setAudioProcessor(sonicProcessor);*/

        /*

        long ts = System.currentTimeMillis();

        Sonic sonic = new Sonic(48000, 2);
        sonic.setQuality(1);

        sonic.setSpeed(0.8f);
        sonic.writeFloatToStream(data, data.length / 2);
        sonic.flushStream();
        float[] out = new float[sonic.samplesAvailable() * 2];
        sonic.readFloatFromStream(out, out.length);
        System.out.println(System.currentTimeMillis() - ts);

        AudioProcessor processor = new AudioPlayProcessor(out);
        audioOutput.setAudioProcessor(processor);*/
    }
}
