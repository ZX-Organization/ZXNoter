package team.zxorg.skin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;

public class TextComponentRenderer extends AbstractComponentRenderer {
    String text;
    double fsize;

    public TextComponentRenderer(UISComponent component) {
        super(component);
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
    void reloadResComponent() {
        text = component.getString("text", "<<无文字>>");
        fsize=component.getDouble("fsize", 16);
    }

    @Override
    void reloadPosComponent() {

    }

    @Override
    void drawComponent(  double width, double height,long time) {
        gc.setFont(Font.font(fsize));
        gc.setFill(color);
        switch (anchor.getHpos()) {
            case LEFT -> gc.setTextAlign(TextAlignment.LEFT);
            case RIGHT -> gc.setTextAlign(TextAlignment.RIGHT);
            case CENTER -> gc.setTextAlign(TextAlignment.CENTER);
        }
        gc.setTextBaseline(anchor.getVpos());
        gc.fillText(text, pos.getX(), pos.getY());
    }
}
