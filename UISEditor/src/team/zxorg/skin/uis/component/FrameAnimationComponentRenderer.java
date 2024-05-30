package team.zxorg.skin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
        Image image = frame.getFrame(0);
        if (image!=null) {
            texSize.setW(image.getWidth());
            texSize.setH(image.getHeight());
        }
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
    void drawComponent( double width, double height,long time) {
        transform();
        frame.update();
        drawImage( frame.getCurrentFrame());
    }
}
