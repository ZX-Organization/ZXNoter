package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

public class MoveAnimationRenderer extends AbstractAnimationRenderer {
    ExpressionVector from;
    ExpressionVector to;
    int type = 0;

    public MoveAnimationRenderer(UISComponent component, String animation, int type) {
        super(component, animation);
        this.type = type;
    }

    @Override
    void reload() {
        from = getExpressionVector("from");
        to = getExpressionVector("to");
    }


    @Override
    protected void draw(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
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
