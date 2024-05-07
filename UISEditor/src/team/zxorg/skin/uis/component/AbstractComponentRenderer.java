package team.zxorg.skin.uis.component;

import com.sun.javafx.geom.Line2D;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import team.zxorg.skin.basis.RenderInterface;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.ExpressionCalculator;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.UISPerspectiveTransform;
import team.zxorg.ui.component.LayerCanvasPane;

/**
 * 组件属性数据
 */
public abstract class AbstractComponentRenderer implements RenderInterface {


    protected Affine affine = new Affine();
    protected ExpressionCalculator ec;
    PerspectiveTransform pt = new PerspectiveTransform();
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
     * 混合模式 1=additive 2=screen
     */
    int blend;
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
        blend = component.getInt("blend", 0);
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

        switch (blend) {
            case 0 -> {
                gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            }
            case 1 -> {
                gc.setGlobalBlendMode(BlendMode.ADD);
            }
            case 2 -> {
                gc.setGlobalBlendMode(BlendMode.SCREEN);
            }
        }

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
    protected void drawImage(Image tex, RenderRectangle rr){
        if (tex != null)
            drawImage(tex, rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
    }
    protected void drawImage(Image tex, double x, double y, double w, double h) {
        if (tex == null || tex.isError())
            return;

        //进行基本变换
        Point2D ul = affine.transform(x, y);
        Point2D ur = affine.transform(x + w, y);
        Point2D lr = affine.transform(x + w, y + h);
        Point2D ll = affine.transform(x, y + h);

        //进行3d变换
        if (is3DLayout()) {
            ul = ec.transform(ul);
            ur = ec.transform(ur);
            lr = ec.transform(lr);
            ll = ec.transform(ll);
        }

        // 添加坐标检查
        if (Double.isNaN(ul.getX()) || Double.isNaN(ul.getY()) ||
                Double.isNaN(ur.getX()) || Double.isNaN(ur.getY()) ||
                Double.isNaN(lr.getX()) || Double.isNaN(lr.getY()) ||
                Double.isNaN(ll.getX()) || Double.isNaN(ll.getY())) {
            return;
        }


        boolean isToBig = false;

        int maxSize = 2000;
        //限制图片尺寸
        if (Math.abs(ul.getX()) > maxSize || Math.abs(ul.getY()) > maxSize ||
                Math.abs(ur.getX()) > maxSize || Math.abs(ur.getY()) > maxSize ||
                Math.abs(lr.getX()) > maxSize || Math.abs(lr.getY()) > maxSize ||
                Math.abs(ll.getX()) > maxSize || Math.abs(ll.getY()) > maxSize) {
            /*ZXLogger.info("图片尺寸过大，无法绘制: " + ul.getX() + "," + ul.getY() + "," + ur.getX() + "," + ur.getY() + "," + lr.getX() + "," + lr.getY() + "," + ll.getX() + "," + ll.getY());
            return;*/
            isToBig = true;
        }

        // 判断绘制的图片是否在屏幕内
        double canvasWidth = ec.getCanvasWidth();
        double canvasHeight = ec.getCanvasHeight();

        // 判断绘制的图片是否完全不在屏幕内
        if ((ul.getX() < 0 && ur.getX() < 0 && lr.getX() < 0 && ll.getX() < 0) ||
                (ul.getX() > canvasWidth && ur.getX() > canvasWidth && lr.getX() > canvasWidth && ll.getX() > canvasWidth) ||
                (ul.getY() < 0 && ur.getY() < 0 && lr.getY() < 0 && ll.getY() < 0) ||
                (ul.getY() > canvasHeight && ur.getY() > canvasHeight && lr.getY() > canvasHeight && ll.getY() > canvasHeight)) {
            //return;
        }


        //将坐标应用到变换
        pt.setUlx(ul.getX());
        pt.setUly(ul.getY());
        pt.setUrx(ur.getX());
        pt.setUry(ur.getY());
        pt.setLrx(lr.getX());
        pt.setLry(lr.getY());
        pt.setLlx(ll.getX());
        pt.setLly(ll.getY());


        // 尺寸检查和限制
        w = Math.min(w, 2000);
        h = Math.min(h, 2000);


        if (isToBig) {
            //裁剪渲染  只渲染屏幕内的部分 裁剪掉屏幕外的优化性能

            // 图像源尺寸
            double sw = tex.getWidth();  // 使用整个图像的宽度
            double sh = tex.getHeight(); // 使用整个图像的高度

            //材质变换
            UISPerspectiveTransform texPt = new UISPerspectiveTransform();
            texPt.setFixedSize(sw, sh);
            texPt.setUnitQuadMapping(ul.getX(), ul.getY(), ur.getX(), ur.getY()
                    , lr.getX(), lr.getY(), ll.getX(), ll.getY());


            //图形上边
            Line2D lineU = new Line2D((float) ul.getX(), (float) ul.getY(), (float) ur.getX(), (float) ur.getY());
            //距离左上角距离
            double dist = lineU.ptLineDist(0, 0);


            //限制在屏幕内的图形区域
            Point2D inUL = new Point2D(Math.max(0, Math.min(canvasWidth, ul.getX())), Math.max(0, Math.min(canvasHeight, ul.getY())));
            Point2D inUR = new Point2D(Math.max(0, Math.min(canvasWidth, ur.getX())), Math.max(0, Math.min(canvasHeight, ur.getY())));
            Point2D inLR = new Point2D(Math.max(0, Math.min(canvasWidth, lr.getX())), Math.max(0, Math.min(canvasHeight, lr.getY())));
            Point2D inLL = new Point2D(Math.max(0, Math.min(canvasWidth, ll.getX())), Math.max(0, Math.min(canvasHeight, ll.getY())));


            // 计算屏幕内的图形区域在材质中的位置
            Point2D texUL = texPt.untransform(ul);
            Point2D texUR = texPt.untransform(ur);
            Point2D texLR = texPt.untransform(lr);
            Point2D texLL = texPt.untransform(ll);


            //后面就不会写了

            double texMinX = Math.min(texUL.getX(), Math.min(texUR.getX(), Math.min(texLR.getX(), texLL.getX())));
            double texMinY = Math.min(texUL.getY(), Math.min(texUR.getY(), Math.min(texLR.getY(), texLL.getY())));
            double texMaxX = Math.max(texUL.getX(), Math.max(texUR.getX(), Math.max(texLR.getX(), texLL.getX())));
            double texMaxY = Math.max(texUL.getY(), Math.max(texUR.getY(), Math.max(texLR.getY(), texLL.getY())));

            double texWidth = texMaxX - texMinX;
            double texHeight = texMaxY - texMinY;


            ul = texPt.transform(new Point2D(texMinX, texMinY));
            ur = texPt.transform(new Point2D(texMinX + texWidth, texMinY));
            lr = texPt.transform(new Point2D(texMinX + texWidth, texMinY + texHeight));
            ll = texPt.transform(new Point2D(texMinX, texMinY + texHeight));


            ul = texPt.transform(texUL);
            ur = texPt.transform(texUR);
            lr = texPt.transform(texLR);
            ll = texPt.transform(texLL);


            //将坐标应用到变换
            pt.setUlx(ul.getX());
            pt.setUly(ul.getY());
            pt.setUrx(ur.getX());
            pt.setUry(ur.getY());
            pt.setLrx(lr.getX());
            pt.setLry(lr.getY());
            pt.setLlx(ll.getX());
            pt.setLly(ll.getY());


            // 计算裁剪后的材质在屏幕上的位置和尺寸
            double dx = Math.min(ul.getX(), Math.min(ur.getX(), Math.min(lr.getX(), ll.getX())));
            double dy = Math.min(ul.getY(), Math.min(ur.getY(), Math.min(lr.getY(), ll.getY())));
            double dw = Math.max(ul.getX(), Math.max(ur.getX(), Math.max(lr.getX(), ll.getX()))) - dx;
            double dh = Math.max(ul.getY(), Math.max(ur.getY(), Math.max(lr.getY(), ll.getY()))) - dy;


            // 应用透视变换和绘制
            gc.setEffect(pt);
            // gc.drawImage(tex, texMinX, texMinY, texWidth, texHeight, dx, dy, dw, dh);
            gc.drawImage(tex, x, y, w, h);

        } else {
            gc.setEffect(pt);
            gc.drawImage(tex, x, y, w, h);
        }
        gc.setEffect(null);
    }

    public void transform() {
        affine = new Affine();
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
            //affine.translate(pos.getX(), pos.getY());
            affine.appendRotation(rotate, pos.getX(), pos.getY());
            //affine.translate(-pos.getX(), -pos.getY());
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

                affine.appendShear(shearY, shearX, new Point2D(pos.getX(), pos.getY()));
            }
        }

        //gc.setFill(Color.LIGHTGREEN);
        //gc.fillRect(pos.getX() - 1, pos.getY() - 1, 3, 3);

        //锚点
        //affine.translate(-anchorX, -anchorY);
        affine.appendTranslation(-anchorX, -anchorY);

        //翻转
        if (flip == Orientation.VERTICAL) {
            affine.appendScale(1, -1);  // 垂直翻转
            affine.appendTranslation(0, -pos.getY() * 2 - size.getH());
        } else if (flip == Orientation.HORIZONTAL) {
            affine.appendScale(-1, 1);  // 水平翻转
            affine.appendTranslation(-pos.getX() * 2 - size.getW(), 0);
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
