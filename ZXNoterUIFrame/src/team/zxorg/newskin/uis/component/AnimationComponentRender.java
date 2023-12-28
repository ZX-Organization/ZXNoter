package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.UISFrame;

public class AnimationComponentRender extends BaseComponentRender {
    UISFrame frame;

    public AnimationComponentRender(UISComponent component) {
        super(component);
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
    void reloadComponent(UISComponent component) {
        frame = component.getFrame("frame");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        frame.update();
        rr.drawImage(gc, frame.getCurrentFrame());
    }
}
