package team.zxorg.zxnoter.map;

import team.zxorg.zxnoter.map.mapInfos.ImdInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.note.timing.Timing;

import java.util.ArrayList;
import java.util.Arrays;

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
    public UnLocalizedMapInfo unLocalizedMapInfo;
    public ZXMap(){}
    public ZXMap(ArrayList<BaseNote> notes, ArrayList<Timing> timingPoints, UnLocalizedMapInfo unLocalizedMapInfo) {
        this.notes = notes;
        this.timingPoints = timingPoints;
        this.unLocalizedMapInfo = unLocalizedMapInfo;
    }

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

        if (notes.size()==0||notes.get(notes.size()-1).timeStamp <= note.timeStamp){
            notes.add(note);
            return notes.size()-1;
        }
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
     * 移动一个按键到指定时间戳
     * @param note 要移动的按键
     * @param time 要移动到的时间戳
     * @return 移动后所处的下标
     */
    public int moveNote(BaseNote note , long time){
        BaseNote newNote = note.clone();
        if (newNote instanceof ComplexNote complexNote){
            long previousTime = complexNote.timeStamp;
            for (BaseNote baseNote : complexNote.notes){
                baseNote.timeStamp += (time - previousTime);
            }
        }
        newNote.timeStamp = time;
        notes.remove(note);
        return insertNote(newNote);
    }

    /**
     * 移动一个按键到指定的轨道
     * @param note 要移动的按键
     * @param orbit 要移动到的轨道
     */
    public void moveNote(BaseNote note , int orbit){
        if (orbit == note.getOrbit()){
            //轨道与原先相同
            return;
        }
        if (note instanceof ComplexNote complexNote){
            //判断为组合键
            int previousOrbit = complexNote.orbit;
            for (BaseNote baseNote : complexNote.notes){
                baseNote.setOrbit(baseNote.getOrbit() + (orbit - previousOrbit));
            }
        }
        note.setOrbit(orbit);
    }

    /**
     * 编辑组合键(imd组合键)[折线]
     * @param complexNote 要编辑的组合键
     * @param willBeEditNote 组合键中要编辑的键(长键或者滑键)
     * @param parameter 要修改的最终参数
     */
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

    /**
     * 编辑按键参数
     * @param fixedOrbitNote 要修改的按键
     * @param parameter 参数
     * @return 修改是否成功
     */
    public boolean editNotePar(FixedOrbitNote fixedOrbitNote,int parameter){
        if (fixedOrbitNote instanceof LongNote longNote){
            longNote.sustainedTime = parameter;
            return true;
        }
        if (fixedOrbitNote instanceof SlideNote slideNote){
            slideNote.slideArg = parameter;
            return true;
        }
        return false;
    }
    public ZXMap imdConvertNoComplex(ImdInfo.ConvertMethod imdConvert){
        ZXMap tempMap = new ZXMap(notes,timingPoints,unLocalizedMapInfo);
        tempMap.notes =  new ArrayList<>();

        for (BaseNote tempNote:notes){
            if (tempNote instanceof SlideNote slideNote){
                FixedOrbitNote[] convertNotes = slideNote.convertNote(imdConvert);
                for (FixedOrbitNote fixedOrbitNote:convertNotes){
                    tempMap.insertNote(fixedOrbitNote);
                }
            }else if (tempNote instanceof ComplexNote complexNote){
                FixedOrbitNote[] convertNotes = complexNote.convertNote(imdConvert);
                for (FixedOrbitNote fixedOrbitNote:convertNotes){
                    tempMap.insertNote(fixedOrbitNote);
                }
            }else {
                tempMap.insertNote(tempNote);
            }
        }
        tempMap.unLocalizedMapInfo.addInfo("TabRows",String.valueOf(tempMap.notes.size()));
        return tempMap;
    }
    @Override
    public String toString() {
        return "ZXMap{" +
                "物件列表=" + notes + '\n' +
                ", 时间点=" + timingPoints + '\n' +
                ", 本地化信息=" + unLocalizedMapInfo + '\n' +
                '}';
    }
}
