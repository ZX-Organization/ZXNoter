package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRenderInterface;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.newskin.property.data.FrameProperty;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;

public class HitComponent implements ElementRenderInterface {
    private final FrameProperty frame;
    private final FrameProperty frame2;
    private final FrameProperty frame3;

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
    private int blend;

    public HitComponent(HashMap<String, String> properties, Path uisPath) {

        double interval = 0;
        if (properties.get("interval") != null) try {
            interval = Double.parseDouble(properties.get("interval"));
        } catch (NumberFormatException ignored) {
        }
        if (properties.get("blend") != null) try {
            blend = Integer.parseInt(properties.get("blend"));
        } catch (NumberFormatException ignored) {
        }

        frame = new FrameProperty(properties.get("frame"), interval, uisPath);
        frame.loop = true;
        frame.setCurrentFrameTo(new Random().nextInt(frame.frames.length));

        frame2 = new FrameProperty(properties.get("frame2"), interval, uisPath);
        frame3 = new FrameProperty(properties.get("frame3"), interval, uisPath);


        String elementName = properties.get("$elementName");
        int elementIndex = Integer.parseInt(elementName.substring(elementName.indexOf('-') + 1)) ;


        pos = ExpressionVector.parse(properties.get("pos") + "," + elementIndex);
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
    public void draw(GraphicsContext gc, double width, double height) {
        rr.setSize(anchor, size.getWidth(), size.getHeight());
        rr.setPos(anchor, pos.getX(), pos.getY());
        gc.save();
        gc.rotate(rotate);
        gc.setGlobalAlpha(opacity / 100);
        if (frame.update()) {
            frame.timer = System.currentTimeMillis() + 400;
        }
        Image currentFrame = (System.currentTimeMillis() % 500 < 200 ? frame.getCurrentFrame() : null);
        currentFrame = frame.getCurrentFrame();
        //if (blend != 0)
        //gc.setGlobalBlendMode((blend == 1 ? BlendMode.SOFT_LIGHT : BlendMode.LIGHTEN));
        gc.setGlobalBlendMode(BlendMode.ADD);
        if (currentFrame != null) {
            if (flip != null) rr.drawImage(gc, currentFrame, flip);
            else rr.drawImage(gc, currentFrame);
        }
        gc.restore();
    }


}
