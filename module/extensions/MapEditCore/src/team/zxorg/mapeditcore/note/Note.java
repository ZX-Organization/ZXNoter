package team.zxorg.mapeditcore.note;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.gson.GsonManager;

import javax.sound.sampled.AudioInputStream;
import java.lang.reflect.Modifier;

public class Note implements Comparable<Note>,Cloneable{
    /**
     * 物件时间戳
     */
    protected int time;
    /**
     * 物件水平位置(0~1),
     */
    protected double position;

    /**
     * key音输入流引用
     */
    public AudioInputStream keyAudio;

    /**
     * 无key音构造
     * @param time 时间
     * @param position 位置
     */
    public Note(int time, double position) {
        setTime(time);
        setPosition(position);
    }

    /**
     * 带key音构造
     * @param time 时间
     * @param position 位置
     * @param keyAudio key音
     */
    public Note(int time, double position, AudioInputStream keyAudio) {
        this.time = time;
        this.position = position;
        this.keyAudio = keyAudio;
    }

    /**
     * 通过定轨构造
     * @param time 时间
     * @param orbit 所处轨道
     * @param maxOrbit 最大轨道数
     */
    public Note(int time, int orbit, int maxOrbit){
        this.time = time;
        this.position = calPosByOrbit(orbit,maxOrbit);
    }

    /**
     * 通过定轨带key音构造
     * @param time 时间
     * @param orbit 所处轨道
     * @param maxOrbit 最大轨道数
     */
    public Note(int time, int orbit, int maxOrbit,AudioInputStream keyAudio){
        this.time = time;
        this.position = calPosByOrbit(orbit,maxOrbit);
        this.keyAudio = keyAudio;
    }

    /**
     * 获取物件时间戳
     * @return 时间
     */
    public int getTime(){return time;}

    /**
     * 设置物件时间戳
     * @param time 时间
     */
    public void setTime(int time){
        //设置时间不可小于0
        if (time>=0)
            this.time = time;
        else {
            Logger.info("使用非法时间戳->" + time + '\n' + "物件:" + this);
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

    /**
     * 获取此物件的key音
     * @return 此物件key音音频流
     */
    public AudioInputStream getKeyAudio() {
        return keyAudio;
    }

    /**
     * 设置此物件的key音
     * @param keyAudio 要设置的key音音频流
     */
    public void setKeyAudio(AudioInputStream keyAudio) {
        this.keyAudio = keyAudio;
    }

    /**
     * 根据定轨计算实际位置
     * @param orbit 所处轨道(1~max)
     * @param maxOrbit 最大轨道数
     * @return 所处位置
     */
    public static double calPosByOrbit(int orbit,int maxOrbit){
        return ((1.0/maxOrbit)/2) + (orbit-1)*(1.0/maxOrbit);
    }

    public String toJson(){
        if (keyAudio == null) {
            return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PUBLIC).create().toJson(this);
        }else {
            return GsonManager.toJson(this);
        }
    }
    /**
     * 比较两note顺序
     */
    @Override
    public int compareTo(Note note) {
        if (time>note.time)
            return 1;
        else if (time< note.time)
            return -1;
        else {
            return Double.compare(position, note.position);
        }
    }

    /**
     * 物件克隆
     */
    @Override
    public Note clone(){
        return new Note(time,position,keyAudio);
    }

    @Override
    public String toString() {
        return '\n'+"      "+"Note{" +
                "时间=" + time +
                ", 位置=" + position +
                ", key音=" + keyAudio +
                '}';
    }
}
