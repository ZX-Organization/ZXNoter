package team.zxorg.skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.component.AbstractComponentRenderer;

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
        double w = from.getWidth() + (to.getWidth() - from.getWidth()) * progress;
        double h = from.getHeight() + (to.getHeight() - from.getHeight()) * progress;

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
        /*ExpressionVector p = cr.pos;

        {
            // 斜切换算
            shearX = Math.tan(-Math.toRadians(shearX));
            shearY = Math.tan(-Math.toRadians(shearY));

            //补偿
            double x = p.getY() * shearY;
            double y = p.getX() * shearX;

            if (shearX != 0) {
                // 平移到斜切中心点
                gc.translate(-p.getX(), -p.getY()-y );
                // 应用透视变换
                gc.transform(1, shearX, 0, 1, 0, 0);
                gc.translate(p.getX(), p.getY()-y);
            }


            if (shearY != 0) {
                // 平移到斜切中心点
                gc.translate(-p.getX() - x, -p.getY());
                // 应用透视变换
                gc.transform(1, 0, shearY, 1, 0, 0);
                gc.translate(p.getX() - x, p.getY() );
            }


        }*/
    }
}
