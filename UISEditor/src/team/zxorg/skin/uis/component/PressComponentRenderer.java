package team.zxorg.skin.uis.component;

import javafx.scene.image.Image;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.UISFrameAnimation;

public class PressComponentRenderer extends AbstractComponentRenderer {

    UISFrameAnimation animation;

    public PressComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        animation = new UISFrameAnimation(component, "frame", "frame2");
        Image image = animation.getFirstFrame();
        if (image!=null) {
            texSize.setW(image.getWidth());
            texSize.setH(image.getHeight());
        }
        animation.addBehavior("frame", 1);
        if (component.contains("frame2")) {
            animation.addBehavior(null, 500);
            animation.addBehavior("frame2", 1);
            animation.addBehavior(null, 1000);
        }else {
            animation.addBehavior(null, 1000);
        }

    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    void drawComponent( double width, double height,long time) {
        transform();
        Image currImage = animation.getCurrentFrame(time);
        drawImage(currImage);
        //rr.drawImage(gc, currImage);
    }
}
