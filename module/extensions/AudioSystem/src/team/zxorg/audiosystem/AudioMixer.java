package team.zxorg.audiosystem;

import team.zxorg.extensionloader.core.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class AudioMixer {
    /**
     * 源数据线路
     */
    private SourceDataLine sourceDataLine;
    /**
     * 混音器格式
     */
    private AudioFormat audioFormat;
    /**
     * 采样缓冲区
     */
    private FloatBuffer sampleBuffer;
    /**
     * 字节缓冲区
     */
    private ByteBuffer byteBuffer;
    /**
     * 缓冲区大小
     */
    private int bufferSize;

    private final AudioStreamMix mixNode = new AudioStreamMix();


    private final Thread AudioMixerThread = new Thread("AudioMixerThread") {
        @Override
        public void start() {
            super.start();
            Logger.info("AudioMixerThread started");
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                byteBuffer.rewind();
                sampleBuffer.rewind();
                Arrays.fill(sampleBuffer.array(), 0);
                mixNode.handle(audioFormat, sampleBuffer);

                sampleBuffer.rewind();
                if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
                    //转换编码
                    while (sampleBuffer.hasRemaining()) {
                        byteBuffer.putShort((short) Math.round(sampleBuffer.get() * Short.MAX_VALUE));
                    }
                } else if (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_FLOAT) {
                    byteBuffer.asFloatBuffer().put(sampleBuffer);
                }
                byteBuffer.flip();
                sourceDataLine.write(byteBuffer.array(), 0, byteBuffer.capacity());
            }
            Logger.warning("AudioMixerThread 关闭");
        }

        @Override
        public void interrupt() {
            super.interrupt();
            Logger.warning("AudioMixerThread interrupted");
        }
    };

    public void setBufferSize(int size) {
        bufferSize = size;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    /**
     * 启动混音器
     *
     * @throws LineUnavailableException 线路不可用异常
     */
    public void open() throws LineUnavailableException {

        //使用默认格式
        if (audioFormat == null)
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        //检查格式编码
        /*if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
            throw new RuntimeException("Audio format is not PCM_SIGNED");*/

        //使用系统默认源数据线路
        if (sourceDataLine == null)
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

        //采样缓冲区
        sampleBuffer = FloatBuffer.allocate(bufferSize * audioFormat.getChannels());
        sampleBuffer.limit(sampleBuffer.capacity());
        //根据目标格式初始化缓冲区
        byteBuffer = ByteBuffer.allocate(bufferSize * audioFormat.getChannels() * (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED ? 2 : (audioFormat.getEncoding() == AudioFormat.Encoding.PCM_FLOAT ? 4 : -1)));
        byteBuffer.limit(byteBuffer.capacity());

        //启动源数据线路
        sourceDataLine.open(audioFormat, byteBuffer.capacity());
        sourceDataLine.start();

        //启动混音器线程
        if (!AudioMixerThread.isAlive())
            AudioMixerThread.start();

        Logger.info("AudioMixer open");
    }

    public AudioStreamMix getMixNode() {
        return mixNode;
    }

    /**
     * 关闭
     */
    public void close() {
        if (AudioMixerThread.isAlive())
            AudioMixerThread.interrupt();
        Logger.info("AudioMixer closed");
    }
}
