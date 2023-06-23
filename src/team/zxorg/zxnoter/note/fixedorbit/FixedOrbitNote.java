package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.note.BaseNote;

import java.nio.file.Path;
import java.util.Random;

/**
 * 定轨按键
 */
public class FixedOrbitNote extends BaseNote implements Cloneable,Comparable<BaseNote>{
    /**
     * 轨道
     */
    public int orbit;
    String soundKey;
    private String soundPath = "";
    protected int hash;
    public FixedOrbitNote(long timeStamp , int orbit) {
        super(timeStamp);
        hash = new Random().nextInt();
        this.orbit = orbit;
    }

    public void setSound(String path){
        soundPath = path;
        if (path.contains("."))
            soundKey = path.substring(0,path.lastIndexOf(".")).replaceAll("/",".");
        else
            soundKey = path.replaceAll("/",".");
    }
    public String getSoundPath(){
        return soundPath;
    }

    @Override
    public FixedOrbitNote clone(){
        FixedOrbitNote fixedOrbitNote = new FixedOrbitNote(timeStamp , orbit);
        fixedOrbitNote.hash = hash;
        return fixedOrbitNote;
    }

    @Override
    public int getOrbit() {
        return orbit;
    }

    @Override
    public void setOrbit(int orbit) {
        this.orbit = orbit;
    }

    @Override
    public int getImdNoteType() {
        return 0;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public ComplexNote getParent() {
        return null;
    }
    @Override
    public int compareTo(BaseNote baseNote) {
        if (timeStamp<baseNote.timeStamp) return -1;
        else if (timeStamp> baseNote.timeStamp) return 1;
        else if (baseNote instanceof FixedOrbitNote fixedOrbitNote){
            return -Integer.compare(orbit, fixedOrbitNote.orbit);
        }else return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FixedOrbitNote fixedOrbitNote)
            return hash == fixedOrbitNote.hash;
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return '\n' +"FixedOrbitNote{" +
                "轨道=" + orbit +
                ", 时间戳=" + timeStamp +
                '}';
    }

}
