package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.UISFrame;

public class ProgressComponentRenderer extends AbstractComponentRenderer {
    UISFrame frame;
    ExpressionVector pos2;
    int count;
    int type2;
    double progress;

    public ProgressComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        frame = component.getFrame("frame");
        count = component.getInt("count", 0);
        type2 = component.getInt("type2", -1);
    }

    @Override
    void reloadPosComponent() {
        pos2 = component.getExpressionVector("pos2");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        progress += 0.01;
        progress %= 1;
        if (type == 0) {

            gc.save();
            gc.beginPath();
            gc.rect(rr.getLeft(), rr.getTop(), rr.getWidth() * progress, rr.getHeight());
            gc.clip();

            rr.drawImage(gc, tex);
            // 恢复裁剪区域
            gc.restore();
        }
    }
}
