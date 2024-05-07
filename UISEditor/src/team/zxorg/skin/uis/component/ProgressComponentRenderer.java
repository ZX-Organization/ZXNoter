package team.zxorg.skin.uis.component;

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
        progress += 0.001;
        progress %= 1;
        if (type == 0) {
            gc.save();
            gc.beginPath();
            gc.rect(pos.getX(), pos.getY(), size.getW() * progress, size.getH());
            gc.clip();
            drawImage(tex);
            // 恢复裁剪区域
            gc.restore();
        }
    }
}
