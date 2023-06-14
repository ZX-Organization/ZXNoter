package team.zxorg.zxnoter.ui.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.zxnoter.map.ZXMap;

public abstract class Render {
    public final Canvas canvas;
    public double canvasWidth,canvasHeight;
    public final GraphicsContext graphics;
    public final ZXMap renderZXMap;
    public RenderInfo renderInfo;

    protected abstract RenderInfo getRenderInfo();


    public Render(RenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas) {
        this.renderInfo = renderInfo;
        this.canvas = canvas;
        graphics = canvas.getGraphicsContext2D();
        this.renderZXMap = renderZXMap;
    }

    /**
     * 执行渲染操作
     */
    public void render() {
        canvasWidth= canvas.getWidth();
        canvasHeight= canvas.getHeight();
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
