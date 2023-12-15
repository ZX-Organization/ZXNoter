package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.ElementRender;

public class PerspectiveComponent implements ElementRender {
    // 角度变量 角度为0时，画面平行 角度增大时画面上面变小，底部不变


    public PerspectiveComponent(String angle) {
        this.angle = Double.parseDouble(angle);
    }

    double angle ;

    @Override
    public void render(GraphicsContext gc, double width, double height) {
        Canvas canvas3d = gc.getCanvas();
        PerspectiveTransform pt = new PerspectiveTransform();
        //计算透视

        // 计算透视，使用角度变量
        double perspectiveAngle = Math.toRadians(angle);
        double perspectiveFactor = Math.tan(perspectiveAngle);

        // 根据需求设置透视变换的参数
        double offsetX = perspectiveFactor * canvas3d.getWidth() * 0.43;
         offsetX = perspectiveFactor * canvas3d.getWidth() * 0.40;
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
