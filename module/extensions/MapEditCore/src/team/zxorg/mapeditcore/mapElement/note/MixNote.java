package team.zxorg.mapeditcore.mapElement.note;

import java.util.ArrayList;

public class MixNote extends Note{
    private final ArrayList<Note> childNotes;

    /**
     * 位置构造法
     * @param time 时间
     * @param position 位置
     */
    public MixNote(int time, double position) {
        super(time, position);
        childNotes = new ArrayList<>();
    }

    /**
     * 轨道构造法
     * @param time 时间
     * @param orbit 所处轨道
     * @param maxOrbit 总轨道数
     */
    public MixNote(int time, int orbit, int maxOrbit) {
        super(time, orbit, maxOrbit);
        childNotes = new ArrayList<>();
    }

    /**
     * 向混合物件中添加新物件
     * @param note 要添加的新物件
     */
    public void addNote(Note note) {
        childNotes.add(note);
    }

    /**
     * 混合物件克隆
     */
    public MixNote clone(){
        MixNote note = new MixNote(getTime(),getPosition());
        for (Note childNote: childNotes) {
            note.childNotes.add(childNote.clone());
        }
        return note;
    }

    public void clearNote() {
        childNotes.clear();
    }

    @Override
    public String toString() {
        return '\n'+"MixNote{" +
                "时间=" + time +
                ", 位置=" + position +
                ", key音=" + keyAudio +
                ", child=" + childNotes +
                '}'+'\n';
    }
}
