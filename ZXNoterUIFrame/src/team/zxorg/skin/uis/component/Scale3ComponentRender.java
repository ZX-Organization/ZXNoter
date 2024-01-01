package team.zxorg.skin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;

public class Scale3ComponentRender extends AbstractComponentRenderer {
    /**
     * 三段元件会固定首尾部分, 只拉伸中间部分. 而中间部分在素材上的位置用part属性标记.
     */
    ExpressionVector part;
    /**
     * 使用size指定显示尺寸时，元件会整体放大，使用size2指定尺寸时，元件除中部以外的部分，将保持原有比例
     */
    ExpressionVector size2;
    /**
     * 通过设置type2=1指明是竖向拉伸, 此时拉伸区域0点在素材上部.
     * 默认是横向拉伸, 此时拉伸区域0点在素材左部.
     */
    int type2;
    double pixelMagnification;

    public Scale3ComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        type2 = component.getInt("type2", 0);
    }


    @Override
    void reloadPosComponent() {
        pixelMagnification = pos.expressionCalculator.getPixelMagnification();
        part = component.getExpressionVector("part");
        size2 = component.getExpressionVector("size2");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {


        //第一部分的位置
        double part1 = part.getWidth();
        // 绘制第一部分
        if (type2 == 1) {
            // 竖向拉伸，此时拉伸区域0点在素材上部
            gc.drawImage(tex,
                    0, 0, tex.getWidth(), part1,
                    rr.getLeft(), rr.getTop(), rr.getWidth(), part1 * pixelMagnification);
        } else {
            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    0, 0, part1, tex.getHeight(),
                    rr.getLeft(), rr.getTop(), part1 * pixelMagnification, rr.getHeight()
            );
        }


        //第三部分的位置
        double part3 = part.getHeight();
        // 绘制第三部分
        if (type2 == 1) {
            // 竖向拉伸，此时拉伸区域0点在素材上部
            gc.drawImage(tex,
                    part3, part1, 0, rr.getWidth(),
                    rr.getBottom() - part1 * pixelMagnification, tex.getWidth(), part1 * pixelMagnification, rr.getHeight()
            );
        } else {
            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    part3, 0, part1, tex.getHeight(),
                    rr.getRight() - part1 * pixelMagnification, rr.getTop(), part1 * pixelMagnification, rr.getHeight()
            );
        }

        //第二部分的位置
        if (type2 == 1) {
            // 竖向拉伸，此时拉伸区域0点在素材上部



        } else {
            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    part1, 0, part3 - part1, tex.getHeight(),
                    rr.getLeft() + part1 * pixelMagnification, rr.getTop(), rr.getRight() - part1 * pixelMagnification - rr.getLeft() + part1 * pixelMagnification, rr.getHeight()
            );
        }

    }
}
