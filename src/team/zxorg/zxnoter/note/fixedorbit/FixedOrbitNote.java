package team.zxorg.zxnoter.note.fixedorbit;

import team.zxorg.zxnoter.note.BaseNote;

public class FixedOrbitNote extends BaseNote {
    /**
     * 轨道
     */
    int orbit;
    public FixedOrbitNote(long timeStamp , int orbit) {
        super(timeStamp);
        this.orbit = orbit;
    }
}
