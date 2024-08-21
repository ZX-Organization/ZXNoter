package team.zxorg.zxnoter.sound.audiomixer;

import team.zxorg.zxnoter.sound.audiomixer.AudioChannel.EndBehavior;
import team.zxorg.zxnoter.sound.audiomixer.AudioChannel.PlayState;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 音频句柄
 */
public class AudioSonicChannel {
    private final long audioLength;//音频时长 ms
    protected EventListener eventListener;//事件监听器
    protected AudioChannel.EndBehavior endBehavior;//结束行为
    //private MixerAudioInputStream audioInputStream;//音频数据流
    protected AudioChannel.PlayState playState;//播放状态
    protected AudioChannel.PlayState lastPlayState;//上一次播放状态
    protected long lastTime;//记录播放时间
    protected long pauseTime;//记录暂停时间
    protected long lastTimeStamp;//记录播放时间戳
    protected long syncTime;
    private ByteArrayInputStream inputStream;//音频流
    private AudioFormat audioFormat;//当前的音频格式
    private Sonic sonic;//处理音频效果
    private int channelBufSize;//缓冲区大小
    private byte inBuffer[];//输入缓冲区
    private byte outStagingBuffer[];//输出暂存缓冲区
    //private float actualShifting;//实际变速
    private long frameLength;//帧总数
    private long t = 0;


    public AudioSonicChannel(AudioInputStream audioData) throws IOException {
        inputStream = new ByteArrayInputStream(audioData.readAllBytes());
        playState = AudioChannel.PlayState.PAUSE;
        lastPlayState = AudioChannel.PlayState.PAUSE;
        endBehavior = EndBehavior.CLOSE;//自动关闭
        audioFormat = new AudioFormat(audioData.getFormat().getSampleRate(), 16, 2, true, false);
        sonic = new Sonic((int) audioFormat.getSampleRate(), audioFormat.getChannels());
        sonic.setQuality(1);
        channelBufSize = 512;
        inBuffer = new byte[channelBufSize];//输入缓冲区
        frameLength = inputStream.available() / 4;//帧长度
        audioLength = (frameLength * 1000) / (long) (audioFormat.getSampleRate());//音频时长
        //152s
        // audioInputStream=new MixerAudioInputStream(inputStream);
        lastTimeStamp = System.currentTimeMillis();
        pauseTime = 0;
        lastTime = 0;
    }

    public long getAudioLength() {
        return audioLength;
    }

    /**
     * 添加事件监听器
     *
     * @param e 事件回调
     */
    public void addEventListener(EventListener e) {
        this.eventListener = e;
    }

    //获取16位
    protected int read(byte[] buf, int pos, int bufSize) throws IOException {


        outStagingBuffer = new byte[bufSize - pos];


        int numBytes = sonic.readBytesFromStream(outStagingBuffer, bufSize - pos);//将数据读取到缓冲区

        if (numBytes == 0 && bufSize != pos)//如果读取的字节不足则再次写入到sonic
        {
            if (writeToSonic() == -1)
                return bufSize;
        }

        if (pos == bufSize)
            return bufSize;

        System.arraycopy(outStagingBuffer, 0, buf, pos, numBytes);
        if (numBytes < bufSize)
            read(buf, pos + numBytes, bufSize);


        return numBytes;
    }

    private int writeToSonic() throws IOException {
        //从原始流读取数据
        int numRead;

        //System.out.println(lastTime);


        //同步时间 上缓冲区全用完

        long remainingFrames = frameLength - (inputStream.available()) / audioFormat.getFrameSize();
        syncTime = (remainingFrames / (long) (audioFormat.getSampleRate() / 1000));
        //System.out.println("同步->: " + timeInSeconds);


        numRead = inputStream.read(inBuffer);
        lastTimeStamp = System.currentTimeMillis();
        lastTime = getTime_();

        //System.out.println("numRead" + numRead);
        //System.out.println(inputStream.available());
        //将数据交给sonic
        if (numRead <= 0) {
            sonic.flushStream();
            playState = PlayState.END;
            /*if (eventListener != null)
                eventListener.stateEvent(playState);*/
        } else {
            sonic.writeBytesToStream(inBuffer, numRead);
            //System.out.println("写入");
        }


        return numRead;
    }

    /**
     * 设置当前播放帧
     *
     * @param frame
     * @throws IOException
     */
    private void setFrame(long frame) {
        inputStream.reset();
        inputStream.skip(frame * audioFormat.getFrameSize());
    }

    public float getVolume() {
        return sonic.getVolume();
    }

    public void setVolume(float volume) {
        sonic.setVolume(volume);
    }

    /**
     * 获取当前播放时间 (实时)
     *
     * @return 单位ms
     */
    private long getTime_() {
        double remainingFrames = frameLength - (double) (inputStream.available()) / audioFormat.getFrameSize();
        double timeInSeconds = remainingFrames / (audioFormat.getSampleRate() / 1000);
        return Math.round(timeInSeconds);
    }

    /**
     * 获取当前播放时间 (变速)
     *
     * @return 单位ms
     */
    public long getTime() {
        if (playState.equals(PlayState.PAUSE))
            return getTime_();

        //if (syncTime - t > 3)
        System.out.println(syncTime - t + "  " + syncTime);
        t = syncTime;

        return (long) (syncTime);//+ ((System.currentTimeMillis() - lastTimeStamp) * sonic.getRate() * sonic.getSpeed())
    }

    /**
     * 设置播放时间
     *
     * @param time 单位ms
     * @throws IOException
     */
    public void setTime(long time) throws IOException {
        //sonic.flushStream();
        setFrame((long) (time * audioFormat.getSampleRate() / 1000));
        //lastTime = getTime_();//获取时间
        //lastTimeStamp = System.currentTimeMillis();//获取时间戳
    }

    /**
     * 关闭通道
     */
    public void close() {
        playState = PlayState.CLOSE;
    }

    /**
     * 播放
     */
    public void play() {
        sonic.flushStream();
        playState = AudioChannel.PlayState.PLAY;
        lastTimeStamp = System.currentTimeMillis();
        lastTime = getTime_();
    }

    /**
     * 暂停
     */
    public void pause() {
        playState = AudioChannel.PlayState.PAUSE;
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态
     */
    public AudioChannel.PlayState getPlayState() {
        return playState;
    }

    /**
     * 设置播放结束行为
     *
     * @param endBehavior 行为
     */
    public void setEndBehavior(AudioChannel.EndBehavior endBehavior) {
        this.endBehavior = endBehavior;
    }

    /**
     * 设置播放速度
     *
     * @param v         1f为原速
     * @param transpose 变调
     */
    public void setPlaySpeed(boolean transpose, double v) {
        if (transpose) {
            sonic.setRate(v);
            sonic.setSpeed(1f);
        } else {
            sonic.setSpeed(v);
            sonic.setRate(1f);
        }

    }

    public double getPlaySpeed() {
        return sonic.getSpeed() * sonic.getRate();
    }

    /**
     * 事件监听器
     */
    public interface EventListener {
        void stateEvent(AudioChannel.PlayState playState);//状态事件

        void timeEvent(long time);//时间事件 (ms)
    }


}
