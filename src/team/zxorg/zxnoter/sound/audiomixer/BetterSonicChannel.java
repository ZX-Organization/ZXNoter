package team.zxorg.zxnoter.sound.audiomixer;

public class BetterSonicChannel {

    private final Sonic sonic;//处理音频效果
    private final float[] audioSamples;
    private final float[] audioLSamples;
    private final float[] audioRSamples;
    private final int sampleRate;
    private final long frameLength;
    private final long audioLength;
    protected AudioChannel.PlayState playState;//播放状态
    protected AudioChannel.PlayState lastPlayState;//上一次播放状态
    protected AudioChannel.EndBehavior endBehavior;//结束行为
    protected AudioSonicChannel.EventListener eventListener;//事件监听器


    protected long lastTime;//记录播放时间
    protected long pauseTime;//记录暂停时间
    protected long lastTimeStamp;//记录播放时间戳


    public BetterSonicChannel(float[] audioSamples, int sampleRate) {
        this.audioSamples = audioSamples;
        this.sampleRate = sampleRate;
        audioLSamples = new float[audioSamples.length / 2];
        audioRSamples = new float[audioSamples.length / 2];
        //分离声道
        for (int i = 0; i < audioSamples.length / 2; i++) {
            audioLSamples[i] = audioSamples[2 * i];
            audioRSamples[i] = audioSamples[2 * i + 1];
        }

        playState = AudioChannel.PlayState.PAUSE;
        lastPlayState = AudioChannel.PlayState.PAUSE;
        endBehavior = AudioChannel.EndBehavior.CLOSE;//自动关闭

        sonic = new Sonic(sampleRate, 2);
        sonic.setQuality(1);
        frameLength = audioSamples.length / 2;


        /*channelBufSize = 512;
        inBuffer = new byte[channelBufSize];//输入缓冲区
        frameLength = inputStream.available() / 4;//帧长度*/

        audioLength = (frameLength * 1000) / (long) (sampleRate);//音频时长

        //152s
        // audioInputStream=new MixerAudioInputStream(inputStream);
        /*lastTimeStamp = System.currentTimeMillis();
        pauseTime = 0;
        lastTime = 0;*/
    }

    //获取16位
    protected int read(float[] bufL, float[] bufR) {

        System.arraycopy(audioLSamples, 0, bufL, 0, bufL.length);
        System.arraycopy(audioRSamples, 0, bufR, 0, bufR.length);
        return 0;
    }

    public long getTime() {
        return 0;
    }

    public void setTime(long time) {

    }

    public float getVolume() {
        return sonic.getVolume();
    }

    public void setVolume(float volume) {
        sonic.setVolume(volume);
    }


    /**
     * 关闭通道
     */
    public void close() {
        playState = AudioChannel.PlayState.CLOSE;
    }

    /**
     * 播放
     */
    public void play() {
        sonic.flushStream();
        playState = AudioChannel.PlayState.PLAY;
        /*lastTimeStamp = System.currentTimeMillis();
        lastTime = getTime_();*/
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

    /**
     * 获取播放速度
     *
     * @return 速度
     */
    public double getPlaySpeed() {
        return sonic.getSpeed() * sonic.getRate();
    }

    public long getAudioLength() {
        return audioLength;
    }
}
