package team.zxorg.skin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.UISFrame;

public class FrameAnimationComponentRenderer extends AbstractComponentRenderer {
    UISFrame frame;

    public FrameAnimationComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        frame = component.getFrame("frame");
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
        frame.update();
        rr.drawImage(gc, frame.getCurrentFrame());
    }
}
