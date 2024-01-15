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
     * @param gc 图形上下文
     * @param cw 画布宽度
     * @param ch 画布高度
     * @param t  当前时间
     */
    default void draw(GraphicsContext gc, double cw, double ch, long t) {

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
