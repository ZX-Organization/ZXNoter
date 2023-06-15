package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.note.BaseNote;

import java.nio.file.Path;

/**
 * 定轨按键
 */
public class FixedOrbitNote extends BaseNote implements Cloneable{
    /**
     * 轨道
     */
    public int orbit;
    String soundKey;
    private String soundPath = "";
    public FixedOrbitNote(long timeStamp , int orbit) {
        super(timeStamp);
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
        return new FixedOrbitNote(timeStamp , orbit);
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
    public String toString() {
        return '\n' +"FixedOrbitNote{" +
                "轨道=" + orbit +
                ", 时间戳=" + timeStamp +
                '}';
    }
}
