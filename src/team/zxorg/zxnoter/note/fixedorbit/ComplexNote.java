package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.note.BaseNote;

import java.util.ArrayList;
import java.util.Arrays;

public class ComplexNote extends FixedOrbitNote implements Cloneable{
    public ArrayList<FixedOrbitNote> notes = new ArrayList<>();
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
        ArrayList<FixedOrbitNote> newNotes = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            newNotes.add(notes.get(i).clone());
        }
        return new ComplexNote(timeStamp,orbit,newNotes);
    }


    @Override
    public String toString() {
        return '\n' +"ComplexNote{" +
                "组合列表=" + notes +'\n' +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}'+'\n';
    }
}
