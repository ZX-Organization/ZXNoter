package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitNotesKey;


public class FixedOrbitMapRender extends FixedOrbitRender {
    String state;//状态


    public FixedOrbitMapRender(FixedOrbitRenderInfo renderInfo, CanvasPane canvas, ZXMap zxMap, String state, String theme) {
        super(renderInfo, zxMap, canvas.canvas, theme);
        this.state = state;

    }




    @Override
    public void renderHandle() {
        //updateNoteBuffer();
        Image image;//临时存储图片

        //判定线时间偏移
        long judgedLineTimeOffset = getJudgedLineTimeOffset();

        for (int i = 0; i < renderZXMap.notes.size(); i++) {
            if (renderZXMap.notes.get(i) instanceof FixedOrbitNote note) {


                //检查是否在显示区域

                image = getImage(state, FixedOrbitNotesKey.NODE);

                double x = (canvasWidth / (orbits)) * note.orbit;
                double y = (renderInfo.timelinePosition - note.timeStamp + judgedLineTimeOffset) * renderInfo.timelineZoom;
                double w = (canvasWidth / (orbits));
                double h = w * (image.getHeight() / image.getWidth());


                //渲染对应键的额外内容
                if (note instanceof ComplexNote complexNote) {
                    for (int j = 0; j < complexNote.notes.size(); j++) {
                        drawNote(complexNote.notes.get(j), canvasWidth, (j >= complexNote.notes.size() - 1 ? DrawMode.ALL : DrawMode.ONLY_LINE), judgedLineTimeOffset);
                    }
                    for (int j = 0; j < complexNote.notes.size() - 1; j++) {
                        drawNote(complexNote.notes.get(j), canvasWidth, DrawMode.ONLY_NODE, judgedLineTimeOffset);
                    }
                } else {
                    drawNote(note, canvasWidth, DrawMode.ALL, judgedLineTimeOffset);
                }


                //绘制第一个键  (最后绘制)
                image = getImage(state, FixedOrbitNotesKey.NOTE);
                graphics.drawImage(image, x, y - h / 2, w, h);
            }
        }


    }

    private void drawNote(FixedOrbitNote note, double canvasWidth, DrawMode drawMode, double judgedLineTimeOffset) {
        Image image;

        double x2 = 0;//temp
        double y2 = 0;
        double w2 = 0;
        double h2 = 0;

        image = getImage(state, FixedOrbitNotesKey.NOTE);

        double x = (canvasWidth / (orbits)) * note.orbit;
        double y = (renderInfo.timelinePosition - note.timeStamp + judgedLineTimeOffset) * renderInfo.timelineZoom;
        double w = (canvasWidth / (orbits));
        double h = w * (image.getHeight() / image.getWidth());


        if (note instanceof LongNote longNote) {
            //画线
            image = getImage(state, FixedOrbitNotesKey.LONG);
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                if (isInRenderRange(x, y - longNote.sustainedTime * renderInfo.timelineZoom, w, longNote.sustainedTime * renderInfo.timelineZoom))
                    graphics.drawImage(image, x, y - longNote.sustainedTime * renderInfo.timelineZoom, w, longNote.sustainedTime * renderInfo.timelineZoom);
            }

            //末尾节点
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                h2 = w * (image.getHeight() / image.getWidth());
                if (isInRenderRange(x, y - longNote.sustainedTime * renderInfo.timelineZoom - h2 / 2, w, h2)) {
                    image = getImage(state, (drawMode.equals(DrawMode.ONLY_NODE) ? FixedOrbitNotesKey.NODE : FixedOrbitNotesKey.END));
                    graphics.drawImage(image, x, y - longNote.sustainedTime * renderInfo.timelineZoom - h2 / 2, w, h2);
                }

            }
        } else if (note instanceof SlideNote slideNote) {
            //画线
            image = getImage(state, FixedOrbitNotesKey.SLIDE);
            ;
            h2 = w * (image.getHeight() / image.getWidth());
            x2 = x + (slideNote.slideArg < 0 ? slideNote.slideArg * w : 0);
            w2 = Math.abs(slideNote.slideArg) * w;
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                if (isInRenderRange(x2 + w / 2, y - h2 / 2, w2, h2))
                    graphics.drawImage(image, x2 + w / 2, y - h2 / 2, w2, h2);
            }

            //画箭头
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {

                if (drawMode.equals(DrawMode.ONLY_NODE)) {
                    //节点
                    image = getImage(state, FixedOrbitNotesKey.NODE);
                } else {
                    //箭头
                    image = getImage(state, (slideNote.slideArg < 0 ? FixedOrbitNotesKey.LEFT : FixedOrbitNotesKey.RIGHT));
                }

                h2 = w * (image.getHeight() / image.getWidth());
                x2 = (slideNote.slideArg < 0 ? x2 - w / 2 : x2 + w / 2);
                if (isInRenderRange(x2 + w / 2, y - h2 / 2, w, h2))
                    graphics.drawImage(image, x2 + w / 2, y - h2 / 2, w, h2);
            }
        }
    }

    private enum DrawMode {
        ONLY_NODE,
        ONLY_LINE,
        ALL
    }

}
