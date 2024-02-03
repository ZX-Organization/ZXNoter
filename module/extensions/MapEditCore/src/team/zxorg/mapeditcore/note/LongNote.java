package team.zxorg.mapeditcore.note;

import team.zxorg.extensionloader.core.Logger;

/**
 * 长键物件
 */
public class LongNote extends Note{
    /**
     * 长键持续时间
     */
    private int duration;

    public LongNote(int time, double position,int duration) {
        super(time, position);
        setDuration(duration);
    }

    /**
     * 获取长条持续时间
     * @return 持续时间
     */
    public int getDuration() {return duration;}

    /**
     * 设置长条持续时间
     * @param duration 持续时间
     * @return 结果
     */
    public int setDuration(int duration) {
        if (duration >=0)
            return this.duration = duration;
        else {
            Logger.info("使用非法持续时间->" + duration + '\n' + "物件:" + this);
            return this.duration;
        }
    }
}
