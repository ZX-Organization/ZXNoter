package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.info.map.ImdInfo;
import team.zxorg.zxnoter.note.BaseNote;

import java.util.Objects;

/**
 * 滑键
 */
public class SlideNote extends FixedOrbitNote implements Cloneable,Comparable<BaseNote>{
    /**
     * 滑键参数,正右负左
     * 值大小为滑动轨道数
     * 0参数不合法
     */
    public int slideArg;
    public SlideNote(long timeStamp, int orbit, int slideArg) {
        super(timeStamp, orbit);
        this.slideArg = slideArg;
        hash = Objects.hash(orbit, timeStamp, soundPath, slideArg);
    }
    public SlideNote(JSONObject slideNoteJson) {
        super(slideNoteJson.getLongValue("time"), slideNoteJson.getIntValue("orbit"),slideNoteJson.getString("soundPath"));
        slideArg = slideNoteJson.getIntValue("slideArg");
        hash = Objects.hash(orbit, timeStamp, soundPath, slideArg);
    }

    public SlideNote(SlideNote slideNote) {
        super(slideNote.timeStamp, slideNote.orbit);
        slideArg = slideNote.slideArg;
        hash = slideNote.hash;
    }

    public FixedOrbitNote[] convertNote(ImdInfo.ConvertMethod convertMethod){
        FixedOrbitNote[] convertNotes = null;
        //转换按键
        switch (convertMethod){
            case BASE -> {
                convertNotes = new FixedOrbitNote[1];
                convertNotes[0] = new FixedOrbitNote(timeStamp,orbit);
                return convertNotes;
            }
            case BASE_SLIDE -> {
                convertNotes = new FixedOrbitNote[Math.abs(slideArg)+1];
                for (int i = 0; i <= Math.abs(slideArg); i++) {
                    convertNotes[i] = new FixedOrbitNote(timeStamp , orbit + (slideArg > 0 ? i : -i));
                }
                return convertNotes;
            }
            case ADVANCE_SLIDE -> {
                convertNotes = new FixedOrbitNote[Math.abs(slideArg)+1];
                for (int i = 0; i <= Math.abs(slideArg); i++) {
                    convertNotes[i] = new FixedOrbitNote(timeStamp + (int)(i * ((double)25 / (Math.abs(slideArg) + 1))) , orbit + (slideArg > 0 ? i : -i));
                }
                return convertNotes;
            }
        }
        return null;
    }

    @Override
    public SlideNote clone(){
        return new SlideNote(this);
    }

    @Override
    public int getImdNoteType() {
        return 1;
    }
    @Override
    public int compareTo(BaseNote baseNote) {
        if (baseNote instanceof SlideNote slideNote){
            if (Math.abs(timeStamp-slideNote.timeStamp) <= ZXFixedOrbitMapEditor.AUTO_FIX_MISTAKE)
                return 0;
        }
        if (baseNote instanceof LongNote longNote){
            if (Math.abs(timeStamp- longNote.timeStamp) <= ZXFixedOrbitMapEditor.AUTO_FIX_MISTAKE){
                return Long.compare( timeStamp,longNote.timeStamp+longNote.sustainedTime);
            }
        }
        return super.compareTo(baseNote);
    }
    @Override
    public JSONObject toJson() {
        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("slideArg",slideArg);
        noteJson.put("soundKey",soundKey);
        noteJson.put("soundPath",soundPath);
        return noteJson;
    }
    @Override
    public String toString() {
        return '\n' +"SlideNote{" +
                "滑动=" + slideArg +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}';
    }
}
