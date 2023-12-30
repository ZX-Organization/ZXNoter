package team.zxorg.newskin.uis.component;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.newskin.basis.RenderInterface;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.ui.component.LayerCanvasPane;

/**
 * 组件属性数据
 */
public abstract class AbstractComponentRenderer implements RenderInterface {
    /**
     * 父元件
     */
    protected AbstractComponentRenderer parent;


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
    public ExpressionVector pos;
    /**
     * 尺寸
     */
    public ExpressionVector size;
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
    public double rotate;
    /**
     * 透明度 范围 0.-1., 1.表示不透明, 0.表示完全透明
     */
    public double opacity;
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
    private final RenderRectangle rr = new RenderRectangle();
    private Shadow shadow;

    protected Orientation flip;

    public ExpressionVector scale;
    UISComponent motion;

    public boolean hide;

    public AbstractComponentRenderer(UISComponent component) {
        this.component = component;
        reloadResComponent_();
    }

    public final UISComponent getComponent() {
        return component;
    }

    /**
     * top 3d bottom
     *
     * @return
     */
    public final String getLayoutName() {
        return (getZindex() < 1 ? "bottom" : (getZindex() < 99 ? "3d" : "top"));
    }

    public UISComponent getMotion() {
        return motion;
    }

    public final String getName() {
        return name;
    }

    public final int getZindex() {
        return zindex;
    }

    private void reloadResComponent_() {
        name = component.getName();
        tex = component.getImage("tex");
        anchor = component.getAnchorPos("anchor");
        if (component.contains("color")) {
            color = Color.web(component.getString("color", "#00000000"));
            shadow = new Shadow(BlurType.ONE_PASS_BOX, color, 0);
            shadow.setColor(color);
        }
        rotate = component.getDouble("rotate", 0);
        opacity = component.getInt("opacity", 100) / 100.;
        zindex = component.getInt("zindex", zindex);
        type = component.getInt("type", 0);
        index = component.getIndex();
        scale = component.getExpressionVector("scale");
        if (!component.contains("scale")) {
            scale.setW(1);
            scale.setH(1);
        }

        flip = component.getOrientation("flip");
        rr.setFlip(flip);
        motion = component.getSkin().getComponent(":" + component.getString("motion", ""));
        //parent=component.
        reloadPosComponent_();
        reloadResComponent();
    }

    abstract void reloadResComponent();

    public void reloadPos() {
        reloadPosComponent_();
    }

    private void reloadPosComponent_() {
        pos = component.getExpressionVector("pos");
        size = component.getExpressionVector("size");
        reloadPosComponent();
    }

    abstract void reloadPosComponent();

    @Override
    final public void draw(GraphicsContext gc, double width, double height) {
        //检查是否被改动过，如果被改动过，重新加载元件信息
        if (component.isChanged()) {
            reloadResComponent_();
        }

        double proportionalWidth = size.getWidth() * scale.getWidth();
        double proportionalHeight = size.getHeight() * scale.getHeight();
        // 根据 高度\宽度 计算比例尺寸
        if (size.getWidth() == 0) {
            proportionalWidth = proportionalHeight * tex.getWidth() / tex.getHeight();
        } else if (size.getHeight() == 0) {
            proportionalHeight = proportionalWidth * tex.getHeight() / tex.getWidth();
        }
        rr.setSize(anchor, proportionalWidth, proportionalHeight);
        rr.setPos(anchor, pos.getX(), pos.getY());


        gc.save();

        if (color != null) {
            gc.setEffect(shadow);
        }

        if (rotate != 0) {
            // 进行旋转变换
            gc.translate(pos.getX(), pos.getY());
            gc.rotate(rotate);
            gc.translate(-pos.getX(), -pos.getY());
        }
        gc.setGlobalAlpha(opacity);
        if (!hide)
            drawComponent(gc, rr, width, height);
        if (color != null) {
            gc.setEffect(null);
        }

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


    /**
     * 组件转到渲染器
     *
     * @param component name
     */
    public static AbstractComponentRenderer toRenderer(UISComponent component, LayerCanvasPane layerCanvasPane) {
        AbstractComponentRenderer r;
        if (component.getName().startsWith("_")) {
            r = switch (component.getInt("type", 0)) {
                case 0 -> new ImageComponentRenderer(component);
                case 1 -> new TextComponentRenderer(component);
                case 2 -> new RectangleComponentRenderer(component);
                case 3 -> new FrameAnimationComponentRenderer(component);
                default -> null;
            };

        } else if (component.getName().startsWith(":")) {
            r = new AnimationComponentRenderer(component);
        } else {
            r = switch (component.getName()) {
                case "note" -> new NoteComponentRenderer(component);
                case "key" -> new KeyComponentRenderer(component);
                case "hit" -> new HitComponentRenderer(component);
                case "press" -> new PressComponentRenderer(component);
                case "judge" -> new JudgeComponentRenderer(component);
                case "pause", "bar" -> new ImageComponentRenderer(component);
                case "touch" -> new TouchComponentRenderer(component);
                case "score-combo", "score-score", "score-acc", "score-maxcombo" ->
                        new ScoreComponentRenderer(component);
                case "score-hp", "progress" -> new ProgressComponentRenderer(component);
                default -> null;
            };
        }
        if (r != null) {
            r.initialize(layerCanvasPane.getCanvas(r.getLayoutName()));
        }
        return r;
    }

}
