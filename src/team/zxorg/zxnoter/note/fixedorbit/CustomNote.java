package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;

public class CustomNote extends FixedOrbitNote{
    public int keyType;
    public int hitSound;
    public int normalSampleSet;
    public int extendSampleSet;
    public int sampleIndex;
    public int sampleVolume;
    public CustomNote(long timeStamp, int orbit, int keyType, int hitSound, String[] sampleSetPars) {
        super(timeStamp, orbit);
        this.keyType = keyType;
        this.hitSound = hitSound;
        normalSampleSet = Integer.parseInt(sampleSetPars[0]);
        extendSampleSet = Integer.parseInt(sampleSetPars[1]);
        sampleIndex = Integer.parseInt(sampleSetPars[2]);
        sampleVolume = Integer.parseInt(sampleSetPars[3]);
    }
    @Override
    public JSONObject toJson() {
        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("soundKey",soundKey);
        noteJson.put("soundPath",soundPath);
        noteJson.put("keyType",keyType);
        noteJson.put("hitSound",hitSound);
        noteJson.put("normalSampleSet",normalSampleSet);
        noteJson.put("extendSampleSet",extendSampleSet);
        noteJson.put("sampleIndex",sampleIndex);
        noteJson.put("sampleVolume",sampleVolume);
        return noteJson;
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
