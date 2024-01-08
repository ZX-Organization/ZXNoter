package team.zxorg.skin.uis;

import javafx.geometry.Point2D;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;

public class UISPerspectiveTransform {
    private static final double a = 3.7832580504765276e-05;
    private static final double b = -0.0041608166982776305;
    private static final double c = 0.558138532712179;
    private double width = 0;
    private double height = 0;

    private double angle;


    private float tx[][] = new float[3][3];
    private float ulx, uly, urx, ury, lrx, lry, llx, lly;
    private float devcoords[] = new float[8];
    private final PerspectiveTransformState state = new PerspectiveTransformState();


    //private final PerspectiveTransform pt = new PerspectiveTransform();
    // private final Rectangle rectangle = new Rectangle();

    public UISPerspectiveTransform() {
        //rectangle.setEffect(pt);
    }

    // 计算角度补偿系数  为了适应Malody
    private static double calculateCompensation(double angle) {
        return a * Math.pow(angle, 2) + b * angle + c;
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
        //rectangle.setWidth(width);
        //rectangle.setHeight(height);

        update();
    }

    public void setAngle(double angle) {
        this.angle = calculateCompensation(angle) * angle;
        update();
    }

    private void update() {
        // 计算透视，使用角度变量
        double perspectiveAngle = Math.toRadians(angle);
        double perspectiveFactor = Math.tan(perspectiveAngle);

        // 根据需求设置透视变换的参数
        double offsetX = perspectiveFactor * width;
       /* pt.setUlx(offsetX);
        pt.setUly(0);
        pt.setUrx(width - offsetX);
        pt.setUry(0);

        pt.setLlx(0);
        pt.setLly(height);
        pt.setLrx(width);
        pt.setLry(height);
*/

        setQuadMapping((float) offsetX, (float) 0, (float) (width - offsetX), (float) 0, (float) 0, (float) height, (float) width, (float) height);

        devcoords[0] = ulx;
        devcoords[1] = uly;
        devcoords[2] = urx;
        devcoords[3] = ury;
        devcoords[4] = lrx;
        devcoords[5] = lry;
        devcoords[6] = llx;
        devcoords[7] = lly;

        setUnitQuadMapping(devcoords[0], devcoords[1],
                devcoords[2], devcoords[3],
                devcoords[4], devcoords[5],
                devcoords[6], devcoords[7]);


    }


    public Point2D transform(Point2D p) {
        /*float sx = (p.x - b.getMinX()) / b.getWidth();
        float sy = (p.y - b.getMinY()) / b.getHeight();*/
        float sx = (float) ((p.getX() - 0) / width);
        float sy = (float) ((p.getY() - 0) / height);
        float dx = tx[0][0] * sx + tx[0][1] * sy + tx[0][2];
        float dy = tx[1][0] * sx + tx[1][1] * sy + tx[1][2];
        float dw = tx[2][0] * sx + tx[2][1] * sy + tx[2][2];
        p = new Point2D(dx / dw, dy / dw);
        return p;
    }

    /**
     * 通过渲染矩形变换3d后的场景坐标
     */
    @Deprecated
    public void transform(double x, double y, double x2, double y2, double x3, double y3, double x4, double y4) {
        System.out.println(transform(new Point2D( 0, 0)));
        System.out.println(transform(new Point2D( 1920, 0)));
        System.out.println(transform(new Point2D( 1920, 1080)));
        System.out.println(transform(new Point2D( 0, 1080)));

        ;
        //System.out.println(polygon.getPoints());
        /*System.out.println(y);
        System.out.println(rectangle.localToScreen(x, y));
        Point2D point = rectangle.localToScreen(x, y);
        setUlx(point.getX());
        setUly(point.getY());
        point = rectangle.localToScreen(x2, y2);
        setUrx(point.getX());
        setUry(point.getY());
        point = rectangle.localToScreen(x3, y3);
        setLlx(point.getX());
        setLly(point.getY());
        point = rectangle.localToScreen(x4, y4);
        setLrx(point.getX());
        setLry(point.getY());*/
    }


    public static void main(String[] args) {
        UISPerspectiveTransform transform = new UISPerspectiveTransform();
        transform.setSize(1920, 1080);
        transform.setAngle(45);
        transform.transform(50, 50, 100, 50, 50, 100, 100, 100);
        System.out.println(transform);
    }

    private void setUnitQuadMapping(float ulx, float uly,
                                    float urx, float ury,
                                    float lrx, float lry,
                                    float llx, float lly) {
        float dx3 = ulx - urx + lrx - llx;
        float dy3 = uly - ury + lry - lly;

        tx[2][2] = 1.0F;

        if ((dx3 == 0.0F) && (dy3 == 0.0F)) { // TODO: use tolerance (RT-27402)
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

    public final void setQuadMapping(float ulx, float uly,
                                     float urx, float ury,
                                     float lrx, float lry,
                                     float llx, float lly) {
        this.ulx = ulx;
        this.uly = uly;
        this.urx = urx;
        this.ury = ury;
        this.lrx = lrx;
        this.lry = lry;
        this.llx = llx;
        this.lly = lly;
    }
}
