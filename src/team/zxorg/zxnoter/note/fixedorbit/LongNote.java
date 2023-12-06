package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.note.BaseNote;

/**
 * 长键
 * @author xiang2333
 */

public class LongNote extends FixedOrbitNote implements Cloneable,Comparable<BaseNote>{
    /**
     *  持续时间
     */
    public long sustainedTime;
    public LongNote(long timeStamp, int orbit , long sustainedTime) {
        super(timeStamp, orbit);
        this.sustainedTime = sustainedTime;
    }
    public LongNote(long timeStamp, int orbit , long sustainedTime, String soundPath) {
        super(timeStamp, orbit);
        this.sustainedTime = sustainedTime;
        this.soundPath = soundPath;
    }

    public LongNote(LongNote longNote) {
        super(longNote.timeStamp,longNote. orbit);
        sustainedTime = longNote.sustainedTime;
        hash = longNote.hash;
    }
    public LongNote(JSONObject longNoteJson) {
        super(longNoteJson.getLongValue("time"),longNoteJson. getIntValue("orbit"),longNoteJson.getString("soundPath"));
        sustainedTime = longNoteJson.getLongValue("sustainedTime");
    }

    @Override
    public LongNote clone() {
        return new LongNote(this);
    }

    @Override
    public int getImdNoteType() {
        return 2;
    }

    @Override
    public long getLength() {
        return sustainedTime;
    }

    @Override
    public JSONObject toJson() {
        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("sustainedTime", sustainedTime);
        noteJson.put("soundKey",soundKey);
        noteJson.put("soundPath",soundPath);
        return noteJson;
    }
    @Override
    public int compareTo(BaseNote baseNote) {
        if (baseNote instanceof SlideNote slideNote){
            if (Math.abs(timeStamp- slideNote.timeStamp) <= ZXFixedOrbitMapEditor.AUTO_FIX_MISTAKE){
                return Long.compare(timeStamp+sustainedTime, slideNote.timeStamp);
            }
        }
        return super.compareTo(baseNote);
    }

    @Override
    public String toString() {
        return '\n' +"LongNote{" +
                "持续时间=" + sustainedTime +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}';
    }
}
