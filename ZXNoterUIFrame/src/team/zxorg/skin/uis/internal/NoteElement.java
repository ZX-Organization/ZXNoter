package team.zxorg.skin.uis.internal;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.skin.ElementRender;

public class NoteElement extends ElementRender {

    public Image tex;
    public Image tex2;
    public Image tex3;
    public Image tex4;
    public Image tex5;
    /**
     * 定义note出现的位置
     */
    public Point2D pos;
    /**
     * 定义note被打击的位置
     */
    public Point2D pos2;
    /**
     * 定义note出现的尺寸
     */
    public Dimension2D size;
    /**
     * 定义note被打击的尺寸
     */
    public Dimension2D size2;
    public Dimension2D size3;
    /**
     * type: hold note类型
     * - type=0时, 由tex当作默认外观.
     * - type=1时, 由tex2指定hold类型外观, 采用Scale3模式拉伸中间段, 配合part属性(参见常规note/Scale3).
     * - type=2时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部拉伸.
     * - type=3时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部填充, 使用size3指定中部的填充间隔, size3的第一个值表示间隔，单位为px，第二个值目前未使用
     * - 其中头部也可以用tex5来定义, 使之与普通note样式区分开来(3.3.5新增).
     * - 可以用tex4来定义同押note的特殊外观.
     */
    int type;
    /**
     * 开关note的特性, 1表示note被打中时立即消失, 2表示hold被按住时, 头部固定不动. 两种属性都需要时, 应写作3
     */
    int toggle;
    /**
     * 下落进度 0出现位置 1f打击的位置
     */
    double progress;

    /**
     * 是否被按下
     */
    boolean pressed;

    private void renderType0(GraphicsContext context) {
        // 处理类型为0的渲染逻辑
        // 使用tex当作默认外观，考虑尺寸的变化
        double x = pos.getX() + (pos2.getX() - pos.getX()) * progress;
        double y = pos.getY() + (pos2.getY() - pos.getY()) * progress;
        double currentWidth = size.getWidth() + (size2.getWidth() - size.getWidth()) * progress;
        double currentHeight = size.getHeight() + (size2.getHeight() - size.getHeight()) * progress;
        context.drawImage(tex, x, y, currentWidth, currentHeight);
    }


    private void renderType1(GraphicsContext context) {
        // 处理类型为1的渲染逻辑
        // 使用tex2指定hold类型外观，采用Scale3模式拉伸中间段，配合part属性
        double x = pos.getX() + (pos2.getX() - pos.getX()) * progress;
        double y = pos.getY() + (pos2.getY() - pos.getY()) * progress;
        context.drawImage(tex2, x, y, size.getWidth(), size.getHeight());
    }

    private void renderType2(GraphicsContext context) {
        // 处理类型为2的渲染逻辑
        // 使用tex、tex2、tex3 组成一个note，其中中部拉伸
        double x = pos.getX() + (pos2.getX() - pos.getX()) * progress;
        double y = pos.getY() + (pos2.getY() - pos.getY()) * progress;
        double middleWidth = size.getWidth() - tex.getWidth() - tex3.getWidth();
        context.drawImage(tex, x, y, tex.getWidth(), size.getHeight());
        context.drawImage(tex2, x + tex.getWidth(), y, middleWidth, size.getHeight());
        context.drawImage(tex3, x + tex.getWidth() + middleWidth, y, tex3.getWidth(), size.getHeight());
    }

    private void renderType3(GraphicsContext context) {
        // 处理类型为3的渲染逻辑
        // 使用tex、tex2、tex3 组成一个note，其中中部填充，使用size3指定填充间隔
        double x = pos.getX() + (pos2.getX() - pos.getX()) * progress;
        double y = pos.getY() + (pos2.getY() - pos.getY()) * progress;
        double gap = size2.getWidth(); // 间隔，单位为px，可以根据实际需要调整
        double totalWidth = tex.getWidth() + size3.getWidth() + tex3.getWidth();
        context.drawImage(tex, x, y, tex.getWidth(), size.getHeight());
        double currentX = x + tex.getWidth();
        while (currentX + totalWidth <= x + size2.getWidth()) {
            context.drawImage(tex2, currentX, y, size3.getWidth(), size.getHeight());
            currentX += size3.getWidth() + gap;
        }
        context.drawImage(tex3, currentX, y, tex3.getWidth(), size.getHeight());
    }

// ...

    @Override
    public void render(GraphicsContext context) {
        progress += 0.001;
        progress %= 1;
        if (type == 0) {
            renderType0(context);
        } else if (type == 1) {
            renderType1(context);
        } else if (type == 2) {
            renderType2(context);
        } else if (type == 3) {
            renderType3(context);
        }
        // 可以根据需要添加其他类型的处理逻辑
    }
}
