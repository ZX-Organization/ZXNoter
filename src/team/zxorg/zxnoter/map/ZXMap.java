package team.zxorg.zxnoter.map;

import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.util.ArrayList;

/**
 * zx谱面类
 */
public class ZXMap {
    /**
     * 所有打击物件
     */
    public ArrayList<BaseNote> notes;
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
    public int findClosestNote(long time){
        if (0 > time) return 0;
        if (notes.get(notes.size()-1).timeStamp < time) return notes.size()-1;

        int searchRes = binarySearch(time , 0 , notes.size()-1);

        //判断最近的
        if (searchRes == 0 ){
            int next = searchRes + 1;
            if (Math.abs(notes.get(next).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time))
                return next;
            else return searchRes;
        } else if (searchRes == notes.size()-1){
            int previous = searchRes-1;
            if (Math.abs(notes.get(previous).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time))
                return previous;
            else return searchRes;
        } else {
            int res = searchRes;
            int previous = searchRes-1;
            int next = searchRes + 1;
            if (Math.abs(notes.get(previous).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time)) res = previous;
            if (Math.abs(notes.get(next).timeStamp-time) < Math.abs(notes.get(searchRes).timeStamp-time)) res = next;
            return res;
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
                highIndex = mid;
            }
        }
        return binarySearch(time,lowIndex , highIndex);
    }

    /**
     * 插入按键,向按键列表中插入一个新按键,返回插入后所处的下标
     * @param note
     * @return 插入后所处下标
     */
    public int insertNote(BaseNote note){
        int index = binarySearch(note.timeStamp , 0 , notes.size()-1);
        ArrayList<BaseNote> backNotes = new ArrayList<>();
        while (index < notes.size()){
            backNotes.add(notes.get(index));
            notes.remove(index);
        }
        notes.add(note);
        notes.addAll(backNotes);
        return index;
    }

    /**
     * 删除按键
     * @param note
     * @return 删除是否成功
     */
    public boolean deleteNote(BaseNote note){
        return notes.remove(note);
    }

    /**
     * 移动一个按键
     * @param note 要移动的按键
     * @param time 要移动到的时间戳
     * @return 移动后所处的下标
     */
    public int moveNote(BaseNote note , long time){
        BaseNote newNote = note.clone();
        newNote.timeStamp = time;
        notes.remove(note);
        return insertNote(newNote);
    }
    public void modifyComplexNote(ComplexNote complexNote , BaseNote willBeEditNote , int parameter){
        //查找到要编辑的物件在组合键中的下标
        int index = complexNote.notes.indexOf(willBeEditNote) + 1;
        //缓存此下标之后的按键并从组合键中移除
        ArrayList<FixedOrbitNote> backNotes = new ArrayList<>();
        while (index < complexNote.notes.size()){
            backNotes.add(complexNote.notes.get(index));
            complexNote.notes.remove(index);
        }
        //处理编辑结果
        if (willBeEditNote instanceof LongNote longNote){
            //编辑的是组合键中的长键
            // 修改此按键的持续时间
            // 修改此按键之后的时间戳(相对偏移)
            long previousSustainedTime = longNote.sustainedTime;
            longNote.sustainedTime = parameter;
            for (int i = 0; i < backNotes.size(); i++) {
                backNotes.get(i).timeStamp += (parameter - previousSustainedTime);
            }
        }else if (willBeEditNote instanceof SlideNote slideNote){
            //编辑的是组合键中的滑键
            //修改此按键的滑动参数
            //修改此按键之后的轨道(相对偏移)
            int previousSlideArg = slideNote.slideArg;
            slideNote.slideArg = parameter;
            for (int i = 0; i < backNotes.size(); i++) {
                backNotes.get(i).orbit += (parameter - previousSlideArg);
            }
        }
        //将修改好的缓存列表加回组合键
        complexNote.notes.addAll(backNotes);
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
