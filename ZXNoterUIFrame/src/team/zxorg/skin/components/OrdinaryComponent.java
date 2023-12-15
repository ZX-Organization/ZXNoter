package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.basis.RenderRectangle;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;
import static team.zxorg.skin.uis.UISParser.getResource;

/**
 * 普通元件
 */
public class OrdinaryComponent implements ElementRender {
    /**
     * 原件的图片
     */
    public Image tex;

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


    private Orientation flip;


    public OrdinaryComponent(HashMap<String, String> properties, Path uisPath) {

        InputStream fileStream;
        fileStream = getResource(uisPath, properties.get("tex"));
        if (fileStream != null) tex = new Image(fileStream);

        pos = ExpressionVector.parse(properties.get("pos"));
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
    }

    @Override
    public void render(GraphicsContext gc, double width, double height) {
        double proportionalWidth = size.getWidth();
       double proportionalHeight = size.getHeight();

        if (size.getWidth() == 0) {
            // 根据高度计算比例尺寸
            proportionalWidth = proportionalHeight * tex.getWidth() / tex.getHeight();
        } else if (size.getHeight() == 0) {
            // 根据宽度计算比例尺寸
            proportionalHeight = proportionalWidth * tex.getHeight() / tex.getWidth();
        }

        rr.setSize(anchor, proportionalWidth, proportionalHeight);
        rr.setPos(anchor, pos.getX(), pos.getY());

        gc.save();
        gc.rotate(rotate);
        gc.setGlobalAlpha(opacity / 100);

        if (flip != null) rr.drawImage(gc, tex, flip);
        else rr.drawImage(gc, tex);

        gc.restore();
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {
    }
}
