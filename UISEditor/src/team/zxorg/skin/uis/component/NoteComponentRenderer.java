package team.zxorg.skin.uis.component;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;

public class NoteComponentRenderer extends AbstractComponentRenderer {
    RenderRectangle rr = new RenderRectangle();
    private ExpressionVector pos1;
    private ExpressionVector size1;
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
     * 开关note的特性, 1表示note被打中时立即消失, 2表示hold被按住时, 头部固定不动. 两种属性都需要时, 应写作3
     */
    private int toggle;
    /**
     * 下落进度 0出现位置 1f打击的位置
     */
    private double progress;
    /**
     * type: hold note类型
     * - type=0时, 由tex当作默认外观.
     * - type=1时, 由tex2指定hold类型外观, 采用Scale3模式拉伸中间段, 配合part属性(参见常规note/Scale3).
     * - type=2时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部拉伸.
     * - type=3时, 由tex(hold头部), tex2(hold中部), tex3(hold尾部)共同组成一个note, 其中中部填充, 使用size3指定中部的填充间隔, size3的第一个值表示间隔，单位为px，第二个值目前未使用
     * - 其中头部也可以用tex5来定义, 使之与普通note样式区分开来(3.3.5新增).
     * - 可以用tex4来定义同押note的特殊外观.
     */
    private double progress2;
    private boolean progress3;
    /**
     * 是否被按下
     */
    private boolean pressed;

    public NoteComponentRenderer(UISComponent component) {
        super(component);
        zindex = 10;
    }

    @Override
    void reloadResComponent() {
        tex2 = component.getImage("tex2");
        tex3 = component.getImage("tex3");
        tex4 = component.getImageOrNull("tex4");
        tex5 = component.getImageOrNull("tex5");
    }

    @Override
    void reloadPosComponent() {
        pos1 = pos.clone();
        size1 = size.clone();

        pos2 = component.getExpressionVector("pos2");
        size2 = component.getExpressionVector("size2");
        if (!component.contains("size2"))
            size2 = size;
        size3 = component.getExpressionVector("size3");
    }

    @Override
    public void message(Object value) {
        super.message(value);
    }


    @Override
    void drawComponent(double width, double height, long time) {
        //progress += 0.004;
        progress = time * 0.0001;
        progress %= 1.4;
        //gc.setGlobalAlpha(1);
        if (type == 1) {


        } else if (type == 2) {

            //绘制身体
            progressCalculation(rr, progress - 0.2);
            Point2D p1 = affine.transform(rr.getLeft(), rr.getCenterY());
            double x1 = p1.getX();
            double y1 = p1.getY();
            double w1 = rr.getWidth();
            progressCalculation(rr, progress);
            Point2D p2 = affine.transform(rr.getLeft(), rr.getCenterY());
            double x2 = p2.getX();
            double y2 = p2.getY();
            double w2 = rr.getWidth();


           /* pos.setX(x1);
            pos.setY(y1);
            size.setW(w1);
            size.setH(y2 - y1);
            transform();*/
            affine = new Affine();

            //绘制中间
            //drawImage(tex2, rr.getRight(), y1, -rr.getWidth(), y2 - y1);
            drawImage(tex2, x1, y1, x1 + w1, y1, x2 + w2, y2, x2, y2);


            //绘制尾部
            progressCalculation(rr, progress - 0.2);


            drawImage(tex3, rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
            drawImage(tex3, rr);

        } else if (type == 3) {

            //绘制身体  间隔
            progressCalculation(rr, progress - 0.2);
            double x1 = rr.getCenterX();
            double y1 = rr.getCenterY();
            double w1 = rr.getWidth() / 2;
            progressCalculation(rr, progress);
            double x2 = rr.getCenterX();
            double y2 = rr.getCenterY();
            double w2 = rr.getWidth() / 2;

            //rr.drawImage(gc, tex2, x1 - w1, y1, x1 + w1, y1, x2 - w2, y2, x2 + w2, y2);

            //绘制尾部
            progressCalculation(rr, progress - 0.2);
            //rr.drawImageTest(gc, tex3);

        }


        //绘制头部
        progressCalculation(rr, progress);

        drawImage(tex, rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());


        //rr.drawImage(gc, (tex5 != null ? tex5 : tex));
        //rr.drawImageTest(gc, tex);

        {

            progressCalculation(rr, 1);

            gc.save();
            gc.setGlobalAlpha((time % 2000 > 1000 ? time % 1000 / 1000f : 1 - time % 1000 / 1000f));
            drawImage(tex, rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
            gc.restore();


            gc.setFill(Color.HOTPINK);
            gc.fillRect(rr.getLeft() - 2, rr.getTop() - 2, 4, 4);


            progressCalculation(rr, 1);
            //rr.drawImageTest(gc, tex);
            gc.fillRect(rr.getLeft() - 2, rr.getTop() - 2, 4, 4);

            //gc.setGlobalAlpha(1);
        }


        pos.setX(rr.getLeft());
        pos.setY(rr.getTop());
        size.setW(rr.getWidth());
        size.setH(rr.getHeight());
    }

    private void progressCalculation(RenderRectangle rr, double p) {
        double imgX = pos1.getX() + (pos2.getX() - pos1.getX()) * p;
        double imgY = pos1.getY() + (pos2.getY() - pos1.getY()) * p;
        double imgW = size1.getW() + (size2.getW() - size1.getW()) * p;
        double imgH = size1.getH() + (size2.getH() - size1.getH()) * p;
        rr.setPos(Pos.TOP_LEFT, imgX, imgY);
        rr.setSize(Pos.TOP_LEFT, imgW, imgH);

        pos.setX(rr.getLeft());
        pos.setY(rr.getTop());
        size.setW(rr.getWidth());
        size.setH(rr.getHeight());
        transform();
    }
}
