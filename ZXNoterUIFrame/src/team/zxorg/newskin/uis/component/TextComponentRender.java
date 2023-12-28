package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;

public class TextComponentRender extends BaseComponentRender {
    String text;
    double fsize;

    public TextComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
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
    void reloadComponent(UISComponent component) {
        text = component.getString("text", "<<无文字>>");
        fsize=component.getDouble("fsize", 16);
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
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
