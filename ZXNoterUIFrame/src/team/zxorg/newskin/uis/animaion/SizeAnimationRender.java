package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

public class SizeAnimationRender extends AbstractAnimationRenderer {
    ExpressionVector from;
    ExpressionVector to;
    int type = 0;

    public SizeAnimationRender(UISComponent component, String animation, int type) {
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
        ExpressionVector p = cr.size;
        double w = from.getWidth() + (to.getWidth() - from.getWidth()) * progress;
        double h = from.getHeight() + (to.getHeight() - from.getHeight()) * progress;
        switch (type) {
            case 0->{
                p.setW(w);
                p.setH(h);
            }
            case 1->{
                p.setW(w);
            }
            case 2->{
                p.setH(h);
            }
        }
    }
}
