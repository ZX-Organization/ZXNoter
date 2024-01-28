package team.zxorg.mapeditcore.note;

import com.google.gson.Gson;
import team.zxorg.extensionloader.core.Logger;

public class Note {
    /**
     * 物件时间戳
     */
    private int time;
    /**
     * 物件水平位置(0~1),
     */
    private double position;

    /**
     * 获取物件时间戳
     * @return 时间
     */
    public int getTime(){return time;}

    /**
     * 设置物件时间戳
     * @param time 时间
     * @return 结果
     */
    public int setTime(int time){
        //设置时间不可小于0
        if (time>=0)
            return this.time = time;
        else {
            Logger.info("使用非法时间戳->" + time + '\n' + "物件:" + this);
            return this.time;
        }
    }

    /**
     * 获取物件位置(0~1),0为最左侧,1最右侧
     * @return 结果
     */
    public double getPosition() {return position;}

    /**
     * 设置物件位置(0~1),0为最左侧,1最右侧
     * @param position 位置
     */
    public void setPosition(double position) {
        if (position <=1 && position>=0)
            this.position = position;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
        /*return "Note{" +
                "时间=" + time +
                ", 位置=" + position +
                '}';*/
    }
}
