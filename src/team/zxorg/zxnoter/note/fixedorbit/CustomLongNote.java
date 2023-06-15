package team.zxorg.zxnoter.note.fixedorbit;

public class CustomLongNote extends LongNote{
    public int keyType;
    public int hitSound;
    public int normalSampleSet;
    public int extendSampleSet;
    public int sampleIndex;
    public int sampleVolume;
    public CustomLongNote(long timeStamp, int orbit, int keyType, int hitSound, long sustainedTime, String[] sampleSetPars) {
        super(timeStamp, orbit, sustainedTime);
        this.keyType = keyType;
        this.hitSound = hitSound;
        normalSampleSet = Integer.parseInt(sampleSetPars[0]);
        extendSampleSet = Integer.parseInt(sampleSetPars[1]);
        sampleIndex = Integer.parseInt(sampleSetPars[2]);
        sampleVolume = Integer.parseInt(sampleSetPars[3]);
    }

    @Override
    public String toString() {
        return '\n'+"OsuCustomLongNote{" +
                "keyType=" + keyType +
                ", hitSound=" + hitSound +
                ", normalSampleSet=" + normalSampleSet +
                ", extendSampleSet=" + extendSampleSet +
                ", sampleIndex=" + sampleIndex +
                ", sampleVolume=" + sampleVolume +
                ", sustainedTime=" + sustainedTime +
                ", orbit=" + orbit +
                ", soundKey='" + soundKey + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
