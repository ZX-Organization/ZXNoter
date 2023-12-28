package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.UISFrameAnimation;

public class JudgeComponentRender extends BaseComponentRender {
    UISFrameAnimation animation;

    public JudgeComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadComponent(UISComponent component) {
        animation = new UISFrameAnimation(component, "frame", "frame2", "frame3", "frame4");
        switch (type) {
            case 1 -> {
                animation.addBehavior("frame", 1);
            }
            case 2 -> {
                animation.addBehavior("frame", 1);
            }
            case 3 -> {
                animation.addBehavior("frame", 1);
                animation.addBehavior("frame2", 1);
                animation.addBehavior("frame3", 1);
                animation.addBehavior("frame4", 1);
            }
        }
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        rr.drawImage(gc,animation.getCurrentFrames());
    }
}
