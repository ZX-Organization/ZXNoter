package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.ui_old.component.CanvasPane;
import team.zxorg.zxnoter.ui_old.render.basis.RenderPoint;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitNotesKey;


public class FixedOrbitMapRender extends FixedOrbitRender {
    String state;//状态
    public RenderPoint renderPoint = new RenderPoint();//当前的鼠标位置
    public RenderNote renderNote = new RenderNote();//当前的鼠标位置


    public FixedOrbitMapRender(FixedOrbitRenderInfo renderInfo, CanvasPane canvas, ZXMap zxMap, String state, String theme) {
        super(renderInfo, zxMap, canvas.canvas, theme);
        this.state = state;
    }


    @Override
    public void renderHandle() {
        drawAllNote();
    }

    public void drawAllNote() {
        {//全部初始化
            renderNote.pos = null;
            renderNote.note = null;
            renderNote.complexNote = null;
            renderNote.key = null;
            renderNote.renderRectangle = null;
        }


        for (int i = 0; i < zxMap.notes.size(); i++) {
            if (zxMap.notes.get(i) instanceof FixedOrbitNote note) {

                double orbitWidth = (renderInfo.canvasWidth.get() / getInfo().orbits.get());


                //渲染对应键的额外内容
                if (note instanceof ComplexNote complexNote) {
                    for (int j = 0; j < complexNote.notes.size(); j++) {
                        drawNote(complexNote.notes.get(j), (j >= complexNote.notes.size() - 1 ? DrawMode.ALL : DrawMode.ONLY_LINE), complexNote);
                    }
                    for (int j = 0; j < complexNote.notes.size() - 1; j++) {
                        drawNote(complexNote.notes.get(j), DrawMode.ONLY_NODE, complexNote);
                    }
                } else {
                    drawNote(note, DrawMode.ALL, null);
                }

                loadImage(getImage(state, FixedOrbitNotesKey.NOTE));
                //计算尺寸
                renderRectangle.setXY(Pos.CENTER,
                        orbitWidth * (note.orbit) + orbitWidth / 2,
                        (renderInfo.timelinePosition.doubleValue() - note.timeStamp + getInfo().getJudgedLineTimeOffset()) * renderInfo.timelineZoom.doubleValue());
                renderRectangle.scale(Pos.CENTER, orbitWidth * 0.8, Orientation.HORIZONTAL);

                //计算位置
                if (renderRectangle.containPoint(renderPoint)) {
                    renderNote.pos = RenderNote.RenderNoteObject.HEAD;
                    renderNote.note = note;
                    renderNote.complexNote = null;
                    renderNote.key = FixedOrbitNotesKey.NOTE;
                    renderNote.renderRectangle = renderRectangle.clone();
                }
                drawImage();
            }
        }

        if (renderNote.key != null) {
            loadImage(getImage("normal-selected", renderNote.key));
            renderRectangle.copyOf(renderNote.renderRectangle);
            drawImage();
        }
    }

    private void drawNote(FixedOrbitNote note, DrawMode drawMode, ComplexNote complexNote) {
        double orbitWidth = (renderInfo.canvasWidth.get() / getInfo().orbits.get());
        double noteXPos = (getInfo().canvasWidth.get() / getInfo().orbits.get()) * note.orbit + orbitWidth / 2;
        double noteYPos = (renderInfo.timelinePosition.doubleValue() - note.timeStamp + getInfo().getJudgedLineTimeOffset()) * renderInfo.timelineZoom.doubleValue();


        if (note instanceof LongNote longNote) {
            //画线
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                FixedOrbitNotesKey fixedOrbitNotesKey = FixedOrbitNotesKey.LONG;
                loadImage(getImage(state, fixedOrbitNotesKey));
                //计算尺寸
                renderRectangle.scale(Pos.CENTER, orbitWidth * 0.16, Orientation.HORIZONTAL);
                renderRectangle.setHeight(VPos.CENTER, longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                //计算位置
                renderRectangle.setXY(Pos.BASELINE_CENTER,
                        noteXPos,
                        noteYPos - longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                if (renderRectangle.containPoint(renderPoint)) {
                    renderNote.pos = RenderNote.RenderNoteObject.BODY;
                    renderNote.note = longNote;
                    renderNote.complexNote = complexNote;
                    renderNote.key = fixedOrbitNotesKey;
                    renderNote.renderRectangle = renderRectangle.clone();
                }
                drawImage();
            }

            //末尾节点
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                FixedOrbitNotesKey fixedOrbitNotesKey = drawMode.equals(DrawMode.ONLY_NODE) ? FixedOrbitNotesKey.NODE : FixedOrbitNotesKey.END;
                loadImage(getImage(state, fixedOrbitNotesKey));

                renderRectangle.setXY(Pos.CENTER,
                        noteXPos,
                        noteYPos - longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                //计算尺寸
                renderRectangle.scale(Pos.CENTER, orbitWidth * (fixedOrbitNotesKey.equals(FixedOrbitNotesKey.NODE) ? 0.2 : 0.3), Orientation.HORIZONTAL);
                //计算位置
                if (renderRectangle.containPoint(renderPoint)) {
                    renderNote.pos = RenderNote.RenderNoteObject.FOOT;
                    renderNote.note = longNote;
                    renderNote.complexNote = complexNote;
                    renderNote.key = fixedOrbitNotesKey;
                    renderNote.renderRectangle = renderRectangle.clone();
                }
                drawImage();
            }
        } else if (note instanceof SlideNote slideNote) {
            //画线
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                FixedOrbitNotesKey fixedOrbitNotesKey = FixedOrbitNotesKey.SLIDE;
                loadImage(getImage(state, fixedOrbitNotesKey));
                //计算尺寸

                renderRectangle.scale(Pos.CENTER, orbitWidth * 0.16, Orientation.VERTICAL);
                renderRectangle.setXY(Pos.CENTER_LEFT,
                        (slideNote.slideArg > 0 ? noteXPos : noteXPos - orbitWidth * (Math.abs(slideNote.slideArg))),
                        noteYPos);
                renderRectangle.setWidth(HPos.LEFT, orbitWidth * (Math.abs(slideNote.slideArg)));
                //计算位置
                if (renderRectangle.containPoint(renderPoint)) {
                    renderNote.pos = RenderNote.RenderNoteObject.BODY;
                    renderNote.note = slideNote;
                    renderNote.complexNote = complexNote;
                    renderNote.key = fixedOrbitNotesKey;
                    renderNote.renderRectangle = renderRectangle.clone();
                }
                drawImage();
            }

            //画箭头
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                FixedOrbitNotesKey fixedOrbitNotesKey;
                if (drawMode.equals(DrawMode.ONLY_NODE)) {//节点
                    fixedOrbitNotesKey = FixedOrbitNotesKey.NODE;
                } else {//箭头
                    fixedOrbitNotesKey = (slideNote.slideArg > 0 ? FixedOrbitNotesKey.RIGHT : FixedOrbitNotesKey.LEFT);
                }
                loadImage(getImage(state, fixedOrbitNotesKey));

                //计算尺寸
                renderRectangle.setXY(Pos.CENTER,
                        noteXPos + slideNote.slideArg * orbitWidth,
                        noteYPos);
                renderRectangle.scale(Pos.CENTER, orbitWidth * (fixedOrbitNotesKey.equals(FixedOrbitNotesKey.NODE) ? 0.2 : 0.4), Orientation.HORIZONTAL);
                if (renderRectangle.containPoint(renderPoint)) {
                    renderNote.pos = RenderNote.RenderNoteObject.FOOT;
                    renderNote.note = slideNote;
                    renderNote.complexNote = complexNote;
                    renderNote.key = fixedOrbitNotesKey;
                    renderNote.renderRectangle = renderRectangle.clone();
                }
                drawImage();
            }
        }
    }

    private enum DrawMode {
        ONLY_NODE,
        ONLY_LINE,
        ALL
    }


}
