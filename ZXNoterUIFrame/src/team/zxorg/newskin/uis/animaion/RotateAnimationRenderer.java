package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

public class RotateAnimationRenderer extends AbstractAnimationRenderer{
    double from;
    double to;
    public RotateAnimationRenderer(UISComponent component, String animation) {
        super(component, animation);
    }

    @Override
    void reload() {
        from = getDouble("from",0);
        to = getDouble("to",0);
    }

    @Override
    protected void draw(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        cr.rotate = from + (to - from) * progress;
    }
}
