package team.zxorg.zxnoter.ui.render.basis;

public class RenderPoint {
    protected double x, y;

    public RenderPoint() {
        x = 0;
        y = 0;
    }

    public RenderPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
