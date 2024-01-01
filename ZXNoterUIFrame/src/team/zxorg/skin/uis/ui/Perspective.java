package team.zxorg.skin.uis.ui;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class Perspective {
    private static final double a = 3.7832580504765276e-05;
    private static final double b = -0.0041608166982776305;
    private static final double c = 0.558138532712179;
    double width = 0;
    double height = 0;
    PerspectiveTransform pt = new PerspectiveTransform();
    Affine affine = new Affine();

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


}
