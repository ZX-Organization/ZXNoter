package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author xiang2333
 */
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
    public CustomNote(JSONObject customNoteJson) {
        super(customNoteJson.getLongValue("time"), customNoteJson.getIntValue("orbit"),customNoteJson.getString("soundPath"));
        keyType = customNoteJson.getIntValue("keyType");
        hitSound = customNoteJson.getIntValue("hitSound");
        normalSampleSet = customNoteJson.getIntValue("normalSampleSet");
        extendSampleSet = customNoteJson.getIntValue("extendSampleSet");
        sampleIndex = customNoteJson.getIntValue("sampleIndex");
        sampleVolume = customNoteJson.getIntValue("sampleVolume");
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
