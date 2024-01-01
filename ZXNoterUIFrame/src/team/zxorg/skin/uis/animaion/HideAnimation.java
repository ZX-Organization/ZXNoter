package team.zxorg.skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.component.AbstractComponentRenderer;

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
    protected void draw(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.hide = isHide;
    }
}
