package team.zxorg.newskin.uis.component;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.ExpressionVector;
import team.zxorg.newskin.uis.UISComponent;

public class NoteComponentRender extends BaseComponentRender {
    public NoteComponentRender(UISComponent component) {
        super(component);
    }


    /**
     * 定义note被打击的位置
     */
    private ExpressionVector pos2;

    /**
     * 定义note被打击的尺寸
     */
    private ExpressionVector size2;
    private ExpressionVector size3;

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
    /**
     * 开关note的特性, 1表示note被打中时立即消失, 2表示hold被按住时, 头部固定不动. 两种属性都需要时, 应写作3
     */
    private int toggle;
    /**
     * 下落进度 0出现位置 1f打击的位置
     */
    private double progress;
    private double progress2;
    private boolean progress3;
    /**
     * 是否被按下
     */
    private boolean pressed;


    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
    }

    double x, y;

    @Override
    public void initialize(Canvas canvas) {

        canvas.addEventFilter(MouseEvent.ANY, event -> {
            System.out.println(event);
            x = event.getX();
            y = event.getY();
        });
    }

    @Override
    public void message(Object value) {
        super.message(value);
    }

    @Override
    void reloadComponent(UISComponent component) {
        pos2 = component.getExpressionVector("pos2");
        size2 = component.getExpressionVector("size2");
        if (!component.contains("size2"))
            size2 = size;
        size3 = component.getExpressionVector("size3");
        tex2 = component.getImage("tex2");
        tex3 = component.getImage("tex3");
        tex4 = component.getImage("tex4");
        tex5 = component.getImage("tex5");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {
        progress += 0.004;
        progress %= 1.4;


        if (type == 1) {

        }
        if (type == 2) {

            //绘制身体
            progressCalculation(rr, progress - 0.2);
            double x1 = rr.getCenterX();
            double y1 = rr.getCenterY();
            double w1 = rr.getWidth() / 2;
            progressCalculation(rr, progress);
            double x2 = rr.getCenterX();
            double y2 = rr.getCenterY();
            double w2 = rr.getWidth() / 2;

            rr.drawImage(gc, tex2, x1 - w1, y1, x1 + w1, y1, x2 - w2, y2, x2 + w2, y2);

            progressCalculation(rr, progress - 0.2);
            rr.drawImage(gc, tex3);
        }

        //绘制头部
        progressCalculation(rr, progress);
        rr.drawImage(gc, tex);
        rr.setPos(Pos.CENTER, x, y);
        rr.setSize(Pos.CENTER, 50, 50);

        rr.drawImageTest(gc, UISComponent.UNKNOWN);
    }

    private void progressCalculation(RenderRectangle rr, double p) {
        double imgX = pos.getX() + (pos2.getX() - pos.getX()) * p;
        double imgY = pos.getY() + (pos2.getY() - pos.getY()) * p;
        double imgW = size.getWidth() + (size2.getWidth() - size.getWidth()) * p;
        double imgH = size.getHeight() + (size2.getHeight() - size.getHeight()) * p;
        rr.setPos(anchor, imgX, imgY);
        rr.setSize(anchor, imgW, imgH);
    }
}
