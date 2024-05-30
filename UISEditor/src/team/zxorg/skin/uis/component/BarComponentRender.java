package team.zxorg.skin.uis.component;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;

public class BarComponentRender extends AbstractComponentRenderer {
    private ExpressionVector pos1;
    private ExpressionVector pos2;
    private  ExpressionVector size1;
    private ExpressionVector size2;
    private double progress;
    RenderRectangle rr = new RenderRectangle();

    public BarComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {

    }

    @Override
    void reloadPosComponent() {
        pos1 = pos.clone();
        size1 = size.clone();
        pos2 = component.getExpressionVector("pos2");
        size2 = component.getExpressionVector("size2");
        if (!component.contains("size2"))
            size2 = size;
    }

    @Override
    void drawComponent(double width, double height, long time) {

        progress = time * 0.0001;
        //progress += 0.004;
        progress %= 1;

        progressCalculation(rr, progress + 1);
        drawImage(tex, rr);
        progressCalculation(rr, progress + 0.5);
        drawImage(tex, rr);
        progressCalculation(rr, progress);
        drawImage(tex, rr);
        progressCalculation(rr, progress - 0.5);
        drawImage(tex, rr);
        progressCalculation(rr, progress - 1);
        drawImage(tex, rr);


        gc.setFill(Color.HOTPINK);
        gc.fillRect(rr.getLeft() - 2, rr.getTop() - 2, 4, 4);


    }

    private void progressCalculation(RenderRectangle rr, double p) {

        double imgX = pos1.getX() + (pos2.getX() - pos1.getX()) * p;
        double imgY = pos1.getY() + (pos2.getY() - pos1.getY()) * p;
        double imgW = size1.getW() + (size2.getW() - size1.getW()) * p;
        double imgH = size1.getH() + (size2.getH() - size1.getH()) * p;
        rr.setPos(Pos.TOP_LEFT, imgX, imgY);
        rr.setSize(Pos.TOP_LEFT, imgW, imgH);
        pos.setX(rr.getLeft());
        pos.setY(rr.getTop());
        size.setW(rr.getWidth());
        size.setH(rr.getHeight());
        transform();

    }
}
