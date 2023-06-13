package team.zxorg.zxnoter.note.freeorbit;

import team.zxorg.zxnoter.note.BaseNote;

public class FreeOrbitNote extends BaseNote {
    public FreeOrbitNote(long timeStamp) {
        super(timeStamp);
    }

    @Override
    public BaseNote clone() {
        return null;
    }

    @Override
    public int getOrbit() {
        return -1;
    }

    @Override
    public void setOrbit(int orbit) {}

    @Override
    public int getImdNoteType() {
        return -1;
    }
}
