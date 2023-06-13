package team.zxorg.zxnoter.map;

import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.Timing;

import java.util.ArrayList;

/**
 * zx谱面类
 */
public class ZXMap {
    /**
     * 所有打击物件
     */
    public ArrayList<BaseNote> notes = new ArrayList<>();
    /**
     * 所有时间点
     */
    public ArrayList<Timing> timingPoints;
    public LocalizedMapInfo localizedMapInfo;

    /**
     * 二分查找到一个时间戳离得最近的按键
     * @param time
     * @return
     */
    public BaseNote findClosestNote(long time){
        //判断时间合法性
        if (0 > time || notes.get(notes.size()-1).timeStamp < time){
            return null;
        }
        int searchRes = binarySearch(time , 0 , notes.size()-1);

        //判断最近的
        if (searchRes == 0 ){
            int next = searchRes + 1;
            if (Math.abs(notes.get(next).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time))
                return notes.get(next);
            else return notes.get(searchRes);
        } else if (searchRes == notes.size()-1){
            int previous = searchRes-1;
            if (Math.abs(notes.get(previous).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time))
                return notes.get(previous);
            else return notes.get(searchRes);
        } else {
            int res = searchRes;
            int previous = searchRes-1;
            int next = searchRes + 1;
            if (Math.abs(notes.get(previous).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time)) res = previous;
            if (Math.abs(notes.get(next).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time)) res = next;
            return notes.get(res);
        }
    }
    private int binarySearch(long time , int lowIndex , int highIndex){
        int mid = (lowIndex + highIndex) / 2;
        if (time > notes.get(mid).timeStamp){
            //查找的时间在中点之后
            if (lowIndex == highIndex){
                return lowIndex;
            }else {
                lowIndex = mid + 1;
            }

        }else {
            //查找的时间在中点之前
            if (lowIndex == highIndex){
                return highIndex;
            }else {
                highIndex = mid - 1;
            }
        }
        return binarySearch(time,lowIndex , highIndex);
    }
    @Override
    public String toString() {
        return "ZXMap{" +
                "物件列表=" + notes + '\n' +
                ", 时间点=" + timingPoints + '\n' +
                ", 本地化信息=" + localizedMapInfo + '\n' +
                '}';
    }
}
