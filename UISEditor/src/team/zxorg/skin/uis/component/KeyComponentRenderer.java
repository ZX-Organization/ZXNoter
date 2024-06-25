package team.zxorg.skin.uis.component;

import javafx.scene.image.Image;
import team.zxorg.skin.uis.UISComponent;

public class KeyComponentRenderer extends AbstractComponentRenderer {
    Image tex2;
    int i;

    public KeyComponentRenderer(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        tex2 = component.getImage("tex2");
    }

    @Override
    void reloadPosComponent() {
        i = component.getIndex();

    }


    @Override
    void drawComponent(double width, double height, long time) {


        transform();
        Image currImage = ((System.currentTimeMillis() + i * 100L) % 1000 > 500 ? tex2 : tex);
        drawImage(currImage);
        //rr.drawImage(gc, currImage);
    }
}
