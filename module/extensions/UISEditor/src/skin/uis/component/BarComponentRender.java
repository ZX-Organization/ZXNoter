package skin.uis.component;

import skin.basis.RenderRectangle;
import skin.uis.ExpressionVector;
import skin.uis.UISComponent;

public class BarComponentRender extends AbstractComponentRenderer {
    private ExpressionVector pos2;
    private ExpressionVector size2;
    private double progress;

    public BarComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {

    }

    @Override
    void reloadPosComponent() {
        pos2 = component.getExpressionVector("pos2");
        size2 = component.getExpressionVector("size2");
    }

    @Override
    void drawComponent( double width, double height,long time) {
        progress += 0.004;
        progress %= 1;

        //progressCalculation(rr, progress + 1);
        //rr.drawImage(gc, tex);
        //progressCalculation(rr, progress + 0.5);
        //rr.drawImage(gc, tex);
        //progressCalculation(rr, progress);
        //rr.drawImage(gc, tex);
        //progressCalculation(rr, progress - 0.5);
        //rr.drawImage(gc, tex);
        //progressCalculation(rr, progress - 1);
        //rr.drawImage(gc, tex);

    }

    private void progressCalculation(RenderRectangle rr, double p) {
        double imgX = pos.getX() + (pos2.getX() - pos.getX()) * p;
        double imgY = pos.getY() + (pos2.getY() - pos.getY()) * p;
        double imgW = size.getW() + (size2.getW() - size.getW()) * p;
        double imgH = size.getH() + (size2.getH() - size.getH()) * p;
        rr.setPos(anchor, imgX, imgY);
        rr.setSize(anchor, imgW, imgH);
    }
}
