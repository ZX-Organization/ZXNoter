package team.zxorg.zxnoter.note.timing;

public class ZxTiming extends Timing{
    int beats;
    int sampleSet;
    int soundPar;
    int volume;
    boolean isExtendTiming;
    int effect;

    public ZxTiming(long timingStamp, double bpmRatio, int beats, int sampleSet, int soundPar, int volume, boolean isExtendTiming, int effect) {
        super(timingStamp, bpmRatio);
        this.beats = beats;
        this.sampleSet = sampleSet;
        this.soundPar = soundPar;
        this.volume = volume;
        this.isExtendTiming = isExtendTiming;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return '\n'+"ZxTiming{" +
                "排数=" + beats +
                ", 音效组=" + sampleSet +
                ", 音效参数=" + soundPar +
                ", 音量=" + volume +
                ", 是否继承时间点=" + isExtendTiming +
                ", 效果=" + effect +
                ", 时间戳=" + timingStamp +
                ", bpm倍率=" + bpmRatio +
                '}';
    }
}
