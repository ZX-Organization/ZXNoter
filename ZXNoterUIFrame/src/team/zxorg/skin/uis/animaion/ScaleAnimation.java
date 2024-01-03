package team.zxorg.skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.component.AbstractComponentRenderer;

public class ScaleAnimation extends AbstractAnimation {
    ExpressionVector from;
    ExpressionVector to;
    int type = 0;

    public ScaleAnimation(UISComponent component, String animation, int type) {
        super(component, animation);
        this.type = type;
    }

    @Override
    void reload() {
        from = getExpressionVector("from");
        to = getExpressionVector("to");
    }

    @Override
    protected void handle(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        ExpressionVector p = cr.scale;
        double w = from.getWidth() + (to.getWidth() - from.getWidth()) * progress;
        double h = from.getHeight() + (to.getHeight() - from.getHeight()) * progress;
        switch (type) {
            case 0 -> {
                p.setW(w);
                p.setH(h);
            }
            case 1 -> {
                p.setW(w);
            }
            case 2 -> {
                p.setH(h);
            }
        }

    }
}
