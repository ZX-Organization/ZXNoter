package team.zxorg.newskin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.newskin.basis.RenderRectangle;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.animaion.*;
import team.zxorg.zxncore.ZXLogger;

import java.util.ArrayList;
import java.util.List;

public class AnimationComponentRenderer extends AbstractComponentRenderer {
    List<AbstractAnimationRenderer> animations;
    long time = System.currentTimeMillis() + 3000;

    public AnimationComponentRenderer(UISComponent component) {
        super(component);
    }

    private AbstractAnimationRenderer toAnimationRenderer(String value) {
        return switch (value.substring(0, value.indexOf(","))) {
            default -> null;
            case "move", "m" -> new MoveAnimationRenderer(component, value, 0);
            case "movex", "mx" -> new MoveAnimationRenderer(component, value, 1);
            case "movey", "my" -> new MoveAnimationRenderer(component, value, 2);
            case "size" -> new SizeAnimationRender(component, value, 0);
            case "width", "w" -> new SizeAnimationRender(component, value, 1);
            case "height", "h" -> new SizeAnimationRender(component, value, 2);
            case "scale", "s" -> new ScaleAnimationRender(component, value, 0);
            case "scalex", "sx" -> new ScaleAnimationRender(component, value, 1);
            case "scaley", "sy" -> new ScaleAnimationRender(component, value, 2);
            case "fade","f" -> new FadeAnimationRender(component, value);
            case "hide" -> new HideAnimationRenderer(component, value);
            case "show" -> new HideAnimationRenderer(component, value);
            case "rotate" -> new RotateAnimationRenderer(component, value);
        };
    }

    @Override
    void reloadResComponent() {

        animations = new ArrayList<>();
        for (String value : component.getAnimations()) {
            AbstractAnimationRenderer ar = toAnimationRenderer(value);
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


    public void update(GraphicsContext gc, double width, double height, AbstractComponentRenderer cr) {
        for (AbstractAnimationRenderer renderer : animations) {
            renderer.draw(gc, width, height, System.currentTimeMillis() - time, cr);
        }
    }

}
