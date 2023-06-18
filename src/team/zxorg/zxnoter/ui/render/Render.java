package team.zxorg.zxnoter.ui.render;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui.render.basis.RenderRectangle;


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
        if (!(canvas.getWidth() == 0 || canvas.getHeight() == 0) && canvas.isVisible())
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
     * 获取画布的宽度
     *
     * @return 宽度
     */
    public double getWidth() {
        return renderInfo.canvasWidth.get();
    }

    /**
     * 获取画布的高度
     *
     * @return 高度
     */
    public double getHeight() {
        return renderInfo.canvasHeight.get();
    }

    /**
     * 绘制图片 (根据需要自动跳过)
     *
     * @param image     图片
     * @param rectangle 图片矩形
     */
    public void drawImage(Image image, RenderRectangle rectangle) {
        //检查是否包含在画布内
        if (new RenderRectangle(0, 0, canvas.getWidth(), canvas.getHeight()).intersects(rectangle)) {
            graphics.drawImage(image, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

    }

}
