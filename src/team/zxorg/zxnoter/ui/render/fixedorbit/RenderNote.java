package team.zxorg.zxnoter.ui.render.fixedorbit;

import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;

public class RenderNote {
    public RenderNoteObject pos;
    public FixedOrbitNote note;
    public ComplexNote complexNote;

    public RenderNote(RenderNoteObject pos, FixedOrbitNote note, ComplexNote complexNote) {
        this.pos = pos;
        this.note = note;
        this.complexNote = complexNote;
    }

    public RenderNote(RenderNoteObject pos, FixedOrbitNote note) {
        this.pos = pos;
        this.note = note;
    }

    public enum RenderNoteObject {
        HAND,//头部
        BODY,//身体
        FOOT//尾部
    }
}
