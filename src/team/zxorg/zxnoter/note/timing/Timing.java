package team.zxorg.zxnoter.note.timing;

public class Timing {
    /**
     * 时间点时间戳
     */
    public long timingStamp;
    /**
     * bpm倍率
     */
    public double bpmRatio;

    public Timing(long timingStamp, double bpmRatio) {
        this.timingStamp = timingStamp;
        this.bpmRatio = bpmRatio;
    }

    @Override
    public String toString() {
        return '\n' +"Timing{" +
                "时间戳=" + timingStamp +
                ", bpm倍率=" + bpmRatio +
                '}';
    }
}
