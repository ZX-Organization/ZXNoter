package team.zxorg.mapedit.render.basis;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.mapedit.render.IRender;
import team.zxorg.mapedit.render.RenderTexture;

public abstract class AbstractRender extends RenderRectangle implements IRender {
    private GraphicsContext gc;
    private Effect effect;
    private Effect rootEffect;

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
        addEffect(shadow);
    }

    protected void addShadow(Color color, double w) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(w);
        dropShadow.setColor(color);
        addEffect(dropShadow);
    }

    protected void addColorAdjust(double hue) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        colorAdjust.setSaturation(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        addEffect(colorAdjust);
    }

    private void addEffect(Effect newEffect) {
        if (effect != null) {
            if (newEffect instanceof Shadow) {
                ((Shadow) newEffect).setInput(effect);
            } else if (newEffect instanceof DropShadow) {
                ((DropShadow) newEffect).setInput(effect);
            } else if (newEffect instanceof ColorAdjust) {
                ((ColorAdjust) newEffect).setInput(effect);
            }
        } else {
            rootEffect = newEffect;
        }
        effect = newEffect;
    }

    private void applyEffect() {
        if (rootEffect != null) {
            gc.setEffect(rootEffect);
        }
    }


    protected void drawImage(RenderTexture tex) {
        applyEffect();
        gc.drawImage(tex.get(), getLeft(), getTop(), getWidth(), getHeight());
    }

    protected abstract void draw(GraphicsContext gc, double cw, double ch, long t);

}
