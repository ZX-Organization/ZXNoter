package team.zxorg.zxnoter.sound.audiomixer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * 音频句柄
 */
public class AudioChannel extends BetterSonicChannel{
    public AudioChannel(float[] audioSamples, int sampleRate) throws IOException {
        super(audioSamples, sampleRate);
    }

   /* private final long audioLength;//音频时长 ms
    protected EventListener eventListener;//事件监听器
    protected EndBehavior endBehavior;//结束行为
    //private MixerAudioInputStream audioInputStream;//音频数据流
    protected PlayState playState;//播放状态
    protected PlayState lastPlayState;//上一次播放状态
    protected long lastTime;//记录播放时间
    protected long pauseTime;//记录暂停时间
    protected long lastTimeStamp;//记录播放时间戳
    private ByteArrayInputStream inputStream;//音频流
    private AudioFormat audioFormat;//当前的音频格式
    private long frameLength;//帧总数
    private ByteBuffer buffer;
    private ShortBuffer shortBuffer;


    public AudioChannel(AudioInputStream audioData) throws IOException {
        inputStream = new ByteArrayInputStream(audioData.readAllBytes());
        playState = PlayState.PAUSE;
        lastPlayState = PlayState.PAUSE;
        endBehavior = EndBehavior.CLOSE;//自动关闭
        audioFormat = new AudioFormat(audioData.getFormat().getSampleRate(), 16, 2, true, false);

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

    *//**
     * 添加事件监听器
     *
     * @param e 事件回调
     *//*
    public void addEventListener(EventListener e) {
        this.eventListener = e;
    }

    //获取16位
    protected int read(byte[] buf, int pos, int bufSize) throws IOException {
        if (buffer == null) {
            buffer = ByteBuffer.allocate(bufSize);
            shortBuffer = buffer.asShortBuffer();
        }

        int numBytes;
        numBytes = inputStream.read(buffer.array());
        System.out.println(shortBuffer.get());
        System.out.println(shortBuffer.get());
        System.out.println(shortBuffer.get());

        if (numBytes == -1)
            playState = PlayState.END;

        return numBytes;
    }

    *//**
     * 设置当前播放帧
     *
     * @param frame
     * @throws IOException
     *//*
    private void setFrame(long frame) {
        inputStream.reset();
        inputStream.skip(frame * audioFormat.getFrameSize());
    }

    public float getVolume() {
        return 0;
    }

    public void setVolume(float volume) {
        //sonic.setVolume(volume);
    }

    *//**
     * 获取当前播放时间 (实时)
     *
     * @return 单位ms
     *//*
    private long getTime_() {
        double remainingFrames = frameLength - (double) (inputStream.available()) / audioFormat.getFrameSize();
        double timeInSeconds = remainingFrames / (audioFormat.getSampleRate() / 1000);
        return Math.round(timeInSeconds);
    }

    *//**
     * 获取当前播放时间 (变速)
     *
     * @return 单位ms
     *//*
    public long getTime() {
        if (playState.equals(PlayState.PAUSE))
            return getTime_();
        return (long) (lastTime + ((System.currentTimeMillis() - lastTimeStamp) ));
    }

    *//**
     * 设置播放时间
     *
     * @param time 单位ms
     * @throws IOException
     *//*
    public void setTime(long time) throws IOException {
        //sonic.flushStream();
        setFrame((long) (time * audioFormat.getSampleRate() / 1000));
        //lastTime = getTime_();//获取时间
        //lastTimeStamp = System.currentTimeMillis();//获取时间戳
    }

    *//**
     * 关闭通道
     *//*
    public void close() {
        playState = PlayState.CLOSE;
    }

    *//**
     * 播放
     *//*
    public void play() {
        playState = PlayState.PLAY;
        lastTimeStamp = System.currentTimeMillis();
        lastTime = getTime_();
    }

    *//**
     * 暂停
     *//*
    public void pause() {
        playState = PlayState.PAUSE;
    }

    *//**
     * 获取播放状态
     *
     * @return 播放状态
     *//*
    public PlayState getPlayState() {
        return playState;
    }

    *//**
     * 设置播放结束行为
     *
     * @param endBehavior 行为
     *//*
    public void setEndBehavior(EndBehavior endBehavior) {
        this.endBehavior = endBehavior;
    }

    *//**
     * 设置播放速度
     *
     * @param v         1f为原速
     * @param transpose 变调
     *//*
    public void setPlaySpeed(boolean transpose, double v) {
        if (transpose) {
            //sonic.setRate(v);
            //sonic.setSpeed(1f);
        } else {
            //sonic.setSpeed(v);
            //sonic.setRate(1f);
        }

    }

    public double getPlaySpeed() {
        //return sonic.getSpeed() * sonic.getRate();
        return 1;
    }*/

    /**
     * 结束行为
     */
    public enum EndBehavior {
        CLOSE,//关闭
        LOOP,//循环
        PAUSE//暂停
    }

    public enum PlayState {
        PLAY,//播放
        PAUSE,//暂停
        END,//播放结束

        CLOSE//关闭
    }


    /**
     * 事件监听器
     */
    public interface EventListener {
        void stateEvent(PlayState playState);//状态事件

        void timeEvent(long time);//时间事件 (ms)
    }
}
