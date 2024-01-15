package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.ExpressionVector;
import skin.uis.UISComponent;

public class SkewAnimation extends AbstractAnimation {
    ExpressionVector from;
    ExpressionVector to;
    int type = 0;

    public SkewAnimation(UISComponent component, String animation, int type) {
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
        // 斜切角度
        double w = from.getW() + (to.getW() - from.getW()) * progress;
        double h = from.getH() + (to.getH() - from.getH()) * progress;

        ExpressionVector p = cr.skew;

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
