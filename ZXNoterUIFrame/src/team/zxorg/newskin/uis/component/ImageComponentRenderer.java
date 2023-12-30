package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;

public class ImageComponentRenderer extends AbstractComponentRenderer {
    protected BlendMode blend;

    public ImageComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        if (!getName().startsWith("_"))
            tex = component.getImageOrNull("tex");
    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
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
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        rr.drawImage(gc, tex);
    }

    @Override
    public String toString() {
        return "ImageComponentRender{" +
                "component=" + component +
                '}';
    }
}
