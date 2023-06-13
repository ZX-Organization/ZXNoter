package team.zxorg.zxnoter.note.fixedorbit;

public class OsuCustomNote extends FixedOrbitNote{
    public int keyType;
    int hitSound;
    public int normalSampleSet;
    public int extendSampleSet;
    public int sampleIndex;
    public int sampleVolume;
    public OsuCustomNote(long timeStamp, int orbit,int keyType,int hitSound,String[] sampleSetPars) {
        super(timeStamp, orbit);
        this.keyType = keyType;
        this.hitSound = hitSound;
        normalSampleSet = Integer.parseInt(sampleSetPars[0]);
        extendSampleSet = Integer.parseInt(sampleSetPars[1]);
        sampleIndex = Integer.parseInt(sampleSetPars[2]);
        sampleVolume = Integer.parseInt(sampleSetPars[3]);
    }

    @Override
    public String toString() {
        return '\n'+"OsuCustomNote{" +
                "keyType=" + keyType +
                ", hitSound=" + hitSound +
                ", normalSampleSet=" + normalSampleSet +
                ", extendSampleSet=" + extendSampleSet +
                ", sampleIndex=" + sampleIndex +
                ", sampleVolume=" + sampleVolume +
                ", orbit=" + orbit +
                ", soundKey='" + soundKey + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
