package audiochannel;


import team.zxorg.zxnoter.audiochannel.ThreadAudioMixer;
import team.zxorg.zxnoter.audiochannel.channel.MemoryAudioInputChannel;
import team.zxorg.zxnoter.audiochannel.channel.SourceAudioOutputChannel;
import team.zxorg.zxnoter.audiochannel.channel.TargetAudioInputChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AudioMixerTest {
    public static void main(String[] args) throws Exception {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
        MemoryAudioInputChannel memoryAudioInputChannel = new MemoryAudioInputChannel(audioFormat, Paths.get("docs/reference/LeaF - NANO DEATH!!!!!/work.wav"));

        SourceAudioOutputChannel sourceAudioOutputChannel = new SourceAudioOutputChannel(audioFormat, 2048);
        sourceAudioOutputChannel.open();

        ThreadAudioMixer threadAudioMixer;
        threadAudioMixer = new ThreadAudioMixer(audioFormat, 2048, (buf) -> {
            sourceAudioOutputChannel.writeSamples(buf, 0, buf.length);
        });

        TargetAudioInputChannel targetAudioMixer = new TargetAudioInputChannel(audioFormat);
        threadAudioMixer.addAudioChannel(targetAudioMixer);
        threadAudioMixer.addAudioChannel(memoryAudioInputChannel);


        threadAudioMixer.start();
    }
}
