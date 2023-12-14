package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.basis.RenderRectangle;

import java.nio.file.Path;
import java.util.HashMap;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;

public class HitComponent implements ElementRender {
    private final AnimationComponent frame;
    private final AnimationComponent frame2;
    private final AnimationComponent frame3;

    /**
     * 定义原件出现的位置
     */
    private final ExpressionVector pos;
    /**
     * /**
     * 定义note出现的尺寸
     */
    private final ExpressionVector size;
    /**
     * 定位参考点
     */
    private final Pos anchor;
    /**
     * 渲染矩形
     */
    private final RenderRectangle rr = new RenderRectangle();
    /**
     * 基于锚点位置的旋转角度 数值表示元件逆时针的转角,单位角度
     */
    private double rotate;
    /**
     * 定义元件透明度 范围0到100,100表示不透明,0表示完全透明
     */
    private double opacity;
    private Orientation flip;

    public HitComponent(HashMap<String, String> properties, Path uisPath) {

        double interval = 0;
        if (properties.get("interval") != null) try {
            interval = Double.parseDouble(properties.get("interval"));
        } catch (NumberFormatException ignored) {
        }


        frame = new AnimationComponent(properties.get("frame"), interval, uisPath);
        frame.loop = true;

        frame2 = new AnimationComponent(properties.get("frame2"), interval, uisPath);
        frame3 = new AnimationComponent(properties.get("frame3"), interval, uisPath);


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
        rr.setSize(anchor, size.getWidth(), size.getHeight());
        rr.setPos(anchor, pos.getX(), pos.getY());
        gc.save();
        gc.rotate(rotate);
        gc.setGlobalAlpha(opacity / 100);
        frame.update();
        Image currentFrame = (System.currentTimeMillis() % 500 < 200 ? frame.getCurrentFrame() : null);
         currentFrame =  frame.getCurrentFrame();
        if (currentFrame != null) {
            if (flip != null) rr.drawImage(gc, currentFrame, flip);
            else rr.drawImage(gc, currentFrame);
        }
        gc.restore();
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }
}
