package team.zxorg.zxnoter.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class FxUtils {
    public static Canvas canvasBind(Canvas canvas) {
        if (canvas.getParent() instanceof Pane pane) {
            canvas.widthProperty().bind(pane.widthProperty());
            canvas.heightProperty().bind(pane.heightProperty());
        }
        return canvas;
    }
}
