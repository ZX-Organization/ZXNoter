package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

public class HideAnimationRenderer extends AbstractAnimationRenderer {
    boolean isHide = true;

    public HideAnimationRenderer(UISComponent component, String animation) {
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
