package team.zxorg.skin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;

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
    void drawComponent(  double width, double height,long time) {
        transform();
        Image currImage = (System.currentTimeMillis() % 1000 > 500 ? tex2 : tex);
        drawImage(currImage);
        //rr.drawImage(gc, currImage);
    }
}
