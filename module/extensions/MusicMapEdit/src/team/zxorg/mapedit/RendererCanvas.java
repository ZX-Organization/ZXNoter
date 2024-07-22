package team.zxorg.mapedit;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import team.zxorg.fxcl.component.ResizableCanvas;
import team.zxorg.mapedit.render.basis.AbstractRender;

import java.util.LinkedHashMap;
import java.util.Map;

public class RendererCanvas extends ResizableCanvas {
    /**
     * 当前渲染时 毫秒
     */
    long currentTime = 0;
    /**
     * 画布宽度
     */
    double canvasWidth;
    /**
     * 画布高度
     */
    double canvasHeight;
    GraphicsContext gc;

    Map<String, AbstractRender> renders = new LinkedHashMap();
    AnimationTimer animationTimer;

    public RendererCanvas() {

        //监听宽高
        widthProperty().addListener((observable, oldValue, newValue) -> {
            canvasWidth = newValue.doubleValue();
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            canvasHeight = newValue.doubleValue();
        });
        gc = getGraphicsContext2D();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, canvasWidth, canvasHeight);
                for (AbstractRender r : renders.values()) {
                    r.render(gc, canvasWidth, canvasHeight, currentTime);
                }
            }
        };
    }


    public void addRender(String name, AbstractRender r) {
        renders.put(name, r);
    }

    public void removeRender(String name) {
        renders.remove(name);
    }

    /**
     * 开始渲染
     */
    public void start() {
        animationTimer.start();
    }

    /**
     * 停止渲染
     */
    public void stop() {
        animationTimer.stop();
    }
}
