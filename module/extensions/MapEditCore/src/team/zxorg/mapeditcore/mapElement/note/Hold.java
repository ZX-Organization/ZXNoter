package team.zxorg.mapeditcore.mapElement.note;

import team.zxorg.extensionloader.core.Logger;

/**
 * 长键物件
 */
public class Hold extends Note{
    /**
     * 长键持续时间
     */
    private int duration;

    /**
     * 时间-位置构造法
     * @param time 时间
     * @param position 位置
     * @param duration 持续时间
     */
    public Hold(int time, double position, int duration) {
        super(time, position);
        type = "Hold";
        setDuration(duration);
    }

    public Hold(int time, int orbit, int maxOrbit, int duration) {
        this(time,Note.calPosByOrbit(orbit,maxOrbit),duration);
    }

    /**
     * 获取长条持续时间
     * @return 持续时间
     */
    public int getDuration() {return duration;}

    /**
     * 设置长条持续时间
     * @param duration 持续时间
     */
    public void setDuration(int duration) {
        if (duration >=0)
            this.duration = duration;
        else {
            Logger.info("使用非法持续时间->" + duration + '\n' + "物件:" + this);
        }
    }

    @Override
    public Hold clone() {
        return new Hold(getTime(),getPosition(),getDuration());
    }
    @Override
    public String toString() {
        return '\n'+"      "+"Hold{" +
                "时间=" + time +
                ", 位置=" + position +
                ", 时长=" + duration +
                ", key音=" + keyAudioPath +
                '}';
    }
}
