package audiochannel;


import team.zxorg.zxnoter.audiochannel.channel.SourceAudioOutputChannel;
import team.zxorg.zxnoter.audiochannel.channel.TargetAudioInputChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

public class TargetAudioInputChannelTest {
    public static void main(String[] args) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);

        TargetAudioInputChannel targetAudioChannel = new TargetAudioInputChannel(audioFormat);
        AudioFrame audioFrame = new AudioFrame(2048);
        audioFrame.multiply=2;
        audioFrame.setZoom(80);

        SourceAudioOutputChannel sourceAudioOutputChannel=new SourceAudioOutputChannel(audioFormat,2048);
        sourceAudioOutputChannel.open();
        int[] buf=new int[2048];
        while (true) {
            targetAudioChannel.read(buf, false);
            sourceAudioOutputChannel.writeSamples(buf);
            audioFrame.buf=buf;
            audioFrame.pushBuf();

        }

    }


}
