package skin.uis.animaion;

import javafx.scene.canvas.GraphicsContext;
import skin.uis.component.AbstractComponentRenderer;
import skin.uis.ExpressionCalculator;
import skin.uis.ExpressionVector;
import skin.uis.UISComponent;
import team.zxorg.zxncore.ZXLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractAnimation {
    public final ExpressionCalculator expressionCalculator;
    private final UISComponent component;
    private final HashMap<String, String> properties;
    private final String name;

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getLastEndTime() {
        int index = animationTimeIndex;
        if (animationTimeIndex - 1 > 0)
            index--;
        return animationTimes.get(index).endTime;
    }

    private final long startTime;
    private final long endTime;
    private final double[] bezierControlPoints;
    /**
     * 元件索引值 如'_sprite-4'为 3
     */
    protected int index;


    List<AnimationTime> animationTimes = new ArrayList<>();
    private int animationTimeIndex = 0;

    public String getName() {
        return name;
    }

    private static HashMap<String, String> parseProperties(String input) {
        HashMap<String, String> properties = new HashMap<>();


        // 使用正则表达式匹配 key=value 的模式
        String patternString = "(\\w+)=(\\([^)]*\\)|-?\\d+%?[-+]?\\d*|-?\\d*%?[-+]?\\d*),?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        // 迭代匹配结果，并将结果放入HashMap中
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            // 去掉括号
            if (value.startsWith("(") && value.endsWith(")")) {
                value = value.substring(1, value.length() - 1);
            }
            properties.put(key, value);
        }

        return properties;
    }

    private double calculateBezier(double p) {
        return cubicBezier(p, bezierControlPoints[0], bezierControlPoints[1], bezierControlPoints[2], bezierControlPoints[3]);
    }

    AnimationTime currentAnimationTime;

    public AnimationTime getCurrentAnimationTime() {
        return currentAnimationTime;
    }

    public static double cubicBezier(double t, double x1, double y1, double x2, double y2) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        // 默认 P0 和 P3 为原点和终点
        double x = uuu * 0 + 3 * uu * t * x1 + 3 * u * tt * x2 + ttt * 1;
        double y = uuu * 0 + 3 * uu * t * y1 + 3 * u * tt * y2 + ttt * 1;

        return y;
    }

    public AbstractAnimation(UISComponent component, String animation) {
        name = animation.substring(0, animation.indexOf(","));
        this.component = component;
        properties = parseProperties(animation);
        if (component.isAnimation()) {
            index = component.getIndex();
            expressionCalculator = component.expressionCalculator;
            reload();
            String time = properties.get("time");
            if (time == null) time = properties.get("atime");
            if (time == null) {
                ZXLogger.warning("动画组件未定义时间: " + component);
                throw new IllegalArgumentException("动画组件未定义时间: " + component);
            }
            int index = time.indexOf(",");
            if (time.contains("+")) {
                //•	A,+B: 起点时刻为A, 终点时刻为A+B
                startTime = Integer.parseInt(time.substring(0, index));
                endTime = Integer.parseInt(time.substring(index + 2));

            } else if (time.contains(",")) {
                //•	A,B: 起点时刻为A, 终点时刻为B
                startTime = Integer.parseInt(time.substring(0, index));
                endTime = Integer.parseInt(time.substring(index + 1));
            } else {
                //•	A: 等同起点时刻为0, 终点时刻为A
                startTime = 0;
                endTime = Integer.parseInt(time);
            }


            /**
             * 重复次数
             */
            int repeatNumber;
            /**
             * 重复延迟
             */
            int repeatDelay;

            long repeatStartTime;
            long repeatEndTime;
            long duration = Math.abs(endTime - startTime);
            //解析重复
            String repeatV = properties.getOrDefault("repeat", "0");
            if (repeatV.contains("r")) {
                //rA: 以往返运动方式重复A次
                repeatNumber = Integer.parseInt(repeatV.substring(1));

                repeatStartTime = startTime;
                repeatEndTime = endTime;
                animationTimes.add(new AnimationTime(Long.MIN_VALUE, repeatStartTime, 0));
                for (int i = 0; i < (repeatNumber + 1) * 2; i++) {
                    animationTimes.add(new AnimationTime(repeatStartTime, repeatEndTime, (i + 1) % 2 == 0));
                    repeatStartTime += duration;
                    repeatEndTime += duration;
                }
                animationTimes.add(new AnimationTime(repeatEndTime, Long.MAX_VALUE, 0));
            } else if (repeatV.contains(",")) {
                //A,B: 重复A次, 每次重复前等待B时间
                String[] repeatValues = repeatV.split(",");
                repeatNumber = Integer.parseInt(repeatValues[0]);
                repeatDelay = Integer.parseInt(repeatValues[1]);
                repeatStartTime = startTime;
                repeatEndTime = endTime;
                animationTimes.add(new AnimationTime(Long.MIN_VALUE, repeatStartTime, 0));
                for (int i = 0; i < repeatNumber + 1; i++) {
                    animationTimes.add(new AnimationTime(repeatStartTime, repeatEndTime, false));
                    animationTimes.add(new AnimationTime(repeatEndTime, repeatEndTime + repeatDelay, 1));

                    repeatStartTime += duration + repeatDelay;
                    repeatEndTime += duration + repeatDelay;
                }
                animationTimes.add(new AnimationTime(repeatEndTime, Long.MAX_VALUE, 1));
            } else {
                //A: 重复A次
                repeatNumber = Integer.parseInt(repeatV);
                repeatStartTime = startTime;
                repeatEndTime = endTime;
                animationTimes.add(new AnimationTime(Long.MIN_VALUE, repeatStartTime, 0));
                for (int i = 0; i < repeatNumber + 1; i++) {
                    animationTimes.add(new AnimationTime(repeatStartTime, repeatEndTime, false));
                    repeatStartTime += duration;
                    repeatEndTime += duration;
                }
                animationTimes.add(new AnimationTime(repeatEndTime, Long.MAX_VALUE, 1));
            }
            String[] trans = properties.getOrDefault("trans", "0.5,0.5,0.5,0.5").split(",");
            bezierControlPoints = new double[4];
            for (int i = 0; i < 4; i++) {
                bezierControlPoints[i] = Double.parseDouble(trans[i]);
            }
        } else {
            ZXLogger.warning("组件不是动画组件: " + component);
            throw new IllegalArgumentException("组件不是动画组件: " + component);
        }
    }

    abstract void reload();

    public final boolean isDisengaged() {
        return currentAnimationTime.isFixedProgress();
    }

    /**
     * 更新动画时间
     *
     * @param time 当前时间
     */
    public final void updateAnimationTime(long time) {
        currentAnimationTime = animationTimes.get(animationTimeIndex);
        if (currentAnimationTime.endTime < time && animationTimeIndex + 1 < animationTimes.size()) {
            //当前 时间大于动画结束时间 向后检查
            do {
                animationTimeIndex++;
                currentAnimationTime = animationTimes.get(animationTimeIndex);
            } while (currentAnimationTime.endTime < time);
        } else if (currentAnimationTime.startTime > time && animationTimeIndex - 1 > -1) {
            //当前 时间小于动画开始时间 向前检查
            do {
                animationTimeIndex--;
                currentAnimationTime = animationTimes.get(animationTimeIndex);
            } while (currentAnimationTime.startTime > time);
        }
    }

    /**
     * 处理动画
     * 注: 需要自行控制是time还是atime
     * atime时间跟随音乐, 当音乐暂停时动画也暂停,
     *
     * @param time 当前的时间 ms
     */
    public final void handleAnimation(GraphicsContext gc, double width, double height, long time, AbstractComponentRenderer cr) {
        double progress = currentAnimationTime.getProgress(time);
        if (progress > 1 || progress < 0)
            return;
        handle(gc, width, height, calculateBezier(progress), cr);
    }


    /**
     * 处理进度
     *
     * @param gc       渲染上下文
     * @param width    宽度
     * @param height   高度
     * @param progress 进度
     */

    protected abstract void handle(GraphicsContext gc, double width, double height,
                                   double progress, AbstractComponentRenderer cr);

    /**
     * 获取表达式坐标
     *
     * @param name 属性名
     * @return 表达式坐标
     */
    protected ExpressionVector getExpressionVector(String name) {
        String v = properties.getOrDefault(name, "");
        return new ExpressionVector(expressionCalculator, (!v.contains(",") ? v + "," + v : v), index);
    }

    protected double getDouble(String name, double defaultValue) {
        return Double.parseDouble(properties.getOrDefault(name, String.valueOf(defaultValue)));
    }

    @Override
    public String toString() {
        return "AbstractAnimation{" +
                "properties=" + properties +
                '}';
    }
}