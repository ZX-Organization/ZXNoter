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
    RenderRectangle scale1 = new RenderRectangle();
    RenderRectangle scale2 = new RenderRectangle();
    RenderRectangle scale3 = new RenderRectangle();

    public Scale3ComponentRender(UISComponent component) {
        super(component);
    }

    @Override
    void reloadResComponent() {
        type2 = component.getInt("type2", 0);
    }


    @Override
    void reloadPosComponent() {
        part = component.getExpressionVector("part");
        size2 = component.getExpressionVector("size2");
    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {


        //计算材质位置
        double scale1TexPos = 0;
        double scale2TexPos = part.getWidth();
        double scale3TexPos = part.getHeight();

        double scale1TexSize = scale2TexPos;
        double scale2TexSize = scale3TexPos - scale2TexPos;
        double scale3TexSize = (type2 == 1 ? tex.getHeight() : tex.getWidth()) - scale3TexPos;


        //计算渲染位置
        double scale1Pos = (type2 == 1 ? rr.getTop() : rr.getLeft());
        double scale2Pos = scale1Pos + scale2TexPos * pixelMagnification;
        double scale3Pos = (type2 == 1 ? rr.getBottom() : rr.getRight()) - scale3TexSize * pixelMagnification;

        double scale1Size = scale1TexSize * pixelMagnification;
        double scale3Size = scale3TexSize * pixelMagnification;
        double scale2Size = (type2 == 1 ? rr.getHeight() : rr.getWidth()) - scale1Size - scale3Size;

        if (type2 == 1) {
            // 竖向拉伸，此时拉伸区域0点在素材上部


            gc.drawImage(tex,
                    0, scale1TexPos, tex.getWidth(), scale1TexSize,
                    rr.getLeft(), scale1Pos, rr.getWidth(), scale1Size
            );

            gc.drawImage(tex,
                    0, scale3TexPos, tex.getWidth(), scale3TexSize,
                    rr.getLeft(), scale3Pos, rr.getWidth(), scale3Size
            );

            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    0, scale2TexPos, tex.getWidth(), scale2TexSize,
                    rr.getLeft(), scale2Pos, rr.getWidth(), scale2Size
            );

        } else {
            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    scale1TexPos, 0, scale1TexSize, tex.getHeight(),
                    scale1Pos, rr.getTop(), scale1Size, rr.getHeight()
            );

            gc.drawImage(tex,
                    scale3TexPos, 0, scale3TexSize, tex.getHeight(),
                    scale3Pos, rr.getTop(), scale3Size, rr.getHeight()
            );

            // 横向拉伸，默认拉伸区域0点在素材左部
            gc.drawImage(tex,
                    scale2TexPos, 0, scale2TexSize, tex.getHeight(),
                    scale2Pos, rr.getTop(), scale2Size, rr.getHeight()
            );
        }

    }
}
