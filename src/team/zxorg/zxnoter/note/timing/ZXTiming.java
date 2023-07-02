package team.zxorg.zxnoter.note.timing;

import com.alibaba.fastjson2.JSONObject;

public class ZXTiming extends Timing implements Comparable<Timing>{
    public int beats;
    public int sampleSet;
    public int soundPar;
    public int volume;
    public boolean isExtendTiming;
    public int effect;

    public ZXTiming(long timingStamp, double bpm, boolean isNewBaseBpm, double absBpm, int beats, int sampleSet, int soundPar, int volume, boolean isExtendTiming, int effect) {
        super(timingStamp, bpm, isNewBaseBpm, absBpm);
        this.beats = beats;
        this.sampleSet = sampleSet;
        this.soundPar = soundPar;
        this.volume = volume;
        this.isExtendTiming = isExtendTiming;
        this.effect = effect;
    }
    public ZXTiming(JSONObject zxTimingJson) {
        super(
                zxTimingJson.getLongValue("timestamp"),zxTimingJson.getDoubleValue("bpmSpeed"),
                zxTimingJson.getBooleanValue("isNewBaseBpm"),zxTimingJson.getDoubleValue("absBpm")
                );
        beats = zxTimingJson.getIntValue("beats");
        sampleSet = zxTimingJson.getIntValue("sampleSet");
        soundPar = zxTimingJson.getIntValue("soundPar");
        volume = zxTimingJson.getIntValue("volume");
        isExtendTiming = zxTimingJson.getBooleanValue("isExtendTiming");
        effect = zxTimingJson.getIntValue("effect");
    }

    public JSONObject toJson() {
        JSONObject timingJson = new JSONObject();
        timingJson.put("time", timestamp);
        timingJson.put("bpmSpeed", bpmSpeed);
        timingJson.put("isNewBaseBpm", isNewBaseBpm);
        timingJson.put("absBpm", absBpm);
        timingJson.put("beats", beats);
        timingJson.put("sampleSet", sampleSet);
        timingJson.put("soundPar", soundPar);
        timingJson.put("volume", volume);
        timingJson.put("isExtendTiming", isExtendTiming);
        timingJson.put("effect", effect);
        return timingJson;
    }

    @Override
    public int compareTo(Timing o) {
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return '\n' + "ZxTiming{" +
                "排数=" + beats +
                ", 音效组=" + sampleSet +
                ", 音效参数=" + soundPar +
                ", 音量=" + volume +
                ", 是否继承时间点=" + isExtendTiming +
                ", 效果=" + effect +
                ", 时间戳=" + timestamp +
                ", bpmSpeed=" + bpmSpeed +
                ", 绝对bpm=" + absBpm +
                ", 是否为新基准bpm=" + isNewBaseBpm +
                '}';
    }
}
