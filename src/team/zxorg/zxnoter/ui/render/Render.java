package team.zxorg.zxnoter.ui.render;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    public RenderRectangle renderRectangle = new RenderRectangle();
    public RenderRectangle canvasRectangle = new RenderRectangle();
    public Image renderImage;

    /**
     * 获取渲染信息
     *
     * @return 渲染信息
     */
    protected abstract RenderInfo getInfo();


    public Render(RenderInfo renderInfo, ZXMap zxMap, Canvas canvas) {
        this.renderInfo = renderInfo;
        this.canvas = canvas;
        graphics = canvas.getGraphicsContext2D();
        this.zxMap = zxMap;
        renderInfo.canvasWidth.bind(canvas.widthProperty());
        renderInfo.canvasHeight.bind(canvas.heightProperty());
        renderInfo.canvasWidth.addListener((observable, oldValue, newValue) -> canvasRectangle.setWidth(HPos.LEFT, newValue.doubleValue()));
        renderInfo.canvasHeight.addListener((observable, oldValue, newValue) -> canvasRectangle.setHeight(VPos.TOP, newValue.doubleValue()));
    }

    /**
     * 执行渲染操作
     */
    public void render() {
        if (!(canvasRectangle.getWidth() == 0 || canvasRectangle.getHeight() == 0) && canvas.isVisible())
            renderHandle();
    }

    /**
     * 清除画布
     */
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
        if (canvasRectangle.isIntersection(rectangle)) {
            graphics.drawImage(image, rectangle.getLeft(), rectangle.getTop(), rectangle.getWidth(), rectangle.getHeight());
        }

    }

    /**
     * 载入渲染图片
     *
     * @param image 图片
     */
    public void loadImage(Image image) {
        renderImage = image;
        renderRectangle.setXY(Pos.TOP_LEFT, 0, 0);
        renderRectangle.setSize(Pos.TOP_LEFT, image.getWidth(), image.getHeight());
    }

    /**
     * 使用内置绘制图片
     */
    public void drawImage() {
        drawImage(renderImage, renderRectangle);
    }

}
