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

    public ObjectProperty<RenderNote> singleRenderNote = new SimpleObjectProperty<>();//单个渲染键


    public FixedOrbitMapRender(FixedOrbitRenderInfo renderInfo, CanvasPane canvas, ZXMap zxMap, String state, String theme) {
        super(renderInfo, zxMap, canvas.canvas, theme);
        this.state = state;
    }


    @Override
    public void renderHandle() {
        drawAllNote(null);
    }

    public RenderNote drawAllNote(RenderPoint point) {
        RenderNote renderNote = null;
        for (int i = 0; i < zxMap.notes.size(); i++) {
            if (zxMap.notes.get(i) instanceof FixedOrbitNote note) {

                double orbitWidth = (renderInfo.canvasWidth.get() / getInfo().orbits.get());


                //渲染对应键的额外内容
                if (note instanceof ComplexNote complexNote) {
                    for (int j = 0; j < complexNote.notes.size(); j++) {
                        renderNote = drawNote(complexNote.notes.get(j), (j >= complexNote.notes.size() - 1 ? DrawMode.ALL : DrawMode.ONLY_LINE), point);
                        if (renderNote != null) {
                            renderNote.complexNote = complexNote;
                            return renderNote;
                        }

                    }
                    for (int j = 0; j < complexNote.notes.size() - 1; j++) {
                        renderNote = drawNote(complexNote.notes.get(j), DrawMode.ONLY_NODE, point);
                        if (renderNote != null) {
                            renderNote.complexNote = complexNote;
                            return renderNote;
                        }
                    }
                } else {
                    renderNote = drawNote(note, DrawMode.ALL, point);
                    if (renderNote != null)
                        return renderNote;
                }

                loadImage(getImage(state, FixedOrbitNotesKey.NOTE));
                //计算尺寸
                renderRectangle.setXY(Pos.CENTER,
                        orbitWidth * (note.orbit) + orbitWidth / 2,
                        (renderInfo.timelinePosition.doubleValue() - note.timeStamp + getInfo().getJudgedLineTimeOffset()) * renderInfo.timelineZoom.doubleValue());
                renderRectangle.scale(Pos.CENTER, orbitWidth * 0.8, Orientation.HORIZONTAL);

                //计算位置
                if (point != null) {
                    if (renderRectangle.containPoint(point))
                        return new RenderNote(RenderNote.RenderNoteObject.HEAD, note, null, FixedOrbitNotesKey.NOTE, renderRectangle);
                } else
                    drawImage();

            }
        }

        if (point == null) {

            renderNote = singleRenderNote.get();
            if (renderNote != null) {
                loadImage(getImage(state, renderNote.key));
                renderRectangle.copyOf(renderNote.renderRectangle);
                //renderRectangle.scale(Pos.CENTER, 1.2);
                drawImage();
            }
        }

        return null;
    }

    private RenderNote drawNote(FixedOrbitNote note, DrawMode drawMode, RenderPoint point) {
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
                if (point != null) {
                    if (renderRectangle.containPoint(point))
                        return new RenderNote(RenderNote.RenderNoteObject.BODY, longNote, null, fixedOrbitNotesKey, renderRectangle);
                } else
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
                if (point != null) {
                    if (renderRectangle.containPoint(point))
                        return new RenderNote(RenderNote.RenderNoteObject.FOOT, longNote, null, fixedOrbitNotesKey, renderRectangle);
                } else
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
                if (point != null) {
                    if (renderRectangle.containPoint(point))
                        return new RenderNote(RenderNote.RenderNoteObject.BODY, slideNote, null, fixedOrbitNotesKey, renderRectangle);
                } else
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
                if (point != null) {
                    if (renderRectangle.containPoint(point))
                        return new RenderNote(RenderNote.RenderNoteObject.FOOT, slideNote, null, fixedOrbitNotesKey, renderRectangle);
                } else
                    drawImage();
            }
        }
        return null;
    }

    private enum DrawMode {
        ONLY_NODE,
        ONLY_LINE,
        ALL
    }


}
