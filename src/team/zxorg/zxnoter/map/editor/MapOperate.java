package team.zxorg.zxnoter.map.editor;

import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

public class MapOperate {
    FixedOrbitNote srcNote;
    FixedOrbitNote desNote;

    public MapOperate(FixedOrbitNote srcNote, FixedOrbitNote desNote) {
        this.srcNote = srcNote;
        this.desNote = desNote;
    }
}
