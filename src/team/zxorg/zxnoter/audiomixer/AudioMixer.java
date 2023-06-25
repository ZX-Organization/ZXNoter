package team.zxorg.zxnoter.audiomixer;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class AudioMixer {
    private ArrayList<byte[]> audioDataList = new ArrayList<>();//音频数据表
    private ArrayList<AudioChannel> audioChannelList = new ArrayList<>();//音频通道表

    private int mixerBufSize;//缓冲区大小
    private SourceDataLine line;//播放数据总线

    /**
     * 采样率转换
     *
     * @param data           原始音频数据
     * @param destSampleRate 要转换的码率
     * @return 处理后的音频数据
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public static AudioInputStream sampleRateConvert(byte[] data, int destSampleRate) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        float sampleRate = destSampleRate;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean bigEndian = false;

        AudioFormat newFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, bigEndian);
        AudioInputStream newStream = AudioSystem.getAudioInputStream(newFormat, audioStream);
        return newStream;
    }


    //播放线程
    private Thread mixerPlayThread = new Thread(() -> {
        try {
            while (true) {
                short[] mixerPCM = new short[mixerBufSize];//混音采样


                synchronized (this) {

                    for (int i = 0; i < audioChannelList.size(); i++) {
                        AudioChannel audioChannel = audioChannelList.get(i);
                        if (!audioChannel.lastPlayState.equals(audioChannel.playState)) {//状态发生变化
                            audioChannel.lastPlayState = audioChannel.playState;
                            if (audioChannel.eventListener != null) {
                                audioChannel.eventListener.stateEvent(audioChannel.playState);//调用回调事件
                            }

                            if (audioChannel.playState.equals(AudioChannel.PlayState.END))//如果播放结束执行动作
                                switch (audioChannel.endBehavior) {
                                    case CLOSE -> {
                                        audioChannel.playState = AudioChannel.PlayState.CLOSE;
                                    }
                                    case LOOP -> {
                                        audioChannel.setProgress(0);
                                        audioChannel.playState = AudioChannel.PlayState.PLAY;
                                    }
                                    case PAUSE -> {
                                        audioChannel.playState = AudioChannel.PlayState.PAUSE;
                                    }
                                }


                        }
                        if (audioChannel.playState.equals(AudioChannel.PlayState.CLOSE)) {//释放音频
                            audioChannelList.remove(audioChannel);
                            i--;
                        } else if (audioChannel.playState.equals(AudioChannel.PlayState.PLAY)) {//播放音频

                            if (audioChannel.eventListener != null) {//更新暂停时间
                                audioChannel.pauseTime = audioChannel.lastTime + (System.currentTimeMillis() - audioChannel.lastTimeStamp);
                                audioChannel.eventListener.timeEvent(audioChannel.pauseTime);//时间事件
                            }


                            byte[] channelBuf = new byte[mixerBufSize * 2];
                            audioChannel.read(channelBuf, 0, mixerBufSize * 2);
                            for (int j = 0; j < mixerBufSize; j++) {
                                mixerPCM[j] += BytesUtils.bytes2short(new byte[]{channelBuf[j * 2], channelBuf[j * 2 + 1]});//混音
                            }
                        }
                    }


                   /* for (AudioChannel audioChannel : audioChannelList) {
                        if (!audioChannel.lastPlayState.equals(audioChannel.playState)) {
                            audioChannel.lastPlayState = audioChannel.playState;
                            if (audioChannel.eventListener != null)
                                audioChannel.eventListener.stateEvent(audioChannel.playState);//调用回调事件
                        }
                        if (audioChannel.playState.equals(AudioChannel.PlayState.CLOSE)) {//释放音频
                            audioChannelList.remove(audioChannel);
                        } else if (audioChannel.playState.equals(AudioChannel.PlayState.PLAY)) {//播放音频
                            byte[] channelBuf = new byte[mixerBufSize * 2];
                            audioChannel.read(channelBuf, 0, mixerBufSize * 2);
                            for (int i = 0; i < mixerBufSize; i++) {
                                mixerPCM[i] += BytesUtils.bytes2short(new byte[]{channelBuf[i * 2], channelBuf[i * 2 + 1]});//混音
                            }
                        }
                    }*/
                }

                writeToLine(mixerPCM);


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }, "AudioMixerThread");

    /**
     * 音频混音器对象
     *
     * @param sampleRate   码率
     * @param mixerBufSize 音频缓冲区大小 必须是2的倍数
     * @throws LineUnavailableException
     */
    public AudioMixer(float sampleRate, int mixerBufSize) throws LineUnavailableException {
        /*float sampleRate,
        int sampleSizeInBits,
        int channels,
        boolean signed,
        boolean bigEndian*/
        AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 2, true, false);
        line = AudioSystem.getSourceDataLine(audioFormat);
        this.mixerBufSize = mixerBufSize;
        line.open(audioFormat, 2048);
        line.start();
        mixerPlayThread.start();

    }

    private void writeToLine(short[] buf) {
        byte[] pcmBuf = new byte[buf.length * 2];
        for (int i = 0; i < buf.length; i++) {
            System.arraycopy(BytesUtils.short2bytesA(buf[i]), 0, pcmBuf, i * 2, 2);
        }
        line.write(pcmBuf, 0, pcmBuf.length);
    }

    /**
     * 打开音频
     *
     * @param audioFile 音频文件 wav
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(File audioFile) throws IOException, UnsupportedAudioFileException {
        return addAudio(audioFile.toPath());
    }

    /**
     * 打开音频
     *
     * @param audioFile 音频文件 wav
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(Path audioFile) throws IOException, UnsupportedAudioFileException {
        return addAudio(Files.readAllBytes(audioFile));
    }

    /**
     * 打开音频
     *
     * @param audioData 音频数据 wav
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(byte[] audioData) throws UnsupportedAudioFileException, IOException {

        if (!audioDataList.contains(audioData))
            audioDataList.add(audioData);
        return audioDataList.indexOf(audioData);
    }


    /**
     * 关闭音频
     *
     * @param handle 音频句柄
     */
    public void removeAudio(int handle) {
        audioDataList.set(handle, null);
    }

    /**
     * 创建音频频道
     *
     * @param audioHandle 音频句柄
     */
    public AudioChannel createChannel(int audioHandle) throws UnsupportedAudioFileException, IOException {

        synchronized (this) {
            AudioChannel newChannel = new AudioChannel(sampleRateConvert(audioDataList.get(audioHandle), (int) line.getFormat().getSampleRate()));
            audioChannelList.add(newChannel);
            return newChannel;
        }

        /*sampleRateConvert(audioData, (int) line.getFormat().getSampleRate())
        synchronized (this) {
            AudioChannel newChannel = new AudioChannel(audioDataList.get(audioHandle), line.getFormat());
            audioChannelList.add(newChannel);
            return newChannel;
        }*/
    }

    /**
     * 移除音频通道
     *
     * @param channel 要移除的通道
     */
    public void removeChannel(AudioChannel channel) {
        synchronized (this) {
            audioChannelList.remove(channel);
        }
    }


}
