package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.UISComponent;

public class FadeAnimation extends AbstractAnimation {
    double from;
    double to;

    public FadeAnimation(UISComponent component, String animation) {
        super(component, animation);
    }

    @Override
    void reload() {
        from = getDouble("from", 100) / 100.;
        to = getDouble("to", 100) / 100.;
    }

    @Override
    protected void handle(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.opacity = from + (to - from) * progress;
    }
}
