package team.zxorg.mapeditcore.timing;

public class Timing {

    /**
     * timing所处时间戳
     */
    int time;
    /**
     * 此处timing的绝对bpm
     */
    double bpm;
    /**
     *  此处timing的变速倍率
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

    public void setTime(int time) {
        this.time = time;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
