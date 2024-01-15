package skin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import skin.uis.UISComponent;
import skin.uis.UISFrameAnimation;

public class HitComponentRenderer extends AbstractComponentRenderer {



    UISFrameAnimation animation;

    public HitComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {


        animation = new UISFrameAnimation(component, "frame", "frame2", "frame3");
        Image image = animation.getFirstFrame();
        if (image!=null) {
            texSize.setW(image.getWidth());
            texSize.setH(image.getHeight());
        }
        animation.addBehavior("frame", 1);
        animation.addBehavior(null, 800);
        animation.addBehavior("frame2", 4);
        animation.addBehavior("frame3", 1);
        animation.addBehavior(null, 800);
    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    void drawComponent(  double width, double height,long time) {


        Image currImage = animation.getCurrentFrames();
        if (blend == 1) {
            gc.setGlobalBlendMode(BlendMode.LIGHTEN);
        } else if (blend == 2) {
            gc.setGlobalBlendMode(BlendMode.SCREEN);
        }
        //rr.drawImage(gc, currImage);

    }
}
