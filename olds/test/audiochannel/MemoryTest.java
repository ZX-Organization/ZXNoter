package audiochannel;


import team.zxorg.zxnoter.audiochannel.channel.MemoryAudioInputChannel;

import javax.sound.sampled.AudioSystem;
import java.io.File;

public class MemoryTest {
   /* public static void main(String[] args) throws Exception{

        MemoryAudioInputChannel memoryAudioChannel=new MemoryAudioInputChannel();
        AudioFrame audioFrame=new AudioFrame(2048);
        audioFrame.setZoom(200);
        audioFrame.multiply=0.8f;
        for (int i = 0; i < 100000; i++) {
            //memoryAudioChannel.setFramePosition(i);
            memoryAudioChannel.read(audioFrame.buf,false);
            audioFrame.pushBuf();
            Thread.sleep(2);
        }


    }*/
}
