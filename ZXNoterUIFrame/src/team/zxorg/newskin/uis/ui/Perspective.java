package team.zxorg.newskin.uis.ui;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Perspective {
    private static final double a = 3.7832580504765276e-05;
    private static final double b = -0.0041608166982776305;
    private static final double c = 0.558138532712179;
    double width = 0;
    double height = 0;
    PerspectiveTransform pt = new PerspectiveTransform();

    // 计算补偿系数的方法
    public static double calculateCompensation(double angle) {
        return a * Math.pow(angle, 2) + b * angle + c;
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
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
        pt.setUlx(offsetX);
        pt.setUly(0);
        pt.setUrx(width - offsetX);
        pt.setUry(0);


        pt.setLlx(0);
        pt.setLly(height);
        pt.setLrx(width);
        pt.setLry(height);
    }

    public PerspectiveTransform getEffect() {
        return pt;
    }

    double angle;

    public void draw(GraphicsContext gc, double width, double height) {
        Canvas canvas3d = gc.getCanvas();

        //计算透视


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
}
