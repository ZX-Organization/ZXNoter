package team.zxorg.zxnoter_old.ui.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class CanvasPane extends Pane {
    public Canvas canvas;
    public CanvasPane() {
        canvas = new Canvas();
        getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
    }
}