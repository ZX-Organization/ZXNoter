package team.zxorg.newskin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;

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
    protected void draw(GraphicsContext gc, double width, double height, double progress, AbstractComponentRenderer cr) {
        // 斜切角度
        double shearY = from.getWidth() + (to.getWidth() - from.getWidth()) * progress;
        double shearX = from.getHeight() + (to.getHeight() - from.getHeight()) * progress;
        ExpressionVector p = cr.pos;
        ExpressionVector s = cr.size;


        // 斜切换算
        shearX = Math.tan(-Math.toRadians(shearX));
        shearY = Math.tan(-Math.toRadians(shearY));


        double x = p.getY() * shearY;
        double y = p.getX() * shearX;
        // 平移到斜切中心点
        gc.translate(-p.getX() - x, -p.getY() - y);
        // 应用透视变换
        gc.transform(1, shearX, shearY, 1, 0, 0);
        gc.translate(p.getX() - x, p.getY() - y);

    }
}
