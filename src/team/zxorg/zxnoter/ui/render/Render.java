package team.zxorg.zxnoter.ui.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.zxnoter.map.ZXMap;

public abstract class Render {
    public final Canvas canvas;
    public final GraphicsContext graphics;
    public final ZXMap zxMap;
    public RenderInfo renderInfo;

    protected abstract RenderInfo getRenderInfo();


    public Render(RenderInfo renderInfo, ZXMap zxMap, Canvas canvas) {
        this.renderInfo = renderInfo;
        this.canvas = canvas;
        graphics = canvas.getGraphicsContext2D();
        this.zxMap = zxMap;
        renderInfo.canvasWidth.bind(canvas.widthProperty());
        renderInfo.canvasHeight.bind(canvas.heightProperty());
    }

    /**
     * 执行渲染操作
     */
    public void render() {
        if (!(canvas.getWidth() == 0 ||  canvas.getHeight() == 0) && canvas.isVisible())
            renderHandle();
    }

    public void clearRect() {
        //清除区域
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    /**
     * 渲染处理
     */
    protected abstract void renderHandle();

    /**
     * 是否在渲染范围内
     */
    public boolean isInRenderRange(double x, double y, double w, double h) {
        if (x + w < 0 || y + h < 0)
            return false;
        if ((x > canvas.getWidth() || y > canvas.getHeight()))
            return false;
        return true;
    }

}
