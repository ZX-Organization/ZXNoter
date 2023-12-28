package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.UISFrameAnimation;

public class HitComponentRender extends BaseComponentRender {

    int blend;

    UISFrameAnimation animation;

    public HitComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadComponent(UISComponent component) {
        blend = component.getInt("blend", 0);

        animation = new UISFrameAnimation(component, "frame", "frame2", "frame3");
        animation.addBehavior("frame", 1);
        animation.addBehavior(null, 800);
        animation.addBehavior("frame2", 4);
        animation.addBehavior("frame3", 1);
        animation.addBehavior(null, 800);
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {


        Image currImage = animation.getCurrentFrames();
        if (blend == 1) {
            gc.setGlobalBlendMode(BlendMode.LIGHTEN);
        } else if (blend == 2) {
            gc.setGlobalBlendMode(BlendMode.SCREEN);
        }
        rr.drawImage(gc, currImage);

    }
}
