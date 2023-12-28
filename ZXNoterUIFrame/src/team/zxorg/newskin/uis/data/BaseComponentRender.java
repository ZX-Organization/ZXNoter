package team.zxorg.newskin.uis.data;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.newskin.basis.ElementRenderInterface;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;

/**
 * 组件属性数据
 */
public abstract class BaseComponentRender implements ElementRenderInterface {
    /**
     * 元件名
     */
    protected String name;
    /**
     * 纹理图片
     */
    protected Image tex;
    /**
     * 位置
     */
    protected ExpressionVector pos;
    /**
     * 尺寸
     */
    protected ExpressionVector size;
    /**
     * 锚点
     */
    protected Pos anchor;
    /**
     * 填充颜色
     */
    protected Color color;
    /**
     * 基于锚点的旋转角 数值表示元件逆时针的转角,单位角度
     */
    protected int rotate;
    /**
     * 透明度 范围 0.-1., 1.表示不透明, 0.表示完全透明
     */
    protected double opacity;
    /**
     * 图层深度
     * 0为最底层, 10为note所在层, 大于100的元件将不受是否开启3D的影响
     */
    protected int zindex;
    /**
     * 元件类型
     */
    protected int type;
    /**
     * 元件索引值 如'_sprite-4'为 3
     */
    protected int index;
    protected UISComponent component;
    /**
     * 渲染矩形
     */
    private RenderRectangle rr = new RenderRectangle();

    private Shadow shadow = new Shadow();

    public BaseComponentRender(UISComponent component) {
        this.component = component;
    }

    private void reloadComponent() {
        name = component.getName();
        tex = component.getImage("tex");
        pos = component.getExpressionVector("pos");
        size = component.getExpressionVector("size");
        anchor = component.getAnchorPos("anchor");
        color = Color.web(component.getString("color", "#00000000"));
        rotate = component.getInt("rotate", 0);
        opacity = component.getInt("opacity", 100) / 100.;
        zindex = component.getInt("zindex", 0);
        type = component.getInt("type", 0);
        index = component.getIndex();
        shadow.setColor(color);
    }

    @Override
    final public void draw(GraphicsContext gc, double width, double height) {
        //检查是否被改动过，如果被改动过，重新加载元件信息
        if (component.isChanged()) {
            reloadComponent();
        }


        double proportionalWidth = size.getWidth();
        double proportionalHeight = size.getHeight();
        // 根据 高度\宽度 计算比例尺寸
        if (size.getWidth() == 0) {
            proportionalWidth = proportionalHeight * tex.getWidth() / tex.getHeight();
        } else if (size.getHeight() == 0) {
            proportionalHeight = proportionalWidth * tex.getHeight() / tex.getWidth();
        }
        rr.setSize(anchor, proportionalWidth, proportionalHeight);
        rr.setPos(anchor, pos.getX(), pos.getY());


        gc.save();
        if (rotate != 0) {
            // 进行旋转变换
            gc.translate(pos.getX(), pos.getY());
            gc.rotate(rotate);
            gc.translate(-pos.getX(), -pos.getY());
        }
        drawComponent(gc, rr, width, height);
        gc.applyEffect(shadow);

        gc.restore();
    }

    /**
     * 绘制组件
     *
     * @param gc     图形上下文
     * @param width  画布宽度
     * @param height 画布高度
     */
    abstract void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height);
}
