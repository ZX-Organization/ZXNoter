package team.zxorg.zxnoter.audiochannel.channel;

public abstract class AudioChannelPlayControl {
    abstract void play(); // 播放音频
    abstract void pause(); // 暂停音频
    abstract void stop(); // 停止音频
    abstract void setVolume(float volume); // 设置音量
}
