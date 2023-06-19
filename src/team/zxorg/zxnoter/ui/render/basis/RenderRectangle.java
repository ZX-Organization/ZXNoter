package team.zxorg.zxnoter.ui.render.basis;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * 渲染矩形 提供更加快捷的矩形运算
 */
public class RenderRectangle {
    @Override
    public String toString() {
        return "矩形{ " + pos + " 宽: " + width + " 高: " + height + " }";
    }

    private final RenderPoint pos = new RenderPoint();
    private double width, height;

    public double getX() {
        return pos.x;
    }

    public double getY() {
        return pos.y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        pos.x = x;
    }

    public void setY(double y) {
        pos.y = y;
    }


    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public RenderRectangle() {
        pos.x = 0;
        pos.y = 0;
        width = 0;
        height = 0;
    }

    public RenderRectangle(Image image) {
        pos.x = 0;
        pos.y = 0;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public RenderRectangle(Canvas canvas) {
        pos.x = 0;
        pos.y = 0;
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
    }

    public RenderRectangle(double x, double y, double width, double height) {
        pos.x = x;
        pos.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSize(Image image) {
        pos.x = 0;
        pos.y = 0;
        width = image.getWidth();
        height = image.getHeight();
    }

    public void setSize(Canvas canvas) {
        pos.x = 0;
        pos.y = 0;
        width = canvas.getWidth();
        height = canvas.getHeight();
    }

    /**
     * 检查坐标是否在矩形内
     */
    public boolean contains(double x, double y) {
        return x >= pos.x && x <= pos.x + width && y >= pos.y && y <= pos.y + height;
    }

    /**
     * 检查坐标是否在矩形内
     */
    public boolean contains(RenderPoint point) {
        return contains(point.x, point.y);
    }

    /**
     * 检查矩形是否和此矩形相交
     */
    public boolean intersects(RenderRectangle r) {
        return r.pos.x + width > pos.x && r.pos.y + height > pos.y && r.pos.x < pos.x + width && r.pos.y < pos.y + height;
    }

    /**
     * 获取矩形中心点
     */
    public RenderPoint getCenterPoint() {
        double centerX = pos.x + width / 2;
        double centerY = pos.y + height / 2;
        return new RenderPoint(centerX, centerY);
    }

    /**
     * 矩形是否为空
     */
    public boolean isEmpty() {
        return width == 0 || height == 0;
    }

    /**
     * 矩形平移
     */
    public void translate(double deltaX, double deltaY) {
        pos.x += deltaX;
        pos.y += deltaY;
    }

    /**
     * 矩形缩放
     *
     * @param scaleX 缩放系数X
     * @param scaleY 缩放系数Y
     */
    public void scale(double scaleX, double scaleY) {
        RenderPoint centerPos = getCenterPoint();
        width *= scaleX;
        height *= scaleY;
        pos.x = centerPos.x - width / 2;
        pos.y = centerPos.y - height / 2;
    }

    /**
     * 强制水平或垂直等比缩放
     *
     * @param image       图片尺寸
     * @param value       目标宽、高尺寸
     * @param orientation 水平或垂直
     */
    public void scale(Image image, double value, Orientation orientation) {
        double scaleX = 1.0;
        double scaleY = 1.0;

        if (orientation.equals(Orientation.HORIZONTAL)) {
            double originalWidth = image.getWidth();
            scaleX = value / originalWidth;
            scaleY = scaleX;
        } else if (orientation.equals(Orientation.VERTICAL)) {
            double originalHeight = image.getHeight();
            scaleY = value / originalHeight;
            scaleX = scaleY;
        }

        // 判断缩放系数，取较小的值保持等比缩放
        double scale = Math.min(scaleX, scaleY);

        // 根据计算的缩放系数对矩形进行缩放操作
        scale(scale, scale);
    }

    /**
     * 根据源区域的相对位置变更区域
     *
     * @param sourceRectangle 源对齐区域
     * @param pos             相对位置
     */
    public void setRelativePosition(RenderRectangle sourceRectangle, Pos pos) {
        switch (pos.getHpos()) {
            case CENTER -> this.pos.x = sourceRectangle.pos.x + (sourceRectangle.width - width) / 2; // 居中对齐
            case LEFT -> this.pos.x = sourceRectangle.pos.x; // 左对齐
            case RIGHT -> this.pos.x = sourceRectangle.pos.x + sourceRectangle.width - width; // 右对齐
        }

        switch (pos.getVpos()) {
            case CENTER -> this.pos.y = sourceRectangle.pos.y + (sourceRectangle.height - height) / 2; // 居中对齐
            case TOP -> this.pos.y = sourceRectangle.pos.y; // 上对齐
            case BOTTOM -> this.pos.y = sourceRectangle.pos.y + sourceRectangle.height - height; // 下对齐
        }
    }


    /**
     * 区域半偏移
     *
     * @param pos 偏移方向
     */
    public void offsetPositionByHalf(Pos pos) {
        switch (pos.getHpos()) {
            case LEFT -> this.pos.x -= width / 2;
            case RIGHT -> this.pos.x += width / 2;
        }
        switch (pos.getVpos()) {
            case TOP -> this.pos.y -= height / 2;
            case BOTTOM -> this.pos.y += height / 2;
        }
    }


}
