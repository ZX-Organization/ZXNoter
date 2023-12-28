package team.zxorg.newskin.basis;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * 渲染矩形 提供更加快捷的矩形运算
 */
public class RenderRectangle {
    private double left, top, right, bottom, width, height;

    public RenderRectangle() {
    }

    public RenderRectangle(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        width = right - left;
        height = bottom - top;
    }

    @Override
    public String toString() {
        return "矩形[ x:" + left + " y:" + top + " w:" + width + " h:" + height + " ]";
    }

    @Override
    public RenderRectangle clone() {
        return new RenderRectangle(left, top, right, bottom);
    }

    public double getLeft() {
        return left;
    }

    public double getTop() {
        return top;
    }

    public double getRight() {
        return right;
    }

    public double getBottom() {
        return bottom;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCenterX() {
        return left + width / 2;
    }

    public double getCenterY() {
        return top + height / 2;
    }

    public boolean setLeft(double left) {
        if (left != this.left) {
            this.left = left;
            right = this.left + width;
            return true;
        }
        return false;
    }

    public boolean setTop(double top) {
        if (top != this.top) {
            this.top = top;
            bottom = this.top + height;
            return true;
        }
        return false;
    }

    public boolean setRight(double right) {
        if (right != this.right) {
            this.right = right;
            left = this.right - width;
            return true;
        }
        return false;
    }

    public boolean setBottom(double bottom) {
        if (bottom != this.bottom) {
            this.bottom = bottom;
            top = this.bottom - height;
            return true;
        }
        return false;
    }

    public void setX(HPos pos, double x) {
        if (setLeft(switch (pos) {
            case LEFT -> x;
            case CENTER -> x - width / 2;
            case RIGHT -> x - width;
        })) {
            right = left + width;
        }
    }

    public void setY(VPos pos, double y) {
        if (setTop(switch (pos) {
            case TOP, BASELINE -> y;
            case CENTER -> y - height / 2;
            case BOTTOM -> y - height;
        })) {
            bottom = top + height;
        }

    }


    public void setWidth(HPos pos, double width) {
        double newLeft = left - switch (pos) {
            case LEFT -> 0;
            case CENTER -> (width - this.width) / 2;
            case RIGHT -> width - this.width;
        };
        if (this.left != newLeft || this.width != width) {
            this.left = newLeft;
            this.width = width;
            right = left + width;
        }
    }

    public void setHeight(VPos pos, double height) {
        double newTop = top - switch (pos) {
            case TOP, BASELINE -> 0;
            case CENTER -> (height - this.height) / 2;
            case BOTTOM -> height - this.height;
        };

        if (this.top != newTop || this.height != height) {
            this.top = newTop;
            this.height = height;
            bottom = top + height;
        }
    }

    /**
     * 检查源坐标是否在此矩形内
     *
     * @param sourceX,sourceY 源坐标
     * @return 是否包含
     */
    public boolean containPoint(double sourceX, double sourceY) {
        return left < sourceX && top < sourceY && right > sourceX && bottom > sourceY;
    }

    /**
     * 检查源坐标是否在此矩形内
     *
     * @param sourcePoint 源坐标
     * @return 是否包含
     */
    public boolean containPoint(RenderPoint sourcePoint) {
        return left < sourcePoint.getX() && top < sourcePoint.getY() && right > sourcePoint.getX() && bottom > sourcePoint.getY();
    }


    /**
     * 检查源矩形是否在此矩形内
     *
     * @param sourceLeft,sourceTop,sourceRight,sourceBottom 源矩形
     * @return 是否包含
     */
    public boolean containRectangle(double sourceLeft, double sourceTop, double sourceRight, double sourceBottom) {
        return left <= sourceLeft && top <= sourceTop && right >= sourceRight && bottom >= sourceBottom;
    }

    /**
     * 检查源矩形是否在此矩形内
     *
     * @param sourceRectangle 源矩形
     * @return 是否包含
     */
    public boolean containRectangle(RenderRectangle sourceRectangle) {
        return left <= sourceRectangle.left && top <= sourceRectangle.top && right >= sourceRectangle.right && bottom >= sourceRectangle.bottom;
    }

    /**
     * 判断两个矩形是否相交
     *
     * @param sourceLeft,sourceTop,sourceRight,sourceBottom 源矩形
     * @return 是否相交
     */
    public boolean isRectCross(double sourceLeft, double sourceTop, double sourceRight, double sourceBottom) {
        return Math.max(left, sourceLeft) <= Math.min(right, sourceRight) && Math.max(top, sourceTop) <= Math.min(bottom, sourceBottom);
    }

    /**
     * 判断两个矩形是否相交
     *
     * @param sourceRectangle 源矩形
     * @return 是否相交
     */
    public boolean isIntersection(RenderRectangle sourceRectangle) {
        return Math.max(left, sourceRectangle.left) <= Math.min(right, sourceRectangle.right) && Math.max(top, sourceRectangle.top) <= Math.min(bottom, sourceRectangle.bottom);
    }

    /**
     * 获取矩形和此矩形相交后的矩形
     *
     * @param sourceLeft,sourceTop,sourceRight,sourceBottom 源矩形
     * @param intersectionRectangle                         相交的矩形
     */
    public void getIntersectionRectangle(double sourceLeft, double sourceTop, double sourceRight, double sourceBottom, RenderRectangle intersectionRectangle) {
        intersectionRectangle.left = Math.max(left, sourceLeft);
        intersectionRectangle.top = Math.max(top, sourceTop);
        intersectionRectangle.right = Math.min(right, sourceRight);
        intersectionRectangle.bottom = Math.min(bottom, sourceBottom);
        intersectionRectangle.width = intersectionRectangle.right - intersectionRectangle.left;
        intersectionRectangle.height = intersectionRectangle.bottom - intersectionRectangle.top;
    }


    /**
     * 获取矩形和此矩形相交后的矩形
     *
     * @param sourceLeft,sourceTop,sourceRight,sourceBottom 源矩形
     * @return 相交后的矩形
     */
    public RenderRectangle getIntersectionRectangle(double sourceLeft, double sourceTop, double sourceRight, double sourceBottom) {
        RenderRectangle intersectionRectangle = new RenderRectangle();
        getIntersectionRectangle(sourceLeft, sourceTop, sourceRight, sourceBottom, intersectionRectangle);
        return intersectionRectangle;
    }


    /**
     * 获取矩形和此矩形相交后的矩形
     *
     * @param sourceRectangle 源矩形
     * @return 相交后的矩形
     */
    public RenderRectangle getIntersectionRectangle(RenderRectangle sourceRectangle) {
        RenderRectangle intersectionRectangle = new RenderRectangle();
        getIntersectionRectangle(sourceRectangle.left, sourceRectangle.top, sourceRectangle.right, sourceRectangle.bottom, intersectionRectangle);
        return intersectionRectangle;
    }

    /**
     * 设置尺寸
     *
     * @param pos    位置
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(Pos pos, double width, double height) {
        setWidth(pos.getHpos(), width);
        setHeight(pos.getVpos(), height);
    }

    /**
     * 矩形等比缩放 (系数缩放)
     *
     * @param pos   位置
     * @param value 缩放系数
     */
    public void scale(Pos pos, double value) {
        setWidth(pos.getHpos(), width * value);
        setHeight(pos.getVpos(), height * value);
    }

    /**
     * 矩形等比缩放 (指定宽高)
     *
     * @param pos         位置
     * @param value       宽或高的值
     * @param orientation 指定方向
     */
    public void scale(Pos pos, double value, Orientation orientation) {

        double scale = switch (orientation) {
            case HORIZONTAL -> value / width;
            case VERTICAL -> value / height;
        };
        setWidth(pos.getHpos(), width * scale);
        setHeight(pos.getVpos(), height * scale);
    }


    public void setPos(Pos pos, double x, double y) {
        setX(pos.getHpos(), x);
        setY(pos.getVpos(), y);
    }

    /**
     * 设置为源矩形参数
     *
     * @param sourceRectangle 源矩形
     */
    public void setTo(RenderRectangle sourceRectangle) {
        this.left = sourceRectangle.left;
        this.top = sourceRectangle.top;
        this.right = sourceRectangle.right;
        this.bottom = sourceRectangle.bottom;
        this.width = sourceRectangle.width;
        this.height = sourceRectangle.height;
    }

    /**
     * 绘制图片
     *
     * @param gc    图形上下文
     * @param image 图片
     */
    public void drawImage(GraphicsContext gc, Image image) {
        gc.drawImage(image, getLeft(), getTop(), getWidth(), getHeight());
    }

    /**
     * 绘制图片 测试
     *
     * @param gc    图形上下文
     * @param image 图片
     */
    public void drawImageTest(GraphicsContext gc, Image image) {
        gc.save();
        gc.drawImage(image, getLeft(), getTop(), getWidth(), getHeight());
        gc.setStroke(Color.RED);
        gc.strokeLine(getLeft(), getTop() + getHeight() / 2, getLeft() + getWidth(), getTop() + getHeight() / 2);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(getLeft() + getWidth() / 2, getTop(), getLeft() + getWidth() / 2, getTop() + getHeight());
        gc.setStroke(Color.BLUE);
        gc.strokeRect(getLeft(), getTop(), getWidth(), getHeight());
        gc.restore();
    }

    /**
     * 绘制翻转图片
     *
     * @param gc    图形上下文
     * @param image 图片
     * @param flip  翻转方向 为null则不处理
     */
    public void drawImage(GraphicsContext gc, Image image, Orientation flip) {
        gc.save();
        if (flip == Orientation.VERTICAL) {
            gc.scale(1, -1);  // 垂直翻转
            gc.drawImage(image, getLeft(), -getTop() - getHeight(), getWidth(), getHeight());
        } else if (flip == Orientation.HORIZONTAL) {
            gc.scale(-1, 1);  // 水平翻转
            gc.drawImage(image, -getLeft() - getWidth(), getTop(), getWidth(), getHeight());
        } else {
            drawImage(gc, image);
        }
        gc.restore();
    }
}
