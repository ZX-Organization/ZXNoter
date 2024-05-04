package team.zxorg.skin.uis.component;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.uis.UISComponent;
import team.zxorg.skin.uis.animaion.*;
import team.zxorg.zxncore.ZXLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationComponentRenderer extends AbstractComponentRenderer {
    List<AbstractAnimation> animations;

    public AnimationComponentRenderer(UISComponent component) {
        super(component);
    }

    private AbstractAnimation toAnimationRenderer(String value) {
        value = value.replaceAll(" ", "");
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
            case "rotate", "r" -> new RotateAnimation(component, value);
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
        //animations.sort(Comparator.comparingLong(AbstractAnimation::getStartTime));
    }

    @Override
    void reloadPosComponent() {

    }

    @Override
    void drawComponent(double width, double height, long time) {

    }


    public void update(GraphicsContext gc, double width, double height, AbstractComponentRenderer cr, long time) {
        //欲播放动画表
        HashMap<String, AbstractAnimation> preExecutionAnimations = new HashMap<>();
        //检查所有动画
        for (AbstractAnimation renderer : animations) {
            //更新动画时间
            renderer.updateAnimationTime(time);
            //检查是否是闲置的
            if (renderer.isDisengaged()) {
                //如果欲播放动画表不包含此类型动画则放进去
                AbstractAnimation currAnimation = preExecutionAnimations.get(renderer.getName());
                if (currAnimation == null) {
                    preExecutionAnimations.put(renderer.getName(), renderer);
                } else {
                    if (currAnimation.getLastEndTime() < renderer.getLastEndTime()) {
                        preExecutionAnimations.put(renderer.getName(), renderer);
                    }
                }
            } else {
                preExecutionAnimations.put(renderer.getName(), renderer);
            }
        }

        for (AbstractAnimation renderer : preExecutionAnimations.values()) {
            renderer.handleAnimation(gc, width, height, time, cr);
        }
    }

}
