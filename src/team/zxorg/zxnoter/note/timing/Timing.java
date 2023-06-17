package team.zxorg.zxnoter.note.timing;

public class Timing {
    /**
     * 时间点时间戳
     */
    public long timingStamp;
    /**
     * bpm倍率
     */
    public double bpm;
    public boolean isNewBaseBpm;

    public Timing(long timingStamp, double bpm, boolean isNewBaseBpm) {
        this.timingStamp = timingStamp;
        this.bpm = bpm;
        this.isNewBaseBpm = isNewBaseBpm;
    }


    @Override
    public String toString() {
        return '\n' +"Timing{" +
                "时间戳=" + timingStamp +
                ", bpm=" + bpm +
                '}';
    }
}
