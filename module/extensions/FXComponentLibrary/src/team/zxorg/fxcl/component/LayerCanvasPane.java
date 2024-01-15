package team.zxorg.fxcl.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 画布容器
 */
public class LayerCanvasPane extends StackPane {
    private final HashMap<String, Canvas> canvasMap = new HashMap<>();
    private final ArrayList<Canvas> canvasList = new ArrayList<>();

    public LayerCanvasPane() {

    }

    /**
     * 获取与此 Canvas 关联的 GraphicsContext
     */
    public GraphicsContext getGraphicsContext2D(String key) {
        return getCanvas(key).getGraphicsContext2D();
    }

    public Canvas createCanvas(String key) {
        Canvas canvas = new ResizableCanvas();
        canvasList.add(canvas);
        canvasMap.put(key, canvas);
        getChildren().add(canvas);
        return canvas;
    }

    /**
     * 获取画布
     */
    public Canvas getCanvas(String key) {
        return canvasMap.get(key);
    }

    /**
     * 获取 GraphicsContext 并自动清除画布内容
     */
    public GraphicsContext gc(String key) {
        Canvas canvas = getCanvas(key);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return gc;
    }

    public void clearRect() {
        for (Canvas canvas : canvasList) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }
}
