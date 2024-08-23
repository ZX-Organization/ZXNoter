package team.zxorg.zxnoter.sound.audiomixer;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class AudioMixer {
    private final ArrayList<float[]> audioDataList = new ArrayList<>();//音频数据表
    private final ArrayList<AudioChannel> audioChannelList = new ArrayList<>();//音频通道表

    private final SourceDataLine line;//播放数据总线
    int sampleRate;

    /**
     * 音频混音器对象
     *
     * @param sampleRate   码率
     * @param mixerBufSize 音频缓冲区大小 必须是2的倍数
     */
    public AudioMixer(int sampleRate, int mixerBufSize) throws LineUnavailableException {
        this.sampleRate = sampleRate;
        AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 2, true, false);
        line = AudioSystem.getSourceDataLine(audioFormat);
        //缓冲区大小
        line.open(audioFormat, mixerBufSize * 4);
        line.start();
        float[] channelBufL = new float[mixerBufSize];
        float[] channelBufR = new float[mixerBufSize];
        float[] mixerPCML = new float[mixerBufSize];//混音采样
        float[] mixerPCMR = new float[mixerBufSize];//混音采样

        Thread mixerPlayThread = new Thread(() -> {
            try {
                while (true) {
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
                                        case CLOSE -> audioChannel.playState = AudioChannel.PlayState.CLOSE;
                                        case LOOP -> {
                                            audioChannel.setTime(0);
                                            audioChannel.playState = AudioChannel.PlayState.PLAY;
                                        }
                                        case PAUSE -> audioChannel.playState = AudioChannel.PlayState.PAUSE;
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

                                audioChannel.read(channelBufL, channelBufR);
                                for (int j = 0; j < mixerBufSize; j++) {
                                    mixerPCML[j] += channelBufL[j];//混音
                                    //mixerPCM[j] += BytesUtils.bytes2short(new byte[]{channelBuf[j * 2], channelBuf[j * 2 + 1]});//混音
                                }
                            }
                        }


                    }

                   // writeToLine(mixerPCM);


                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }, "AudioMixerThread");
        mixerPlayThread.start();

    }

    private static short floatToShort(float sample) {
        sample = Math.max(-1.0f, Math.min(1.0f, sample));
        return (short) (sample * 32767);
    }

    /**
     * 采样率转换
     *
     * @param audioStream    音频数据
     * @param destSampleRate 要转换的码率
     * @return 处理后的音频数据
     */
    public static AudioInputStream sampleRateConvert(AudioInputStream audioStream, float destSampleRate) throws UnsupportedAudioFileException, IOException {
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean bigEndian = false;

        AudioFormat newFormat = new AudioFormat(destSampleRate, sampleSizeInBits, channels, true, bigEndian);
        return AudioSystem.getAudioInputStream(newFormat, audioStream);
    }

    private void writeToLine(float[] buf) {
        byte[] pcmBuf = new byte[buf.length * 2]; // 每个 float 对应两个字节

        for (int i = 0; i < buf.length; i++) {
            short shortSample = floatToShort(buf[i]);
            // 将 short 值转换为两个字节，并存储在 pcmBuf 中
            pcmBuf[i * 2] = (byte) (shortSample & 0xFF); // 低位字节
            pcmBuf[i * 2 + 1] = (byte) ((shortSample >> 8) & 0xFF); // 高位字节
        }

        // 将字节数组写入音频输出
        line.write(pcmBuf, 0, pcmBuf.length);
    }

    /**
     * 打开音频
     *
     * @param audioFile 音频文件
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(Path audioFile) {
        return addAudio(FFmpeg.read(audioFile, (int) sampleRate));
    }

    /**
     * 打开音频
     *
     * @param audioData 音频数据
     * @return 被打开的音频句柄 audioHandle
     */
    public int addAudio(float[] audioData) {
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
            AudioChannel newChannel = new AudioChannel(audioDataList.get(audioHandle), sampleRate);
            audioChannelList.add(newChannel);
            return newChannel;
        }
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
