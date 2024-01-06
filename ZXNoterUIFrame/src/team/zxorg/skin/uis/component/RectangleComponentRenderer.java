package team.zxorg.skin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;

public class RectangleComponentRenderer extends AbstractComponentRenderer {
    public RectangleComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {

    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    public void initialize(Canvas canvas) {
        super.initialize(canvas);
    }

    @Override
    public void message(Object value) {
        super.message(value);
    }



    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height,long time) {
        gc.setFill(color);

        gc.fillRect(rr.getLeft(), rr.getTop(),rr.getWidth(), rr.getHeight());
    }
}
