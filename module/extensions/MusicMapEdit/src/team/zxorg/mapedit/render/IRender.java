package team.zxorg.mapedit.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public interface IRender {
    void render(GraphicsContext gc, double cw, double ch, long t);
}
