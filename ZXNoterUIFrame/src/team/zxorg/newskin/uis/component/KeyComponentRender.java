package team.zxorg.newskin.uis.component;

import javafx.geometry.Orientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;

public class KeyComponentRender extends BaseComponentRender {
    Image tex2;

    public KeyComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadComponent(UISComponent component) {
        tex2 = component.getImage("tex2");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        Image currImage = (System.currentTimeMillis() % 1000 > 500 ? tex2 : tex);
        rr.drawImage(gc, currImage);
    }
}
