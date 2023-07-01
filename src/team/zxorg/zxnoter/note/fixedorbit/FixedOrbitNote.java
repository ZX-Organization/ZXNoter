package team.zxorg.zxnoter.note.fixedorbit;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.note.BaseNote;

import java.nio.file.Path;
import java.util.Random;

/**
 * 定轨按键
 */
public class FixedOrbitNote extends BaseNote implements Cloneable, Comparable<BaseNote> {
    /**
     * 轨道
     */
    public int orbit;
    String soundKey;
    protected String soundPath = "";
    protected int hash;

    public FixedOrbitNote(long timeStamp, int orbit) {
        super(timeStamp);
        hash = new Random().nextInt();
        this.orbit = orbit;
    }

    public void setSound(String path) {
        soundPath = path;
        if (path.contains("."))
            soundKey = path.substring(0, path.lastIndexOf(".")).replaceAll("/", ".");
        else
            soundKey = path.replaceAll("/", ".");
    }

    public String getSoundPath() {
        return soundPath;
    }

    @Override
    public FixedOrbitNote clone() {
        FixedOrbitNote fixedOrbitNote = new FixedOrbitNote(timeStamp, orbit);
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
    public JSONObject toJson() {
        JSONObject noteJson = new JSONObject();
        noteJson.put("time", timeStamp);
        noteJson.put("orbit", orbit);
        noteJson.put("soundKey",soundKey);
        noteJson.put("soundPath",soundPath);
        return noteJson;
    }

    @Override
    public int compareTo(BaseNote baseNote) {
        //首先检查时间戳是否不相同

/*        System.out.println(this+"与"+baseNote+"比较");
        System.out.println();*/
        //传入时间戳大于当前时间戳
        if (timeStamp < baseNote.timeStamp) return -1;
        else if (timeStamp > baseNote.timeStamp) return 1;

        else if (baseNote instanceof FixedOrbitNote fixedOrbitNote) {

            if (baseNote instanceof SlideNote && this instanceof LongNote) {
                return 1;
            }
            if (baseNote instanceof LongNote && this instanceof SlideNote) {
                return -1;
            }
            if (baseNote instanceof SlideNote && this instanceof SlideNote) {
                return 0;
            }
            return Integer.compare(orbit, fixedOrbitNote.orbit);
        }
        //相同
        else return 0;
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
        return '\n' + "FixedOrbitNote{" +
                "轨道=" + orbit +
                ", 时间戳=" + timeStamp +
                '}';
    }

}
