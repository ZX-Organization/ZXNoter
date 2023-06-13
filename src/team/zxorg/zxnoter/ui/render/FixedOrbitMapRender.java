package team.zxorg.zxnoter.ui.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.render.info.FixedOrbitRenderInfo;

import java.util.function.Consumer;


public class FixedOrbitMapRender extends MapRender {

    private GraphicsContext graphics;
    public FixedOrbitRenderInfo fixedOrbitRenderInfo;


    public FixedOrbitMapRender(CanvasPane canvas, ZXMap zxMap,String key) {
        super(zxMap);
        this.canvas = canvas.canvas;
        graphics = this.canvas.getGraphicsContext2D();
        fixedOrbitRenderInfo = new FixedOrbitRenderInfo(key);
        renderInfo = fixedOrbitRenderInfo;
    }

    @Override
    void renderHandle() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        Image image;//临时存储图片

        //判定线时间偏移
        long judgedLineTimeOffset = (long) (canvasHeight * fixedOrbitRenderInfo.judgedLinePosition / renderInfo.timelineZoom);

        for (int i = 0; i < renderZXMap.notes.size(); i++) {
            if (renderZXMap.notes.get(i) instanceof FixedOrbitNote note) {


                //检查是否在显示区域


                double x = (canvasWidth / (fixedOrbitRenderInfo.orbits)) * note.orbit;
                double y = (renderInfo.timelinePosition - note.timeStamp + judgedLineTimeOffset) * renderInfo.timelineZoom;
                double w = (canvasWidth / (fixedOrbitRenderInfo.orbits));
                double h = w * (fixedOrbitRenderInfo.nodeImage.getHeight() / fixedOrbitRenderInfo.nodeImage.getWidth());


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
                graphics.drawImage(fixedOrbitRenderInfo.noteImage, x, y - h / 2, w, h);
            }
        }

        //绘制判定线
        image = fixedOrbitRenderInfo.judgedLineImage;
        graphics.drawImage(image, 0, canvasHeight * fixedOrbitRenderInfo.judgedLinePosition - image.getHeight() / 2, canvasWidth, image.getHeight());


    }

    private void drawNote(FixedOrbitNote note, double canvasWidth, DrawMode drawMode, double judgedLineTimeOffset) {
        Image image;

        double x2 = 0;//temp
        double y2 = 0;
        double w2 = 0;
        double h2 = 0;


        double x = (canvasWidth / (fixedOrbitRenderInfo.orbits)) * note.orbit;
        double y = (renderInfo.timelinePosition - note.timeStamp + judgedLineTimeOffset) * renderInfo.timelineZoom;
        double w = (canvasWidth / (fixedOrbitRenderInfo.orbits));
        double h = w * (fixedOrbitRenderInfo.nodeImage.getHeight() / fixedOrbitRenderInfo.nodeImage.getWidth());


        if (note instanceof LongNote longNote) {
            //画线
            image = fixedOrbitRenderInfo.longImage;
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                graphics.drawImage(image, x, y - longNote.sustainedTime * renderInfo.timelineZoom, w, longNote.sustainedTime * renderInfo.timelineZoom);
            }

            //末尾节点
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {
                h2 = w * (image.getHeight() / image.getWidth());
                graphics.drawImage(drawMode.equals(DrawMode.ONLY_NODE) ? fixedOrbitRenderInfo.nodeImage : fixedOrbitRenderInfo.endImage, x, y - longNote.sustainedTime * renderInfo.timelineZoom - h2 / 2, w, h2);
            }
        } else if (note instanceof SlideNote slideNote) {
            //画线
            image = fixedOrbitRenderInfo.slideImage;
            h2 = w * (image.getHeight() / image.getWidth());
            x2 = x + (slideNote.slideArg < 0 ? slideNote.slideArg * w : 0);
            w2 = Math.abs(slideNote.slideArg) * w;
            if (!drawMode.equals(DrawMode.ONLY_NODE)) {
                graphics.drawImage(image, x2 + w / 2, y - h2 / 2, w2, h2);
            }

            //画箭头
            if (!drawMode.equals(DrawMode.ONLY_LINE)) {

                if (drawMode.equals(DrawMode.ONLY_NODE)) {
                    //节点
                    image = fixedOrbitRenderInfo.nodeImage;
                } else {
                    //箭头
                    image = (slideNote.slideArg < 0 ? fixedOrbitRenderInfo.leftImage : fixedOrbitRenderInfo.rightImage);
                }

                h2 = w * (image.getHeight() / image.getWidth());
                x2 = (slideNote.slideArg < 0 ? x2 - w / 2 : x2 + w / 2);
                graphics.drawImage(image, x2 + w / 2, y - h2 / 2, w, h2);
            }
        }
    }

    private enum DrawMode {
        ONLY_NODE,
        ONLY_LINE,
        ALL
    }

    @Override
    boolean isInRenderRange(BaseNote note) {

        return false;
    }
}
