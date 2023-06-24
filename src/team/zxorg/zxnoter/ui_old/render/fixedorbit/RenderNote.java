package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.ui_old.render.basis.RenderRectangle;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitNotesKey;

public class RenderNote {
    public RenderNoteObject pos;
    public FixedOrbitNote note;
    public ComplexNote complexNote;
    public FixedOrbitNotesKey key;
    public RenderRectangle renderRectangle;

    public RenderNote(RenderNoteObject pos, FixedOrbitNote note, ComplexNote complexNote, FixedOrbitNotesKey key, RenderRectangle renderRectangle) {
        this.pos = pos;
        this.note = note;
        this.complexNote = complexNote;
        this.key = key;
        this.renderRectangle = renderRectangle;
    }

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
        HEAD,//头部
        BODY,//身体
        FOOT//尾部
    }
}
