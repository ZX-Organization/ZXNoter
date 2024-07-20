package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.mapElement.note.Note;

import java.util.ArrayList;

public class MapOperate {
    /**
     * 操作前物件列表
     */
    private final ArrayList<Note> preOpNotes;
    /**
     * 操作后物件列表
     */
    private final ArrayList<Note> aftOpNotes;


    /**
     * 反转构造
     * @param operate 要反转的操作
     */
    private MapOperate(MapOperate operate) {
        preOpNotes = operate.aftOpNotes;
        aftOpNotes = operate.preOpNotes;
    }
}
