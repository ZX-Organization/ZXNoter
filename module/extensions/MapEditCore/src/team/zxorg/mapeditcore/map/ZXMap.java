package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.note.Note;
import team.zxorg.mapeditcore.timing.Timing;

import java.util.ArrayList;

public class ZXMap {
    public final ArrayList<Note> notes;
    public final ArrayList<Timing> timings;

    public ZXMap(){
        notes = new ArrayList<>();
        timings = new ArrayList<>();
    }


    /**
     * 有序地插入物件
     * @param note 物件
     */
    public void insertNote(Note note) {
        int noteTime = note.getTime();
        if (notes.size() > 0) {
            if (notes.size() > 1) {
                //表中有两个及以上物件
                //二分搜索时间
                notes.add(MapUtil.binarySearchNoteTime(notes, noteTime), note);
            } else {
                //表中只有一个物件
                if (notes.get(0).getTime() <= noteTime)
                    notes.add(note);
            }
        }else
            notes.add(note);
    }
    public void insertTiming(Timing timing){
        int timingTime = timing.getTime();

        if (timings.size() > 0) {
            if (timings.size() > 1) {
                //表中有两个及以上timing
                //二分搜索时间
                timings.add(MapUtil.binarySearchTimingTime(timings, timingTime), timing);
            } else {
                //表中只有一个timing
                if (timings.get(0).getTime() <= timingTime)
                    timings.add(timing);
            }
        }else
            timings.add(timing);
    }


}
