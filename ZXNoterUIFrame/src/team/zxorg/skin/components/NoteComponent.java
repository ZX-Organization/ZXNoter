package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.basis.RenderRectangle;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;
import static team.zxorg.skin.uis.UISParser.getResource;

public class NoteComponent implements ElementRender {

    /**
     * 定义note出现的位置
     */
    private final ExpressionVector pos;
    /**
     * 定义note被打击的位置
     */
    private final ExpressionVector pos2;
    /**
     * 定义note出现的尺寸
     */
    private final ExpressionVector size;
    /**
     * 定义note被打击的尺寸
     */
    private final ExpressionVector size2;
    private final ExpressionVector size3;
    /**
     * 定位参考点
     */
    private final Pos anchor;
    private final RenderRectangle rr = new RenderRectangle();
    private Image tex;
    private Image tex2;
    private Image tex3;
    private Image tex4;
    private Image tex5;
    /**
     * type: hold note类型
     * - type=0时, 由tex当作默认外观.
     * - type=1时, 由tex2指定hold类型外观, 采用Scale3模式拉伸中间段, 配合part属性(参见常规note/Scale3).
     * - type=2时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部拉伸.
     * - type=3时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部填充, 使用size3指定中部的填充间隔, size3的第一个值表示间隔，单位为px，第二个值目前未使用
     * - 其中头部也可以用tex5来定义, 使之与普通note样式区分开来(3.3.5新增).
     * - 可以用tex4来定义同押note的特殊外观.
     */
    private int type;
    /**
     * 开关note的特性, 1表示note被打中时立即消失, 2表示hold被按住时, 头部固定不动. 两种属性都需要时, 应写作3
     */
    private int toggle;
    /**
     * 下落进度 0出现位置 1f打击的位置
     */
    private double progress;
    /**
     * 是否被按下
     */
    private boolean pressed;


    public NoteComponent(HashMap<String, String> properties, Path uisPath) {

        InputStream fileStream;
        fileStream = getResource(uisPath, properties.get("tex"));
        if (fileStream != null) tex = new Image(fileStream);

        pos = ExpressionVector.parse(properties.get("pos"));
        pos2 = ExpressionVector.parse(properties.get("pos2"));
        size = ExpressionVector.parse(properties.get("size"));
        size2 = ExpressionVector.parse(properties.get("size2"));
        size3 = ExpressionVector.parse(properties.get("size3"));
        anchor = getAnchorPos(properties.get("anchor"));
        progress=new Random().nextFloat();
    }

    private void renderType0(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        // 处理键对象类型为0的渲染逻辑

        //根据进度计算位置和大小
        double imgX = pos.getX() + (pos2.getX() - pos.getX()) * progress;
        double imgY = pos.getY() + (pos2.getY() - pos.getY()) * progress;
        double imgW = size.getWidth() + (size2.getWidth() - size.getWidth()) * progress;
        double imgH = size.getHeight() + (size2.getHeight() - size.getHeight()) * progress;

        //设置渲染矩阵
        gc.save();
        gc.setGlobalAlpha(1);
        rr.setSize(anchor, imgW, imgH);
        rr.setPos(anchor, imgX, imgY);
        rr.drawImageTest(gc, tex);
        gc.restore();
        gc.save();
        gc.setGlobalAlpha(0.5);
        rr.setSize(anchor,  size.getWidth(),  size.getHeight());
        rr.setPos(anchor, pos.getX(), pos.getY());
        rr.drawImageTest(gc, tex);
        gc.restore();
        gc.save();
        gc.setGlobalAlpha(0.5);
        rr.setSize(anchor,  size2.getWidth(),  size2.getHeight());
        rr.setPos(anchor, pos2.getX(), pos2.getY());
        rr.drawImageTest(gc, tex);
        gc.restore();
    }

    @Override
    public void render(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        progress += 0.001;
        progress %= 1;
        if (type == 0) {
            renderType0(gc, canvasWidth, canvasHeight);
        }
        // 可以根据需要添加其他类型的处理逻辑
    }

    @Override
    public void canvasResized(double canvasWidth, double canvasHeight, Orientation orientation) {
    }
}
