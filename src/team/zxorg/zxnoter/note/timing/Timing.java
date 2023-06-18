package team.zxorg.zxnoter.note.timing;

public class Timing {
    /**
     * 时间点时间戳
     */
    public long timestamp;
    /**
     * bpm倍率
     */
    public double bpmSpeed;
    public boolean isNewBaseBpm;
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
