package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.ExpressionVector;
import skin.uis.UISComponent;

public class MoveAnimation extends AbstractAnimation {
    ExpressionVector from;
    ExpressionVector to;
    int type = 0;

    public MoveAnimation(UISComponent component, String animation, int type) {
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
        ExpressionVector p = cr.pos;
        double x = from.getX() + (to.getX() - from.getX()) * progress;
        double y = from.getY() + (to.getY() - from.getY()) * progress;
        switch (type) {
            case 0 -> {
                p.setX(x);
                p.setY(y);
            }
            case 1 -> {
                p.setX(x);
            }
            case 2 -> {
                p.setY(y);
            }
        }
    }


}
