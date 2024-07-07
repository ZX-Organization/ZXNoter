package team.zxorg.zxnoter_old.note.timing;

import com.alibaba.fastjson2.JSONObject;

public class Timing implements Comparable<Timing>{
    /**
     * 时间点时间戳
     */
    public long timestamp;
    /**
     * bpm倍率,用于倍速播放谱面的一部分
     */
    public double bpmSpeed;
    /**
     * 是否为新的基准bpm(歌曲bpm发生变化)
     */
    public boolean isNewBaseBpm;
    /**
     * 绝对bpm,用于绘制分节线
     */
    public double absBpm;

    public Timing(long timingStamp, double bpmSpeed,boolean isNewBaseBpm, double absBpm) {
        this.timestamp = timingStamp;
        this.bpmSpeed = bpmSpeed;
        this.isNewBaseBpm = isNewBaseBpm;
        this.absBpm = absBpm;
    }
    public Timing(JSONObject timingJson) {
        System.out.println(timingJson);
        timestamp = timingJson.getLongValue("time");
        bpmSpeed = timingJson.getDoubleValue("bpmSpeed");
        isNewBaseBpm = timingJson.getBooleanValue("isNewBaseBpm");
        absBpm = timingJson.getDoubleValue("absBpm");
    }
    public JSONObject toJson(){
        JSONObject timingJson = new JSONObject();
        timingJson.put("time",timestamp);
        timingJson.put("bpmSpeed",bpmSpeed);
        timingJson.put("isNewBaseBpm",isNewBaseBpm);
        timingJson.put("absBpm",absBpm);
        return timingJson;
    }

    @Override
    public String toString() {
        return '\n' +"Timing{" +
                "时间戳=" + timestamp +
                ", bpmSpeed=" + bpmSpeed +
                ", isNewBaseBpm=" + isNewBaseBpm +
                ", 绝对bpm=" + absBpm +
                '}';
    }

    @Override
    public int compareTo(Timing o) {
        if (o.timestamp>timestamp)return -1;
        else if (o.timestamp<timestamp)return 1;
        else if (o.isNewBaseBpm && !isNewBaseBpm) return -1;
        else if (!o.isNewBaseBpm && isNewBaseBpm) return 1;
        else return 0;
    }
}
