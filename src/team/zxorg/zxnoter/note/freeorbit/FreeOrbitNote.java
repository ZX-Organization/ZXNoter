package team.zxorg.zxnoter.note.freeorbit;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;

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

    @Override
    public long getLength() {
        //待重写
        return 0;
    }

    @Override
    public ComplexNote getParent() {
        return null;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}
