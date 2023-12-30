package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

public class FadeAnimationRender extends AbstractAnimationRenderer {
    double from;
    double to;

    public FadeAnimationRender(UISComponent component, String animation) {
        super(component, animation);
    }

    @Override
    void reload() {
        from = getDouble("from", 100) / 100.;
        to = getDouble("to", 100) / 100.;
    }

    @Override
    protected void draw(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.opacity = from + (to - from) * progress;
    }
}
