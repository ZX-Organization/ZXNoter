package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;

public class KeyComponentRenderer extends AbstractComponentRenderer {
    Image tex2;

    public KeyComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        tex2 = component.getImage("tex2");
    }

    @Override
    void reloadPosComponent() {

    }


    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        Image currImage = (System.currentTimeMillis() % 1000 > 500 ? tex2 : tex);
        rr.drawImage(gc, currImage);
    }
}
