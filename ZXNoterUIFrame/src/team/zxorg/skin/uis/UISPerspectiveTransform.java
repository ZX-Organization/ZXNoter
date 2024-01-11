package team.zxorg.skin.uis;

import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import javafx.geometry.Point2D;
import javafx.scene.effect.PerspectiveTransform;
import team.zxorg.zxncore.ZXLogger;

public class UISPerspectiveTransform {
    private static final double a = 3.7832580504765276e-05;
    private static final double b = -0.0041608166982776305;
    private static final double c = 0.558138532712179;
    private double width = 0;
    private double height = 0;

    private double angle;


    private final float[][] tx = new float[3][3];
    private final PerspectiveTransformState state = new PerspectiveTransformState();

    /**
     * 计算角度补偿系数  为了适应Malody
     *
     * @param angle 原始角度
     * @return 补偿后角度
     */
    private static double calculateCompensation(double angle) {
        return (a * Math.pow(angle, 2) + b * angle + c) * angle;
    }

    public void setSize(double width, double height) {
        //ZXLogger.info("设置3d大小: " + width + " " + height);
        this.width = width;
        this.height = height;
        update();
    }

    public void setFixedSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setAngle(double angle) {
        ZXLogger.info("设置3d角度: " + angle);
        this.angle = angle;
        update();
    }

    private void update() {
        // 计算透视，使用角度变量
        double perspectiveAngle = Math.toRadians(calculateCompensation(angle));
        double perspectiveFactor = Math.tan(perspectiveAngle);

        // 根据需求设置透视变换的参数
        double offsetX = perspectiveFactor * width;

        // 设置透视变换的参数
        setUnitQuadMapping(offsetX, 0,
                width - offsetX, 0,
                width, height,
                0, height);

    }

    public Point2D untransform(Point2D p) {
        float dx = (float) p.getX();
        float dy = (float) p.getY();
        float itx[][] = state.getITX();
        float sx = itx[0][0] * dx + itx[0][1] * dy + itx[0][2];
        float sy = itx[1][0] * dx + itx[1][1] * dy + itx[1][2];
        float sw = itx[2][0] * dx + itx[2][1] * dy + itx[2][2];
        p = new Point2D(0 + (sx / sw) * width,
                0 + (sy / sw) * height);
        return p;
    }

    public Point2D untransform(Point2D p, float minX, float minY, float width, float height) {
        float dx = (float) p.getX();
        float dy = (float) p.getY();
        float[][] itx = state.getITX();
        float sx = itx[0][0] * dx + itx[0][1] * dy + itx[0][2];
        float sy = itx[1][0] * dx + itx[1][1] * dy + itx[1][2];
        float sw = itx[2][0] * dx + itx[2][1] * dy + itx[2][2];
        p = new Point2D(minX + (sx / sw) * width,
                minY + (sy / sw) * height);
        return p;
    }

    public Point2D transform(Point2D p) {
        float sx = (float) ((p.getX() - 0) / width);
        float sy = (float) ((p.getY() - 0) / height);
        float dx = tx[0][0] * sx + tx[0][1] * sy + tx[0][2];
        float dy = tx[1][0] * sx + tx[1][1] * sy + tx[1][2];
        float dw = tx[2][0] * sx + tx[2][1] * sy + tx[2][2];
        p = new Point2D(dx / dw, dy / dw);
        return p;
    }

    public Point2D transform(Point2D p, float minX, float minY, float width, float height) {
        float sx = (float) ((p.getX() - minX) / width);
        float sy = (float) ((p.getY() - minY) / height);
        float dx = tx[0][0] * sx + tx[0][1] * sy + tx[0][2];
        float dy = tx[1][0] * sx + tx[1][1] * sy + tx[1][2];
        float dw = tx[2][0] * sx + tx[2][1] * sy + tx[2][2];
        p = new Point2D(dx / dw, dy / dw);
        return p;
    }

    public void setUnitQuadMapping(double ulx, double uly,
                                   double urx, double ury,
                                   double lrx, double lry,
                                   double llx, double lly) {
        setUnitQuadMapping((float) ulx, (float) uly, (float) urx, (float) ury, (float) lrx, (float) lry, (float) llx, (float) lly);
    }

    public void setUnitQuadMapping(float ulx, float uly,
                                   float urx, float ury,
                                   float lrx, float lry,
                                   float llx, float lly) {
        float dx3 = ulx - urx + lrx - llx;
        float dy3 = uly - ury + lry - lly;

        tx[2][2] = 1.0F;

        if ((dx3 == 0.0F) && (dy3 == 0.0F)) {
            tx[0][0] = urx - ulx;
            tx[0][1] = lrx - urx;
            tx[0][2] = ulx;
            tx[1][0] = ury - uly;
            tx[1][1] = lry - ury;
            tx[1][2] = uly;
            tx[2][0] = 0.0F;
            tx[2][1] = 0.0F;
        } else {
            float dx1 = urx - lrx;
            float dy1 = ury - lry;
            float dx2 = llx - lrx;
            float dy2 = lly - lry;

            float invdet = 1.0F / (dx1 * dy2 - dx2 * dy1);
            tx[2][0] = (dx3 * dy2 - dx2 * dy3) * invdet;
            tx[2][1] = (dx1 * dy3 - dx3 * dy1) * invdet;
            tx[0][0] = urx - ulx + tx[2][0] * urx;
            tx[0][1] = llx - ulx + tx[2][1] * llx;
            tx[0][2] = ulx;
            tx[1][0] = ury - uly + tx[2][0] * ury;
            tx[1][1] = lly - uly + tx[2][1] * lly;
            tx[1][2] = uly;
        }
        state.updateTx(tx);
    }


    public static void setPerspectiveTransform(PerspectiveTransform pt, Point2D ul, Point2D ur, Point2D ll, Point2D lr) {
        pt.setUlx(ul.getX());
        pt.setUly(ul.getY());
        pt.setUrx(ur.getX());
        pt.setUry(ur.getY());
        pt.setLlx(ll.getX());
        pt.setLly(ll.getY());
        pt.setLrx(lr.getX());
        pt.setLry(lr.getY());
    }


}
