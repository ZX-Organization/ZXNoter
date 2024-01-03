package team.zxorg.skin.uis;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UISFrameAnimation {
    public Image getFirstFrame() {
        return firstFrame;
    }

    private static class AnimationBehavior {
        /**
         * 动画名称 如果name为null则为延迟且repeat为延迟时间ms
         */
        String name;
        /**
         * 动画播放次数 0为无限重复
         */
        int repeat;

        public AnimationBehavior(String name, int repeat) {
            this.name = name;
            this.repeat = repeat;
        }
    }

    //帧动画表
    private final HashMap<String, List<Image>> animationMap = new HashMap<>();
    // 动画行为队列
    private final ArrayList<AnimationBehavior> behaviors = new ArrayList<>();
    // 当前行为索引
    private int currentBehaviorIndex = 0;
    // 当前帧索引
    private int currentFrameIndex = 0;
    // 当前帧动画 可能为null
    private List<Image> currentFrames;
    // 当前行为 可能为null
    private AnimationBehavior currentBehavior;
    //当前重复的次数
    private int currentRepeat;
    //间隔时间
    private final double interval;
    //计时器
    private long timer;
    private Image firstFrame;

    public UISFrameAnimation(UISComponent component, String... names) {
        this.interval = component.getDouble("interval", 500);
        for (String name : names) {
            List<Image> animationList = component.getImageList(name);
            animationMap.put(name, animationList);
            if (animationList != null)
                if (firstFrame == null)
                    if (!animationList.isEmpty())

                        firstFrame = animationList.getFirst();
        }
    }

    /**
     * 添加动画行为
     *
     * @param name   帧动画名 如果name为null则为延迟且repeat为延迟时间ms
     * @param repeat 重复次数 0为无限重复
     */
    public void addBehavior(String name, int repeat) {
        behaviors.add(new AnimationBehavior(name, repeat));
        if (currentBehavior == null)
            switchAnimation();
    }

    /**
     * 获取当前帧
     *
     * @return 当前帧 可能为null
     */
    public Image getCurrentFrames() {
        if (currentBehavior == null)
            return null;
        int time = (int) (System.currentTimeMillis() - timer);
        //如果是延迟
        if (currentBehavior.name == null) {
            if (time > currentBehavior.repeat) {
                switchAnimation();
                return getCurrentFrames();
            }
            return null;
        } else {
            //是帧动画
            if (time > interval) {
                currentFrameIndex++;
                timer = System.currentTimeMillis();
                if (currentFrameIndex >= currentFrames.size()) {
                    currentFrameIndex = 0;
                    currentRepeat--;
                    //判断当前重复次数 支持无限重复
                    if (currentRepeat <= 0 && currentBehavior.repeat != 0) {
                        switchAnimation();
                        return getCurrentFrames();
                    }
                }
            }
            if (!currentFrames.isEmpty())
                return currentFrames.get(currentFrameIndex);
        }

        return null;
    }

    /**
     * 切换动画
     */
    private void switchAnimation() {
        currentFrameIndex = 0;
        currentBehaviorIndex++;
        currentBehaviorIndex %= behaviors.size();

        currentRepeat = 0;
        timer = System.currentTimeMillis();


        currentBehavior = behaviors.get(currentBehaviorIndex);
        currentFrames = animationMap.get(currentBehavior.name);
        currentRepeat = currentBehavior.repeat;


    }
}
