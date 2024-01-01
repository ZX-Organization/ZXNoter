package team.zxorg.skin.basis;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * 元素渲染接口
 */
public interface RenderInterface {
    /**
     * 绘制
     *
     * @param gc     图形上下文
     * @param width  画布宽度
     * @param height 画布高度
     */
    default void draw(GraphicsContext gc, double width, double height) {

    }

    /**
     * 大小改变
     *
     * @param width  画布宽度
     * @param height 画布高度
     */
    default void resize(double width, double height) {

    }

    /**
     * 初始化
     *
     * @param canvas 所属画布对象
     */
    default void initialize(Canvas canvas) {

    }

    /**
     * 消息
     *
     * @param value 消息数据
     */
    default void message(Object value) {

    }

}
