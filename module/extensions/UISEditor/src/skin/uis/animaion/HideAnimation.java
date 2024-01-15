package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.UISComponent;

public class HideAnimation extends AbstractAnimation {
    boolean isHide = true;

    public HideAnimation(UISComponent component, String animation) {
        super(component, animation);
        isHide = animation.contains("hide");
    }

    @Override
    void reload() {

    }

    @Override
    protected void handle(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.hide = isHide;
    }
}
