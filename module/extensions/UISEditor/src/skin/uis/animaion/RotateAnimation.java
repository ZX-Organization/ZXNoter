package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.UISComponent;

public class RotateAnimation extends AbstractAnimation {
    double from;
    double to;
    public RotateAnimation(UISComponent component, String animation) {
        super(component, animation);
    }

    @Override
    void reload() {
        from = getDouble("from",0);
        to = getDouble("to",0);
    }

    @Override
    protected void handle(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.rotate = from + (to - from) * progress;
    }
}
