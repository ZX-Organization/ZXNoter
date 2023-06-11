package team.zxorg.zxnoter.note;

public class Timing {
    /**
     * 时间点时间戳
     */
    long timingStamp;
    /**
     * bpm倍率
     */
    double bpmRatio;

    public Timing(long timingStamp, double bpmRatio) {
        this.timingStamp = timingStamp;
        this.bpmRatio = bpmRatio;
    }
}