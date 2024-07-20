package team.zxorg.mapeditcore.mapElement.note;

import team.zxorg.extensionloader.core.Logger;

public class Flick extends Note{
    /**
     * 滑动方向角度,0~360
     */
    private double direction;
    private double slideLength;

    /**
     * 位置-方向构造法
     * @param time 时间
     * @param position 头位置
     * @param direction 方向
     */
    public Flick(int time, double position, double direction, double flickLength) {
        super(time, position);
        type = "Flick";
        setDirection(direction);
        setSlideLength(flickLength);
    }

    /**
     * 轨道-滑参构造法
     * @param time 时间
     * @param orbit 所处轨道
     * @param maxOrbit 最大轨道数
     * @param flickPar 滑动参数
     */
    public Flick(int time, int orbit, int maxOrbit, int flickPar) {
        this(time,calPosByOrbit(orbit,maxOrbit),flickPar>0?180:0,Math.abs(flickPar) * 1.0/maxOrbit);
    }

    /**
     * 获取滑键方向
     * @return 方向
     */
    public double getDirection() {
        return direction;
    }

    /**
     * 设置滑键方向
     * @param direction 方向
     */
    public void setDirection(double direction) {
        if (direction<=360 &&direction>=0)
            this.direction = direction;
        else {
            Logger.info("使用非法方向->" + direction + '\n' + "物件:" +this);
        }
    }

    /**
     * 获取滑键滑动长度
     * @return 滑动长度
     */
    public double getSlideLength() {
        return slideLength;
    }

    /**
     * 设置滑键滑动长度
     * @param slideLength 长度
     */
    public void setSlideLength(double slideLength) {
        this.slideLength = slideLength;
    }

    @Override
    public Flick clone() {//made
        return new Flick(getTime(),getPosition(),getDirection(),getSlideLength());
    }

    @Override
    public String toString() {
        return '\n'+"      "+"Flick{" +
                ", 时间=" + time +
                ", 位置=" + position +
                ", 方向=" + direction +
                ", 距离=" + slideLength +
                ", key音=" + keyAudioPath +
                '}';
    }
}
