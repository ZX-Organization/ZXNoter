package team.zxorg.mapeditcore.mapElement.timing;

import team.zxorg.mapeditcore.mapElement.IMapElement;

public class Timing implements IMapElement,Comparable<IMapElement>{

    /**
     * timing所处时间戳
     */
    int time;
    /**
     * 此处timing的绝对bpm
     */
    double bpm;
    /**
     *  此处timing的变速bpm
     */
    double speed;

    public Timing(int time, double bpm, double speed) {
        this.time = time;
        this.bpm = bpm;
        this.speed = speed;
    }

    public int getTime() {
        return time;
    }

    /**
     * 设置timing时间
     */
    public void setTime(int time) {
        this.time = time;
    }

    public double getBpm() {
        return bpm;
    }

    /**
     * 设置此处timing的绝对bpm
     */
    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     *设置此处bpm速度
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return '\n'+"Timing{" +
                "time=" + time +
                ", bpm=" + bpm +
                ", speed=" + speed +
                '}';
    }

    @Override
    public int compareTo(IMapElement mapElement) {
        return Integer.compare(time,mapElement.getTime());
    }
}
