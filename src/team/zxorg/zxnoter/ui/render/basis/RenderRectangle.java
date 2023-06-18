package team.zxorg.zxnoter.ui.render.basis;

public class RenderRectangle {
    private final RenderPoint min = new RenderPoint();
    private final RenderPoint max = new RenderPoint();
    private double width, height;

    public double getX() {
        return min.x;
    }

    public double getY() {
        return min.y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        min.x = Math.min(x, x + width);
        max.x = Math.max(x, x + width);
    }

    public void setY(double y) {
        min.y = Math.min(y, y + height);
        max.y = Math.max(y, y + height);
    }


    public void setWidth(double width) {
        this.width = width;
        min.x = Math.min(min.x, min.x + width);
        max.x = Math.max(max.x, max.x + width);
    }

    public void setHeight(double height) {
        this.height = height;
        min.y = Math.min(min.y, min.y + height);
        max.y = Math.max(max.y, max.y + height);
    }

    public RenderRectangle(double x, double y, double width, double height) {
        min.x = Math.min(x, x + width);
        max.x = Math.max(x, x + width);
        min.y = Math.min(y, y + height);
        max.y = Math.max(y, y + height);
        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    /**
     * 检查坐标是否在矩形内
     */
    public boolean contains(double x, double y) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y;
    }

    /**
     * 检查矩形是否和此矩形相交
     */
    public boolean intersects(RenderRectangle r) {
        return r.max.x > min.x && r.max.y > min.y && r.min.x < max.x && r.min.y < max.y;
    }

    /**
     * 获取矩形中心点
     */
    public RenderPoint getCenterPoint() {
        double centerX = (min.x + max.x) / 2;
        double centerY = (min.y + max.y) / 2;
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
        min.x += deltaX;
        max.x += deltaX;
        min.y += deltaY;
        max.y += deltaY;
    }

    /**
     * 矩形缩放
     * @param scaleX 缩放系数X
     * @param scaleY 缩放系数Y
     */
    public void scale(double scaleX, double scaleY) {
        double centerX = (min.x + max.x) / 2;
        double centerY = (min.y + max.y) / 2;
        double newWidth = width * scaleX;
        double newHeight = height * scaleY;
        min.x = centerX - newWidth / 2;
        max.x = centerX + newWidth / 2;
        min.y = centerY - newHeight / 2;
        max.y = centerY + newHeight / 2;
        width = newWidth;
        height = newHeight;
    }

    public void rotate(double angle) {
        double centerX = (min.x + max.x) / 2;
        double centerY = (min.y + max.y) / 2;
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double newX1 = centerX + (min.x - centerX) * cosTheta - (min.y - centerY) * sinTheta;
        double newY1 = centerY + (min.x - centerX) * sinTheta + (min.y - centerY) * cosTheta;
        double newX2 = centerX + (max.x - centerX) * cosTheta - (max.y - centerY) * sinTheta;
        double newY2 = centerY + (max.x - centerX) * sinTheta + (max.y - centerY) * cosTheta;
        min.x = Math.min(newX1, newX2);
        max.x = Math.max(newX1, newX2);
        min.y = Math.min(newY1, newY2);
        max.y = Math.max(newY1, newY2);
        width = max.x - min.x;
        height = max.y - min.y;
    }
}
