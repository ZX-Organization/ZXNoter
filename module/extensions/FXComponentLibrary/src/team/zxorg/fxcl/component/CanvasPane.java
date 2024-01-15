package team.zxorg.fxcl.component;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 * 画布容器
 */
public class CanvasPane extends Pane {
    private final static int UPDATE_DELAY_MILLISECONDS = 20;
    private long updateLastTime = 0;
    private boolean needUpdate = false;
    private Canvas canvas;

    public CanvasPane() {
        // 添加监听器，当父容器的大小发生变化时，重新布局
        widthProperty().addListener(evt -> {
            layoutUpdate();
        });
        heightProperty().addListener(evt -> {
            layoutUpdate();
        });
        layoutUpdate();
    }

    private void layoutUpdate() {
        if (updateLastTime < System.currentTimeMillis() - UPDATE_DELAY_MILLISECONDS) {
            needUpdate = false;
            updateLastTime = System.currentTimeMillis();

            Canvas newCanvas = new Canvas();
            newCanvas.setWidth(getWidth());
            newCanvas.setHeight(getHeight());
            // 设置新的 Canvas 为不可见
            newCanvas.setVisible(false);
            newCanvas.getGraphicsContext2D().setImageSmoothing(true);
            getChildren().add(newCanvas);
            // 在下一帧渲染之前显示新的 Canvas
            Platform.runLater(() -> {

                // 移除旧的 Canvas
                if (getChildren().size() > 1) {
                    //canvas.getGraphicsContext2D().save();
                    getChildren().removeFirst();
                }
                newCanvas.setVisible(true);
                canvas = newCanvas;  // 更新 canvas 引用
            });
        } else {
            needUpdate = true;
        }
    }


    /**
     * 返回与此 Canvas 关联的 GraphicsContext
     *
     * @return 与此 Canvas 关联的 GraphicsContext
     */
    public GraphicsContext getGraphicsContext2D() {
        if (needUpdate) layoutUpdate();
        return canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * 返回与此 Canvas 关联的 GraphicsContext 带有自动清除功能
     *
     * @return 与此 Canvas 关联的 GraphicsContext
     */
    public GraphicsContext gc() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return gc;
    }
}
