package team.zxorg.mapedit.render.basis;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import team.zxorg.mapedit.render.IRender;
import team.zxorg.mapedit.render.RenderTexture;

public abstract class AbstractRender implements IRender {
    private GraphicsContext gc;
    private Effect effect;

    @Override
    public final void render(GraphicsContext gc, double cw, double ch, long t) {
        this.gc = gc;
        effect = null;
        gc.save();
        draw(gc, cw, ch, t);
        gc.restore();

    }

    protected void drawImage(Image tex, double x, double y, double w, double h) {
        if (tex == null || tex.isError()) return;
        gc.drawImage(tex, x, y, w, h);
    }

    protected void addFill(Color color) {
        Shadow shadow = new Shadow();
        shadow.setColor(color);
        effect = shadow;
    }

    protected void addShadow(Color color, double w) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(w);
        dropShadow.setHeight(w);
        dropShadow.setColor(color);
        effect = dropShadow;
    }

    protected void addColorAdjust(double hue) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        colorAdjust.setSaturation(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        effect = colorAdjust;
    }


    /**
     * 绘制图形
     *
     * @param tex 图形纹理
     */
    protected void drawImage(RenderTexture tex, RenderRectangle rr) {
        gc.drawImage(tex.get(), rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
    }

    protected void clearEffect() {
        gc.setEffect(null);
    }

    public abstract void onMouseEvent(MouseEvent event);

    protected abstract void draw(GraphicsContext gc, double cw, double ch, long t);

}
