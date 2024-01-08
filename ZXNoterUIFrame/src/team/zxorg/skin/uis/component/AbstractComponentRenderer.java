package team.zxorg.skin.uis.component;

import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.RenderInterface;
import team.zxorg.skin.uis.ExpressionCalculator;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.ui.component.LayerCanvasPane;

/**
 * 组件属性数据
 */
public abstract class AbstractComponentRenderer implements RenderInterface {
    protected ExpressionCalculator ec;

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
    /**
     * 原始组件
     */
    protected UISComponent component;
    /**
     * 渲染矩形
     */
    //private final RenderRectangle rr = new RenderRectangle();
    private Shadow shadow;
    /**
     * 翻转
     */
    protected Orientation flip;
    /**
     * 缩放
     */
    public ExpressionVector scale;
    /**
     * 动画组件
     */
    private UISComponent motion;
    /**
     * 是否隐藏
     */
    public boolean hide;
    /**
     * 倾斜
     */
    public ExpressionVector skew;
    /**
     * 材质尺寸
     */
    protected ExpressionVector texSize;
    /**
     * 像素倍率
     */
    protected double pixelMagnification;

    /**
     * 渲染上下文
     */
    GraphicsContext gc;

    public AbstractComponentRenderer(UISComponent component) {
        this.component = component;
        ec = component.expressionCalculator;
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
    /*public String getLayoutName() {
        return (getZindex() < 1 ? "bottom" : (getZindex() < 99 ? "3d" : "top"));
    }*/

    /**
     * 是否是3d图层
     *
     * @return 是否受到3d
     */
    public boolean is3DLayout() {
        return (getZindex() >= 1 & getZindex() < 99);
    }

    public String getLayoutName() {
        return "view";
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
        texSize = component.getExpressionVector("___texSize");
        name = component.getName();
        tex = component.getImageOrNull("tex");
        if (tex != null) {
            texSize.setW(tex.getWidth());
            texSize.setH(tex.getHeight());
        }

        if (component.contains("color")) {
            color = Color.web(component.getString("color", "#00000000"));
            shadow = new Shadow(BlurType.ONE_PASS_BOX, color, 0);
            shadow.setColor(color);
        }
        reloadStyle();

        type = component.getInt("type", 0);
        index = component.getIndex();

        //rr.setFlip(flip);
        motion = component.getSkin().getComponent(":" + component.getString("motion", "notfound"));
        //parent=component.
        reloadStyle();
        reloadPosComponent_();
        reloadResComponent();
    }

    abstract void reloadResComponent();

    public void reloadPos() {
        reloadPosComponent_();
    }

    public final void reloadStyle() {
        pixelMagnification = component.expressionCalculator.getPixelMagnification();
       /* pos = component.getExpressionVector("pos");
        size = component.getExpressionVector("size");*/

        rotate = component.getDouble("rotate", 0);
        opacity = component.getInt("opacity", 100) / 100.;
        scale = component.getExpressionVector("scale", "1px,1px");
        skew = component.getExpressionVector("skew");

        zindex = component.getInt("zindex", zindex);

        hide = false;
    }

    private void reloadPosComponent_() {
        pixelMagnification = component.expressionCalculator.getPixelMagnification();
        pos = component.getExpressionVector("pos");
        size = component.getExpressionVector("size");

        flip = component.getOrientation("flip");
        anchor = component.getAnchorPos("anchor");

        double proportionalWidth = size.getW();
        double proportionalHeight = size.getH();


        // 根据 高度\宽度 计算比例尺寸
        if (proportionalWidth == 0) {
            proportionalWidth = proportionalHeight * (texSize.getW() / texSize.getH());
        } else if (proportionalHeight == 0) {
            proportionalHeight = proportionalWidth * (texSize.getH() / texSize.getW());
        }

        proportionalWidth *= scale.getW();
        proportionalHeight *= scale.getH();
        size.setW(proportionalWidth);
        size.setH(proportionalHeight);

        reloadPosComponent();
    }

    abstract void reloadPosComponent();

    @Override
    final public void draw(GraphicsContext gc, double width, double height, long time) {
        //检查是否被改动过，如果被改动过，重新加载元件信息
        if (component.isChanged()) {
            reloadResComponent_();
        }
        this.gc = gc;


        /*rr.setSize(Pos.TOP_LEFT, proportionalWidth, proportionalHeight);
        rr.setPos(Pos.TOP_LEFT, pos.getX(), pos.getY());*/

        gc.save();

        if (color != null) {
            gc.setEffect(shadow);
        }

        gc.setGlobalAlpha(opacity);


        transform();

        if (!hide)
            drawComponent(width, height, time);
        if (color != null) {
            gc.setEffect(null);
        }

        gc.restore();
    }

    protected void drawImage(Image tex) {
        if (tex != null)
            drawImage(tex, pos.getX(), pos.getY(), size.getW(), size.getH());
    }

    protected void drawImage(Image tex, double x, double y, double w, double h) {
        if (is3DLayout()) {
            Point2D p1 = ec.transform(new Point2D(x, y));
            Point2D p2 = ec.transform(new Point2D(x + w, y));
            Point2D p3 = ec.transform(new Point2D(x, y + h));
            Point2D p4 = ec.transform(new Point2D(x + w, y + h));
            PerspectiveTransform perspectiveTransform = new PerspectiveTransform(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY());
            gc.setEffect(perspectiveTransform);
            /*PerspectiveTransform transform = ec.transform(x, y, w, h);
            gc.setEffect(transform);
*/
        }
        gc.drawImage(tex, x, y, w, h);
        if (is3DLayout()) {
            gc.setEffect(null);
        }
        //gc.drawImage(tex, x, y, w, h);
    }

    public void transform() {

        double anchorX = switch (anchor.getHpos()) {
            case LEFT -> 0;
            case CENTER -> size.getW() / 2;
            case RIGHT -> size.getW();
        };

        double anchorY = switch (anchor.getVpos()) {
            case TOP -> 0;
            case CENTER, BASELINE -> size.getH() / 2;
            case BOTTOM -> size.getH();
        };


        // 旋转变换
        if (rotate != 0) {
            gc.translate(pos.getX(), pos.getY());
            gc.rotate(rotate);
            gc.translate(-pos.getX(), -pos.getY());
        }

        //斜切变换
        if (skew.getW() != 0 || skew.getH() != 0) {
            //计算比例
            double texWidth = texSize.getW();
            double texHeight = texSize.getH();
            double renderWidth = size.getW();
            double renderHeight = size.getH();

            // 计算宽高比
            double textureAspectRatio = texWidth / texHeight;
            double renderAspectRatio = renderWidth / renderHeight;

            // 斜切角度 shear是倾斜度数
            double shearX = skew.getH();
            double shearY = skew.getW();

            {
                // 斜切换算
                shearX = Math.tan(-Math.toRadians(shearX));
                shearY = Math.tan(-Math.toRadians(shearY));

                // 根据宽高比调整斜切
                shearX *= textureAspectRatio / renderAspectRatio;
                shearY *= renderAspectRatio / textureAspectRatio;

                //补偿
                double x = pos.getY() * shearY;
                double y = pos.getX() * shearX;

                if (shearX != 0) {
                    // 平移到斜切中心点
                    gc.translate(-pos.getX(), -pos.getY() - y);
                    // 应用透视变换
                    gc.transform(1, shearX, 0, 1, 0, 0);
                    gc.translate(pos.getX(), pos.getY() - y);
                }


                if (shearY != 0) {
                    // 平移到斜切中心点
                    gc.translate(-pos.getX() - x, -pos.getY());
                    // 应用透视变换
                    gc.transform(1, 0, shearY, 1, 0, 0);
                    gc.translate(pos.getX() - x, pos.getY());
                }
            }
        }

        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(pos.getX() - 1, pos.getY() - 1, 3, 3);

        //锚点
        gc.translate(-anchorX, -anchorY);

        //翻转
        if (flip == Orientation.VERTICAL) {
            gc.scale(1, -1);  // 垂直翻转
            gc.translate(0, -pos.getY() * 2 - size.getH());
        } else if (flip == Orientation.HORIZONTAL) {
            gc.scale(-1, 1);  // 水平翻转
            gc.translate(-pos.getX() * 2 - size.getW(), 0);
        }


    }

    /**
     * 绘制组件
     *
     * @param width  画布宽度
     * @param height 画布高度
     */
    abstract void drawComponent(double width, double height, long time);


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
                case 4 -> new Scale3ComponentRender(component);
                default -> null;
            };

        } else if (component.getName().startsWith(":")) {
            r = new AnimationComponentRenderer(component);
        } else {
            r = switch (component.getName()) {
                case "note" -> new NoteComponentRenderer(component);
                case "key" -> new KeyComponentRenderer(component);
                case "hit", "hit-fast", "hit-slow" -> new HitComponentRenderer(component);
                case "press" -> new PressComponentRenderer(component);
                case "judge" -> new JudgeComponentRenderer(component);
                case "pause" -> new ImageComponentRenderer(component);
                case "bar" -> new BarComponentRender(component);
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
