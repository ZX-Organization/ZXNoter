package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRenderer;
import team.zxorg.skin.basis.RenderRectangle;

import java.nio.file.Path;
import java.util.HashMap;

import static team.zxorg.skin.uis.UISParser.getAnchorPos;

public class JudgeComponent implements ElementRenderer {

    private final ExpressionVector pos;
    private final ExpressionVector size;
    private final AnimationComponent frame;
    private AnimationComponent frame2;
    private AnimationComponent frame3;
    private AnimationComponent frame4;
    private int state = 0;
    /**
     * type=1时, 判定是单张图片, 而Malody有4种判定, 即Best/Cool/Good/Miss, 因此使用frame属性同时定义4种图片.
     * type=2时, 会同时显示当前判定+这个判定的总数量, 使用frame2定义数字素材.
     * type=3时, 判定是Animation, 使用frame,frame2,frame3,frame4分别定义每一组动画
     */
    private final int type;
    /**
     * 渲染矩形
     */
    private final RenderRectangle rr = new RenderRectangle();
    /**
     * 定位参考点
     */
    private final Pos anchor;

    public JudgeComponent(HashMap<String, String> properties, Path uisPath) {
        type = Integer.parseInt(properties.get("type"));

        double interval = 0;
        if (properties.get("interval") != null) try {
            interval = Double.parseDouble(properties.get("interval"));
        } catch (NumberFormatException ignored) {
        }

        frame = new AnimationComponent(properties.get("frame"), interval, uisPath);
        frame.loop = true;
        if (type == 3) {
            frame2 = new AnimationComponent(properties.get("frame2"), interval, uisPath);
            frame2.loop = true;
            frame3 = new AnimationComponent(properties.get("frame3"), interval, uisPath);
            frame3.loop = true;
            frame4 = new AnimationComponent(properties.get("frame4"), interval, uisPath);
            frame4.loop = true;
        } else {
            {
                frame.interval = 500;
            }
        }

        String elementName = properties.get("$elementName");
        int elementIndex = Integer.parseInt(elementName.substring(elementName.indexOf('-') + 1));

        pos = ExpressionVector.parse(properties.get("pos") + "," + elementIndex);
        anchor = getAnchorPos(properties.get("anchor"));
        size = ExpressionVector.parse(properties.get("size"));
    }

    @Override
    public void render(GraphicsContext gc, double width, double height) {
        rr.setSize(anchor, size.getWidth(), size.getHeight());
        rr.setPos(anchor, pos.getX(), pos.getY());
        gc.save();


        AnimationComponent currentFrame = null;
        if (type == 1) {
            currentFrame = frame;
        } else {
            currentFrame = switch (state) {
                case 1 -> frame2;
                case 2 -> frame3;
                case 3 -> frame4;
                default -> frame;
            };
        }

        if (currentFrame != null)
            if (currentFrame.update()) {
                currentFrame.timer = System.currentTimeMillis() + 400;
                if (type == 3) {
                    state++;
                    state %= 4;
                }
            }

        Image currentImage = currentFrame.getCurrentFrame();
        if (currentImage != null) {
            rr.drawImage(gc, currentImage);
        }
        gc.restore();
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }
}
