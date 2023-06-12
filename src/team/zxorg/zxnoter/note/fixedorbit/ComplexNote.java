package team.zxorg.zxnoter.note.fixedorbit;

import java.util.ArrayList;

public class ComplexNote extends FixedOrbitNote implements Cloneable{
    ArrayList<FixedOrbitNote> notes = new ArrayList<>();
    public ComplexNote(long timeStamp, int orbit, ArrayList<FixedOrbitNote> notes) {
        super(timeStamp, orbit);
        this.notes = notes;
    }
    public ComplexNote(long timeStamp, int orbit) {
        super(timeStamp, orbit);
    }


    public void addNote(FixedOrbitNote note){
        notes.add(note);
    }
    public void clearNote(){
        notes.clear();
        timeStamp = 0;
        orbit = 0;
    }

    @Override
    public ComplexNote clone() {
        return new ComplexNote(timeStamp,orbit,notes);
    }
}
