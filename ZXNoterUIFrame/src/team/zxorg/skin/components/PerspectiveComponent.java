package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.ElementRenderInterface;

public class PerspectiveComponent implements ElementRenderInterface {
    // 角度变量 角度为0时，画面平行 角度增大时画面上面变小，底部不变
    // 二次多项式的系数
    private static final double a = 3.7832580504765276e-05;
    private static final double b = -0.0041608166982776305;
    private static final double c = 0.558138532712179;


    // 计算补偿系数的方法
    public static double calculateCompensation(double angle) {
        return a * Math.pow(angle, 2) + b * angle + c;
    }

    public PerspectiveComponent(String angle) {

        //角度 10  补偿系数 0.52
        //角度 20  补偿系数 0.4915
        //角度 25  补偿系数 0.47725

        //角度 30  补偿系数 0.466
        //角度 40  补偿系数 0.453
        //角度 45  补偿系数 0.45
        //角度 50  补偿系数 0.445
        //角度 55  补偿系数 0.4434





        //角度 28  补偿系数 0.476
        //角度 40  补偿系数 0.486

        //计算补偿后的角度
        this.angle = Double.parseDouble(angle);
        System.out.println("补偿值: " + calculateCompensation(this.angle));
        this.angle *= calculateCompensation(this.angle);
    }

    double angle;

    @Override
    public void draw(GraphicsContext gc, double width, double height) {
        Canvas canvas3d = gc.getCanvas();
        PerspectiveTransform pt = new PerspectiveTransform();
        //计算透视

        // 计算透视，使用角度变量
        double perspectiveAngle = Math.toRadians(angle);
        double perspectiveFactor = Math.tan(perspectiveAngle);

        // 根据需求设置透视变换的参数
        double offsetX = perspectiveFactor * canvas3d.getWidth();
        pt.setUlx(offsetX);
        pt.setUly(0);
        pt.setUrx(canvas3d.getWidth() - offsetX);
        pt.setUry(0);


        pt.setLlx(0);
        pt.setLly(canvas3d.getHeight());
        pt.setLrx(canvas3d.getWidth());
        pt.setLry(canvas3d.getHeight());

        // 创建画布快照
        SnapshotParameters snapshotParams = new SnapshotParameters();
        snapshotParams.setFill(Color.TRANSPARENT);
        WritableImage snapshot = canvas3d.snapshot(snapshotParams, null);

        // 清除整个画布
        gc.clearRect(0, 0, canvas3d.getWidth(), canvas3d.getHeight());
        gc.setEffect(pt);
        // 绘制之前的快照
        gc.drawImage(snapshot, 0, 0);
        gc.setEffect(null);
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }
}
