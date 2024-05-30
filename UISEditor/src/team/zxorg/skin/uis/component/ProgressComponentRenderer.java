package team.zxorg.skin.uis.component;

import javafx.geometry.Point2D;
import team.zxorg.skin.uis.UISComponent;

public class ProgressComponentRenderer extends AbstractComponentRenderer {
    double progress;

    public ProgressComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {

    }

    @Override
    void reloadPosComponent() {
    }

    @Override
    void drawComponent(double width, double height, long time) {
        transform();
        //progress += 0.01;
        progress = time * 0.0001;

        progress %= 1;
        if (type == 0) {

            Point2D a1 = affine.transform(pos.getX(), pos.getY());
            Point2D a2 = affine.transform(pos.getX() + size.getW() * progress, pos.getY() + size.getH());
            gc.save();
            gc.beginPath();
            gc.rect(a1.getX(), a1.getY(), a2.getX(), a2.getY());
            gc.clip();
            drawImage(tex);
            // 恢复裁剪区域
            gc.restore();
        }
    }
}
