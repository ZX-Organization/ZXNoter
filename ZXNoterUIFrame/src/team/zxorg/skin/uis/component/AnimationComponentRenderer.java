package team.zxorg.skin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.animaion.*;
import team.zxorg.zxncore.ZXLogger;

import java.util.ArrayList;
import java.util.List;

public class AnimationComponentRenderer extends AbstractComponentRenderer {
    List<AbstractAnimation> animations;

    public AnimationComponentRenderer(UISComponent component) {
        super(component);
    }

    private AbstractAnimation toAnimationRenderer(String value) {
        return switch (value.substring(0, value.indexOf(","))) {
            default -> null;
            case "move", "m" -> new MoveAnimation(component, value, 0);
            case "movex", "mx" -> new MoveAnimation(component, value, 1);
            case "movey", "my" -> new MoveAnimation(component, value, 2);
            case "size" -> new SizeAnimation(component, value, 0);
            case "width", "w" -> new SizeAnimation(component, value, 1);
            case "height", "h" -> new SizeAnimation(component, value, 2);
            case "scale", "s" -> new ScaleAnimation(component, value, 0);
            case "scalex", "sx" -> new ScaleAnimation(component, value, 1);
            case "scaley", "sy" -> new ScaleAnimation(component, value, 2);
            case "fade", "f" -> new FadeAnimation(component, value);
            case "hide", "show" -> new HideAnimation(component, value);
            case "rotate","r" -> new RotateAnimation(component, value);
            case "skew" -> new SkewAnimation(component, value, 0);
            case "skewx" -> new SkewAnimation(component, value, 1);
            case "skewy" -> new SkewAnimation(component, value, 2);
        };
    }

    @Override
    void reloadResComponent() {

        animations = new ArrayList<>();
        for (String value : component.getAnimations()) {
            AbstractAnimation ar = toAnimationRenderer(value);
            if (ar != null)
                animations.add(ar);
            else
                ZXLogger.warning("动画组件解析失败: " + value);
        }
    }

    @Override
    void reloadPosComponent() {

    }

    @Override
    void drawComponent(GraphicsContext gc, RenderRectangle rr, double width, double height) {

    }


    public void update(GraphicsContext gc, double width, double height, AbstractComponentRenderer cr,long time) {
        for (AbstractAnimation renderer : animations) {
            renderer.draw(gc, width, height, time, cr);
        }
    }

}
