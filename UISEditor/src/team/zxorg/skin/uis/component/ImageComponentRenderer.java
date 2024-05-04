package team.zxorg.skin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;

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
        //System.out.println(this.getName() + " pos: " + pos + "    size: " + size);
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
    void drawComponent(double width, double height, long time) {
        //rr.drawImage(gc, tex);
        drawImage(tex);
    }

    @Override
    public String toString() {
        return "ImageComponentRender{" +
                "component=" + component +
                '}';
    }
}
