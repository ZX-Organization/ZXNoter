package team.zxorg.skin.basis;

import javafx.geometry.Orientation;
import javafx.scene.canvas.GraphicsContext;

public interface ElementRender {
    /**
     * 渲染处理
     *
     * @param gc     图形上下文
     * @param width  画布宽度
     * @param height 画布高度
     */
    void render(GraphicsContext gc, double width, double height);

    /**
     * 画布大小改变
     *
     * @param width       画布宽度
     * @param height      画布高度
     * @param orientation 变更方向
     */
    void canvasResized(double width, double height, Orientation orientation);
}
