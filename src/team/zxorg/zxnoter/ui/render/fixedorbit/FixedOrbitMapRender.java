package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.render.basis.RenderPoint;
import team.zxorg.zxnoter.ui.render.basis.RenderRectangle;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitNotesKey;


public class FixedOrbitMapRender extends FixedOrbitRender {
    String state;//状态


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

                double orbitWidth = (renderInfo.canvasWidth.get() / orbits);


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

                loadRenderImage(getImage(state, FixedOrbitNotesKey.NOTE));
                //计算尺寸
                renderRectangleScale(orbitWidth, Orientation.HORIZONTAL);
                renderRectangle.scale(0.8, 0.8);
                //计算位置
                renderRectangleSetX(orbitWidth * (note.orbit) + orbitWidth / 2);
                renderRectangleSetY((renderInfo.timelinePosition.doubleValue() - note.timeStamp + getRenderInfo().getJudgedLineTimeOffset()) * renderInfo.timelineZoom.doubleValue());
                renderRectangleOffsetPositionByHalf(Pos.TOP_LEFT);
                if (point != null) {
                    if (renderRectangle.contains(point))
                        return new RenderNote(RenderNote.RenderNoteObject.BODY, note);
                } else
                    drawImage();

            }
        }
        return null;
    }

    private RenderNote drawNote(FixedOrbitNote note, DrawMode drawMode, RenderPoint point) {
        double orbitWidth = (renderInfo.canvasWidth.get() / orbits);
        double noteXPos = (getRenderInfo().canvasWidth.get() / (orbits)) * note.orbit + orbitWidth / 2;
        double noteYPos = (renderInfo.timelinePosition.doubleValue() - note.timeStamp + getRenderInfo().getJudgedLineTimeOffset()) * renderInfo.timelineZoom.doubleValue();


        if (note instanceof LongNote longNote) {
            //画线
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                loadRenderImage(getImage(state, FixedOrbitNotesKey.LONG));
                //计算尺寸
                renderRectangleScale(orbitWidth, Orientation.HORIZONTAL);
                renderRectangle.scale(0.8, 0.8);
                renderRectangle.setHeight(longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                //计算位置
                renderRectangleSetX(noteXPos);
                renderRectangleSetY(noteYPos - longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                renderRectangleOffsetPositionByHalf(Pos.CENTER_LEFT);
                if (point != null) {
                    if (renderRectangle.contains(point))
                        return new RenderNote(RenderNote.RenderNoteObject.BODY, longNote);
                } else
                    drawImage();
            }

            //末尾节点
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                loadRenderImage(getImage(state, (drawMode.equals(DrawMode.ONLY_NODE) ? FixedOrbitNotesKey.NODE : FixedOrbitNotesKey.END)));
                //计算尺寸
                renderRectangleScale(orbitWidth, Orientation.HORIZONTAL);
                renderRectangle.scale(0.8, 0.8);
                //计算位置
                renderRectangleSetX(noteXPos);
                renderRectangleSetY(noteYPos - longNote.sustainedTime * renderInfo.timelineZoom.doubleValue());
                renderRectangleOffsetPositionByHalf(Pos.TOP_LEFT);
                if (point != null) {
                    if (renderRectangle.contains(point))
                        return new RenderNote(RenderNote.RenderNoteObject.FOOT, longNote);
                } else
                    drawImage();
            }
        } else if (note instanceof SlideNote slideNote) {
            //画线
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                loadRenderImage(getImage(state, FixedOrbitNotesKey.SLIDE));
                //计算尺寸
                renderRectangleScale(orbitWidth, Orientation.HORIZONTAL);
                renderRectangle.scale(0.8, 0.8);
                renderRectangle.setWidth(orbitWidth * Math.abs(slideNote.slideArg));
                //计算位置
                renderRectangleSetX(noteXPos + (slideNote.slideArg < 0 ? slideNote.slideArg * orbitWidth : 0));
                renderRectangleSetY(noteYPos);
                renderRectangleOffsetPositionByHalf(Pos.TOP_CENTER);
                if (point != null) {
                    if (renderRectangle.contains(point))
                        return new RenderNote(RenderNote.RenderNoteObject.BODY, slideNote);
                } else
                    drawImage();
            }

            //画箭头
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                if (drawMode.equals(DrawMode.ONLY_NODE)) {//节点
                    loadRenderImage(getImage(state, FixedOrbitNotesKey.NODE));
                } else {//箭头
                    loadRenderImage(getImage(state, (slideNote.slideArg < 0 ? FixedOrbitNotesKey.LEFT : FixedOrbitNotesKey.RIGHT)));
                }
                //计算尺寸
                renderRectangleScale(orbitWidth, Orientation.HORIZONTAL);
                renderRectangle.scale(0.8, 0.8);
                //计算位置
                renderRectangleSetX(noteXPos + slideNote.slideArg * orbitWidth);
                renderRectangleSetY(noteYPos);
                renderRectangleOffsetPositionByHalf(Pos.TOP_LEFT);
                if (point != null) {
                    if (renderRectangle.contains(point))
                        return new RenderNote(RenderNote.RenderNoteObject.FOOT, slideNote);
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
