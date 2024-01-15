package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author xiang2333
 */
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
    public CustomLongNote(JSONObject customLongNoteJson) {
        super(customLongNoteJson.getLongValue("time"), customLongNoteJson.getIntValue("orbit"), customLongNoteJson.getLongValue("sustainedTime"),customLongNoteJson.getString("soundPath"));
        keyType = customLongNoteJson.getIntValue("keyType");
        hitSound = customLongNoteJson.getIntValue("hitSound");
        normalSampleSet = customLongNoteJson.getIntValue("normalSampleSet");
        extendSampleSet = customLongNoteJson.getIntValue("extendSampleSet");
        sampleIndex = customLongNoteJson.getIntValue("sampleIndex");
        sampleVolume = customLongNoteJson.getIntValue("sampleVolume");
    }

    @Override
    public JSONObject toJson() {

        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("sustainedTime", sustainedTime);
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
