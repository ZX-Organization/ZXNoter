package team.zxorg.zxnoter.ui.render;

import javafx.css.converter.EffectConverter;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
     * 根据图片相对位置绘制
     *
     * @param image    图片
     * @param position 图片的相对位置
     */
    public void drawImageWithRelativePosition(Image image, double x, double y, double width, double height, Pos position) {
        // 根据位置微调绘制坐标
        if (position == Pos.TOP_RIGHT || position == Pos.CENTER_RIGHT || position == Pos.BOTTOM_RIGHT) {
            x -= width;
        } else if (position == Pos.TOP_CENTER || position == Pos.CENTER || position == Pos.BOTTOM_CENTER) {
            x -= width / 2;
        }
        if (position == Pos.BOTTOM_LEFT || position == Pos.BOTTOM_CENTER || position == Pos.BOTTOM_RIGHT) {
            y -= height;
        } else if (position == Pos.CENTER_LEFT || position == Pos.CENTER || position == Pos.CENTER_RIGHT) {
            y -= height / 2;
        }
        drawImage(image, new Rectangle2D(x, y, width, height));
    }

    /**
     * 根据画布的相对位置绘制
     *
     * @param image    图片
     * @param width    宽度
     * @param height   高度
     * @param position 相对位置
     */
    public void drawImageWithPosition(Image image, double width, double height, Pos position) {
        double x = calculateXWithPosition(width, position.getHpos());
        double y = calculateYWithPosition(height, position.getVpos());

        // 绘制图片
        drawImage(image, new Rectangle2D(x, y, width, height));
    }

    private double calculateXWithPosition(double width, HPos hPos) {
        if (hPos == HPos.CENTER) {
            return (getWidth() - width) / 2; // 居中对齐
        } else if (hPos == HPos.LEFT) {
            return 0; // 左对齐
        } else if (hPos == HPos.RIGHT) {
            return getWidth() - width; // 右对齐
        }
        return 0; // 默认左对齐
    }

    private double calculateYWithPosition(double height, VPos vPos) {
        if (vPos == VPos.CENTER) {
            return (getHeight() - height) / 2; // 居中对齐
        } else if (vPos == VPos.TOP) {
            return 0; // 上对齐
        } else if (vPos == VPos.BOTTOM) {
            return getHeight() - height; // 下对齐
        }
        return 0; // 默认上对齐
    }

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
    public void drawImage(Image image, Rectangle2D rectangle) {
        //检查是否包含在画布内
        if (new Rectangle2D(0, 0, canvas.getWidth(), canvas.getHeight()).intersects(rectangle)) {
            graphics.drawImage(image, rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
        }

    }

}
