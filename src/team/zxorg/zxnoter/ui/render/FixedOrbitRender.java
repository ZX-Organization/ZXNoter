package team.zxorg.zxnoter.ui.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.ui.render.info.FixedOrbitRenderInfo;


public class FixedOrbitRender extends Render {
    private Canvas canvas;
    private GraphicsContext graphics;
    private FixedOrbitRenderInfo fixedOrbitRenderInfo;


    public FixedOrbitRender(Canvas canvas, ZXMap zxMap) {
        super(zxMap);
        this.canvas = canvas;
        graphics = canvas.getGraphicsContext2D();
        fixedOrbitRenderInfo = new FixedOrbitRenderInfo();
        renderInfo = fixedOrbitRenderInfo;
    }

    @Override
    void renderHandle() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        graphics.clearRect(0, 0, canvasWidth, canvasHeight);
        long stageHintTimeOffset = (long) (canvasHeight * stageHintPos / renderInfo.timelineZoom);


    }

    @Override
    boolean isInRenderRange(BaseNote note) {

        return false;
    }
}
