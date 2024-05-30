package team.zxorg.skin.uis.component;

import javafx.scene.image.Image;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.UISFrameAnimation;

public class JudgeComponentRenderer extends AbstractComponentRenderer {
    UISFrameAnimation animation;

    public JudgeComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        animation = new UISFrameAnimation(component, "frame", "frame2", "frame3", "frame4");
        Image image = animation.getFirstFrame();
        if (image!=null) {
            texSize.setW(image.getWidth());
            texSize.setH(image.getHeight());
        }
        switch (type) {
            case 1, 2 -> {
                animation.addBehavior("frame", 1);
            }
            case 3 -> {
                animation.addBehavior("frame", 1);
                animation.addBehavior("frame2", 1);
                animation.addBehavior("frame3", 1);
                animation.addBehavior("frame4", 1);
            }
        }
    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    void drawComponent(double width, double height,long time) {

        transform();
        drawImage(animation.getCurrentFrame(time));
        //rr.drawImage(gc,animation.getCurrentFrames());
    }
}
