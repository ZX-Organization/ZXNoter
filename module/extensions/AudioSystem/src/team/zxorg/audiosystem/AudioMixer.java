package team.zxorg.audiosystem;

import team.zxorg.extensionloader.core.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
     * 字节缓冲区
     */
    private byte[] buffer;
    /**
     * 缓冲区大小
     */
    private int bufferSize;

    private AudioStreamMixNode mixNode = new AudioStreamMixNode();


    private final Thread AudioMixerThread = new Thread("AudioMixerThread") {
        @Override
        public void start() {
            super.start();
            Logger.info("AudioMixerThread started");
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                Arrays.fill(buffer, (byte) 0);
                mixNode.read(audioFormat, buffer);
                sourceDataLine.write(buffer, 0, buffer.length);
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


        //sampleBuffer = new short[bufferSize * audioFormat.getChannels()];
        //设置缓冲区2
        //buffer = ByteBuffer.allocate(sampleBuffer.length * 2);
        buffer = new byte[bufferSize];

        //启动源数据线路
        sourceDataLine.open(audioFormat, bufferSize * audioFormat.getFrameSize());
        sourceDataLine.start();

        //启动混音器线程
        if (!AudioMixerThread.isAlive())
            AudioMixerThread.start();

        Logger.info("AudioMixer open");
    }

    public AudioStreamMixNode getMixNode() {
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
