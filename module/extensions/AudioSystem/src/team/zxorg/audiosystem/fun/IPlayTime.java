package team.zxorg.audiosystem.fun;

public interface IPlayTime {
    /**
     * 获取时间
     *
     * @return 当前播放位置
     */
    long getTime();

    /**
     * 设置时间
     *
     * @param time 当前播放位置
     */
    void setTime(long time);

    /**
     * 获取时长
     *
     * @return 总时长
     */
    long getDuration();
}
