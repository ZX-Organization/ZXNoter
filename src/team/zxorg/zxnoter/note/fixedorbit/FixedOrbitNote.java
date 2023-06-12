package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.note.BaseNote;

/**
 * 定轨按键
 */
public class FixedOrbitNote extends BaseNote {
    /**
     * 轨道
     */
    public int orbit;
    public FixedOrbitNote(long timeStamp , int orbit) {
        super(timeStamp);
        this.orbit = orbit;
    }
}
