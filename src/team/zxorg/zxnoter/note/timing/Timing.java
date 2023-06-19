package team.zxorg.zxnoter.note.timing;

public class Timing {
    /**
     * 时间点时间戳
     */
    public long timestamp;
    /**
     * bpm倍率,用于倍速播放谱面的一部分
     */
    public double bpmSpeed;
    /**
     * 是否为新的基准bpm(歌曲bpm发生变化)
     */
    public boolean isNewBaseBpm;
    /**
     * 绝对bpm,用于绘制分节线
     */
    public double absBpm;

    public Timing(long timingStamp, double bpmSpeed,boolean isNewBaseBpm, double absBpm) {
        this.timestamp = timingStamp;
        this.bpmSpeed = bpmSpeed;
        this.isNewBaseBpm = isNewBaseBpm;
        this.absBpm = absBpm;
    }


    @Override
    public String toString() {
        return '\n' +"Timing{" +
                "时间戳=" + timestamp +
                ", bpmSpeed=" + bpmSpeed +
                ", 绝对bpm=" + absBpm +
                '}';
    }
}
