package team.zxorg.skin.components;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRenderInterface;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.newskin.property.data.FrameProperty;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;
import static team.zxorg.skin.uis.UISParser.getResource;

/**
 * 普通元件
 */
public class OrdinaryComponent implements ElementRenderInterface {
    /**
     * 原件的图片
     */
    public Image tex;

    private Color color;
    private FrameProperty frame;

    private String text;
    private double fsize;


    /**
     * 定义原件出现的位置
     */
    public ExpressionVector pos;
    /**
     * /**
     * 定义note出现的尺寸
     */
    public ExpressionVector size;
    /**
     * 定位参考点
     */
    Pos anchor;
    /**
     * 基于锚点位置的旋转角度 数值表示元件逆时针的转角,单位角度
     */
    double rotate;
    /**
     * 定义元件透明度 范围0到100,100表示不透明,0表示完全透明
     */
    double opacity;


    /**
     * 渲染矩形
     */
    RenderRectangle rr = new RenderRectangle();
    /**
     * type=0
     * 普通元件, 表示一个图片
     * type=1
     * 文字元件, 表示一个固定的文字, 使用text属性指定要显示的内容, 可以使用{xxx}的方式来引用一些当前谱面里的文字内容:
     * •	{title}: 表示歌曲名
     * •	{artist}: 表示歌曲艺术家
     * •	{creator}: 表示谱面作者名
     * •	{version}: 谱面难度名
     * type=2
     * 纯色元件, 即一个纯色的矩形, 颜色由color属性指定
     * type=3
     * 动态元件(Animation), 即序列帧动画, 使用frame属性指定动画帧素材名, 用interval指定动画切换速度
     * type=4
     * 三段拉伸元件(Scale3), 指素材的中间部分是可以拉伸的, 例如当使用100*20的素材, 但要显示时填满100*200的区域时, 普通元件会将整个图片整体拉伸, 而三段元件会固定首尾部分, 只拉伸中间部分. 而中间部分在素材上的位置用part属性标记.
     * 用于三段拉伸的图片素材必须是横向拉伸的, 需要竖向显示时, 可以配合rotate属性.
     * *[2.2新增] 三段拉伸素材可以是竖向, 通过设置type2=1指明是竖向拉伸, 此时拉伸区域0点在素材上部.
     * *[3.5新增] 使用size指定显示尺寸时，元件会整体放大，使用size2指定尺寸时，元件除中部以外的部分，将保持原有比例
     * <p>
     * type=5
     * 九宫图(Scale9), 指素材中间的矩形区域是可拉伸的，如图所示，使用rect属性指定这个区域的左上角素材位置x,y和拉伸区域宽高w,h。注意这里的4个值都是相对于素材本身的，因此是像素值，需要以px为单位。
     */
    private int type = 0;

    private Orientation flip;


    public OrdinaryComponent(HashMap<String, String> properties, Path uisPath) {

        if (properties.get("type") != null)
            type = Integer.parseInt(properties.get("type"));


        if (properties.get("tex") != null) {
            InputStream fileStream;
            fileStream = getResource(uisPath, properties.get("tex"));
            if (fileStream != null) tex = new Image(fileStream);
        }

        String elementName = properties.get("$elementName");

        int elementIndex = 0;
        if (elementName.contains("-"))
            elementIndex = Integer.parseInt(elementName.substring(elementName.indexOf('-') + 1));


        pos = ExpressionVector.parse(properties.get("pos")+ "," + elementIndex);
        if (properties.get("size") != null)
            size = ExpressionVector.parse(properties.get("size"));
        anchor = getAnchorPos(properties.get("anchor"));
        rotate = 0;
        if (properties.get("rotate") != null) try {
            rotate = Double.parseDouble(properties.get("rotate"));
        } catch (NumberFormatException ignored) {
        }
        opacity = 100;
        if (properties.get("opacity") != null) try {
            opacity = Double.parseDouble(properties.get("opacity"));
        } catch (NumberFormatException ignored) {
        }

        if (properties.get("flip") != null)
            flip = (properties.get("flip").equals("0") ? Orientation.HORIZONTAL : Orientation.VERTICAL);

        if (properties.get("color") != null)
            color = Color.web(properties.get("color"));


        switch (type) {
            case 1 -> {
                text = properties.get("text");
                fsize = Double.parseDouble(properties.get("fsize"));
            }
            case 3 -> {
                double interval = 0;
                if (properties.get("interval") != null) try {
                    interval = Double.parseDouble(properties.get("interval"));
                } catch (NumberFormatException ignored) {
                }
                frame = new FrameProperty(properties.get("frame"), interval, uisPath);
            }

        }
    }

    @Override
    public void draw(GraphicsContext gc, double width, double height) {
        double proportionalWidth = 0;
        double proportionalHeight = 0;
        if (size != null) {
            proportionalWidth = size.getWidth();
            proportionalHeight = size.getHeight();

            if (size.getWidth() == 0) {
                // 根据高度计算比例尺寸
                proportionalWidth = proportionalHeight * tex.getWidth() / tex.getHeight();
            } else if (size.getHeight() == 0) {
                // 根据宽度计算比例尺寸
                proportionalHeight = proportionalWidth * tex.getHeight() / tex.getWidth();
            }
            rr.setSize(anchor, proportionalWidth, proportionalHeight);
            rr.setPos(anchor, pos.getX(), pos.getY());
        }

        gc.save();
        if (rotate != 0) {
            double x = switch (anchor.getHpos()) {
                case LEFT -> rr.getLeft();
                case CENTER -> rr.getCenterX();
                case RIGHT -> rr.getRight();
            };
            double y = switch (anchor.getVpos()) {
                case TOP -> rr.getTop();
                case BASELINE, CENTER -> rr.getCenterY();
                case BOTTOM -> rr.getBottom();
            };
            gc.translate(pos.getX(), pos.getY());
            rr.setX(HPos.CENTER, -rr.getCenterX());
            rr.setY(VPos.CENTER, -rr.getCenterY());
            gc.rotate(rotate);
        }

        gc.setGlobalAlpha(opacity / 100);

        if (color != null) {
            gc.setFill(color);
        }
        Image img = null;
        switch (type) {
            case 0 -> img = tex;
            case 3 -> img = frame.getCurrentFrame();
            case 1 -> {
                gc.setFont(Font.font(fsize));
                switch (anchor.getHpos()) {
                    case LEFT -> gc.setTextAlign(TextAlignment.LEFT);
                    case RIGHT -> gc.setTextAlign(TextAlignment.RIGHT);
                    case CENTER -> gc.setTextAlign(TextAlignment.CENTER);
                }

                gc.setTextBaseline(anchor.getVpos());
                gc.fillText(text, pos.getX(), pos.getY());
            }
            case 2 -> {
                gc.fillRect(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
            }
        }


        if (img != null) {
            if (flip != null) rr.drawImage(gc, img, flip);
            else rr.drawImage(gc, img);
        }
        gc.restore();
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {
    }
}
