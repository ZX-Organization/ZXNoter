package team.zxorg.zxnoter_old.sound.audiomixer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioDataStream {
    protected AudioFormat audioFormat;//音频格式
    protected ByteBuffer audioData;//音频数据

/*
    public static void main(String[] args) throws Exception {
        AudioDataStream audioDataStream = new AudioDataStream(AudioMixer.sampleRateConvert(Files.readAllBytes(Path.of("Hikaru Station.wav")), 12800));
    }
*/


    public AudioDataStream(AudioInputStream audioStream) throws IOException {
        this(audioStream.readAllBytes(), audioStream.getFormat());
    }

    public AudioDataStream(byte[] pcmBytes, AudioFormat format) {

        audioFormat = format;//获取音频格式
        audioData = ByteBuffer.wrap(pcmBytes);//读取全部音频数据
        audioData.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));


    }

    public void read(int bufSize ,ByteBuffer buf) {
        audioData.getShort();
    }
}












