package team.zxorg.zxnoter.note.timing;

public class AdvTiming extends Timing{
    int beats;
    int sampleSet;
    int volume;
    boolean isExtendTiming;
    int effect;

    public AdvTiming(long timingStamp, double bpmRatio, int beats, int sampleSet,int volume, boolean isExtendTiming, int effect) {
        super(timingStamp, bpmRatio);
        this.beats = beats;
        this.sampleSet = sampleSet;
        this.volume = volume;
        this.isExtendTiming = isExtendTiming;
        this.effect = effect;
    }
}
