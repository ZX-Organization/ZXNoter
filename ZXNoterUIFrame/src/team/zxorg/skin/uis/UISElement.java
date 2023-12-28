package team.zxorg.skin.uis;

import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class UISElement {
    /**
     * 定义这个元件对应的图片
     */
    public Image tex;
    /**
     * 定义元件的显示尺寸  长或宽为0时, 表示元件一边固定, 另一边自动缩放. 尺寸有三种写法, 参照后续尺寸段
     */
    public Dimension2D size;
    /**
     * 定义元件的定位位置
     */
    public Point2D pos;
    /**
     * 定义元件在定位时的参考点,从0-8分别代表 左上,中上,右上,中左,正中,中右,左下,下中,右下
     */
    public Pos anchor;
    /**
     * 定义元件的染色效果
     */
    public Color color;
    /**
     * 定义元件基于anchor位置的旋转角 数值表示元件逆时针的转角,单位角度
     */
    public int rotate;
    /**
     * 定义元件的翻转效果 0代表水平镜像,1代表垂直镜像
     */
    public Orientation flip;
    /**
     * 定义元件透明度 范围0到100,100表示不透明,0表示完全透明
     */
    public int opacity;
    /**
     * 定义元件的堆叠位置 0为最底层, 10为note所在层, 大于100的元件将不受是否开启3D的影响
     */
    public int zindex;
    /**
     * 定义元件类型
     */
    public ConventionalComponentType type;

    /**
     * 定义一个动态图所使用的所有图片,贴图文件必须为png,采用前缀/帧数格式书写,无需指定完整文件名即有light-0.png到light-4.png的序列帧
     */
    public List<Image> frame;
    /**
     * 定义动态图的刷新间隔, 单位毫秒
     */
    public int interval;

    /**
     * 定义拉伸类元件的拉伸范围
     */
    public Dimension2D part;
    /**
     * 渲染模式
     */
    public BlendType blend;

    /**
     * 定义文字元件的字号 单位同size
     */
    public int fsize;
    /**
     * 定义文字元件的显示内容
     */
    public String text;

    public abstract void render(GraphicsContext context);
}
