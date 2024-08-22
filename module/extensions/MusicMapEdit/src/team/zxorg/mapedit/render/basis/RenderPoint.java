package team.zxorg.mapedit.render.basis;

/**
 * 渲染点坐标
 */
public class RenderPoint {
    private double x, y;

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

    public void setXY(double x,double y){
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "坐标[" +
                "x: " + x +
                " y: " + y +
                ']';
    }
}
