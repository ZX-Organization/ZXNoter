package team.zxorg.zxnoter_old.map;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter_old.info.editor.ZXNInfo;
import team.zxorg.zxnoter_old.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter_old.note.BaseNote;
import team.zxorg.zxnoter_old.note.fixedorbit.*;
import team.zxorg.zxnoter_old.note.timing.Timing;
import team.zxorg.zxnoter_old.note.timing.ZXTiming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * zx谱面类
 */
public class ZXMap {
    /**
     * 所有打击物件
     */
    public ArrayList<BaseNote> notes;
    private ArrayList<BaseNote> separateNotes;
    /**
     * 所有时间点
     */
    public ArrayList<Timing> timingPoints;
    public UnLocalizedMapInfo unLocalizedMapInfo;

    /**
     * 编辑器信息
     */
    public HashMap<String,String> editorInfo;


    /**
     * 新建map
     */
    public ZXMap() {
        notes = new ArrayList<>();
        separateNotes = new ArrayList<>();
        timingPoints = new ArrayList<>();
        unLocalizedMapInfo = UnLocalizedMapInfo.getDefaultInfo();
        //初始化编辑器信息
        editorInfo = new HashMap<>();
        for (ZXNInfo info:ZXNInfo.values())
            editorInfo.put(info.name(),info.getDefVal());
    }

    public ZXMap(ArrayList<BaseNote> notes, ArrayList<Timing> timingPoints, UnLocalizedMapInfo unLocalizedMapInfo) {
        this.notes = notes;
        this.timingPoints = timingPoints;
        this.unLocalizedMapInfo = unLocalizedMapInfo;
        editorInfo = new HashMap<>();
    }

    public ZXMap(Path zxnFilePath) throws IOException {
        File zxnFile = zxnFilePath.toFile();
        FileInputStream fis = new FileInputStream(zxnFile);
        JSONObject zxnJson = JSON.parseObject(fis);
        fis.close();
        //编辑器信息
        editorInfo = new HashMap<>();
        JSONObject editorJson = zxnJson.getJSONObject("editor");
        Set<String> editorKeys = editorJson.keySet();
        for (String editorKey:editorKeys) editorInfo.put(editorKey,editorJson.getString(editorKey));

        //反本地化信息读取
        unLocalizedMapInfo = new UnLocalizedMapInfo(zxnJson.getJSONObject("allInfo"));

        //timing读取
        timingPoints = new ArrayList<>();
        JSONArray timingJsonArr = zxnJson.getJSONArray("timing");
        for (int i = 0; i < timingJsonArr.size(); i++) {
            JSONObject timingJson = timingJsonArr.getJSONObject(i);
            if (timingJson.size() == 4){
                //普通timing
                timingPoints.add(new Timing(timingJson));
            }
            if (timingJson.size()==10){
                //zxTiming
                timingPoints.add(new ZXTiming(timingJson));
            }
        }
        //按键读取
        notes = new ArrayList<>();
        JSONArray noteJsonArr = zxnJson.getJSONArray("note");
        for (int i = 0; i < noteJsonArr.size(); i++) {
            JSONObject noteJson = noteJsonArr.getJSONObject(i);
            if (noteJson.size()==3){
                //普通单键
                notes.add(new FixedOrbitNote(noteJson));
            }
            if (noteJson.containsKey("slideArg")){
                //滑键
                notes.add(new SlideNote(noteJson));
            }
            if (noteJson.containsKey("sustainedTime")){
                //长条
                //判断是否为特殊长条

                if (noteJson.size() == 10) notes.add(new CustomLongNote(noteJson));
                    else notes.add(new LongNote(noteJson));
            }
            if (noteJson.containsKey("child")){
                if (noteJson.getJSONArray("child").size() == 0) continue;
                notes.add(new ComplexNote(noteJson));
            }
            if (noteJson.size()==9){
                //特殊单键
                notes.add(new CustomNote(noteJson));
            }
        }
        //System.out.println(notes);
        //System.out.println(timingPoints);
        //System.out.println(unLocalizedMapInfo);
        //System.out.println(zxnJson);
    }

    /**
     * 二分查找到离指定时间戳最近的Timing(可能一个时间戳上有多个Timing)
     *
     * @param time 指定时间戳
     * @return 结果时间戳列表
     */
    public ArrayList<Timing> findClosestTimings(long time) {
        ArrayList<Timing> findResults = new ArrayList<>();
        int res = findClosestTimingIndex(time);
        if (res == -1) return findResults;
        if (timingPoints.size()==0)return findResults;
        Timing resTiming = timingPoints.get(res);
        int tempIndex = res;
        Timing tempTiming;
        //向前查找
        while (tempIndex > 0 && (tempTiming = timingPoints.get(--tempIndex)).timestamp == resTiming.timestamp) {
            findResults.add(tempTiming);
        }
        //加入自身结果
        findResults.add(resTiming);
        //复原索引
        tempIndex = res;
        //向后查找

        while (tempIndex + 1 < timingPoints.size()) {
            if (tempIndex < notes.size() - 1 && (tempTiming = timingPoints.get(++tempIndex)).timestamp == resTiming.timestamp)
                findResults.add(tempTiming);
        }
        return findResults;
    }

    /**
     * 二分查找到一个时间戳离得最近的按键
     *
     * @param time 指定时间戳
     * @return 结果物件列表
     */
    public ArrayList<BaseNote> findClosestNotes(long time, boolean isDeep) {
        ArrayList<BaseNote> findResults = new ArrayList<>();
        int res = findClosestNoteIndex(time, notes, isDeep);
        if (res == -1) return findResults;
        BaseNote resNote = notes.get(res);
        int tempIndex = res;
        BaseNote tempNote;
        //向前查找
        while (tempIndex > 0 && (tempNote = notes.get(--tempIndex)).timeStamp == resNote.timeStamp) {
            findResults.add(tempNote);
        }
        //加入自身结果
        findResults.add(resNote);
        //复原索引
        tempIndex = res;
        //向后查找
        while (tempIndex < notes.size() - 1 && (tempNote = notes.get(++tempIndex)).timeStamp == resNote.timeStamp) {
            findResults.add(tempNote);
        }
        //排序
        findResults.sort(BaseNote::compareTo);
        return findResults;
    }

    /**
     * 获取指定时间段内的全部按键
     *
     * @param time  指定时间头
     * @param scale 指定时间长度范围
     * @return 查询到的全部按键
     */
    public ArrayList<BaseNote> getScaleNotes(long time, long scale, boolean isDeepFind) {
        if (separateNotes == null || separateNotes.size() == 0) {
            initSeparateNotes();
        }
        ArrayList<BaseNote> timeNotes = findClosestNotes(time, isDeepFind);
        //结果预定义
        ArrayList<BaseNote> desNotes = new ArrayList<>();

        if (timeNotes.size() == 0) return desNotes;
        //结果首按键
        BaseNote firstNote = timeNotes.get(0);
        //遍历起始下标
        int startIndex;
        if (isDeepFind) {
            startIndex = separateNotes.indexOf(firstNote);
        } else {
            startIndex = notes.indexOf(firstNote);
        }

        //从指定位置向后遍历范围内按键
        if (isDeepFind) {
            //深度查询
            startIndex = 0;
            //向后找到时间范围内的按键
            while (startIndex <= separateNotes.size() - 1 && separateNotes.get(startIndex).timeStamp < time) {
                startIndex++;
            }
            for (int i = startIndex; i < separateNotes.size(); i++) {
                BaseNote note = separateNotes.get(i);
                if (note.timeStamp <= time + scale) {
                    desNotes.add(note);
                } else {
                    break;
                }
            }
        } else {
            //向后找到时间范围内的按键
            while (startIndex <= notes.size() - 1 && notes.get(startIndex).timeStamp < time) {
                startIndex++;
            }
            for (int i = startIndex; i < notes.size(); i++) {
                BaseNote note = notes.get(i);
                if (note.timeStamp <= time + scale) {
                    desNotes.add(note);
                } else {
                    break;
                }
            }
        }
        return desNotes;
    }

    /**
     * 寻找指定按键列表中距离指定时间戳最近的按键下标
     *
     * @param time  指定时间戳
     * @param notes 指定列表
     * @return 按键下标
     */
    private int findClosestNoteIndex(long time, ArrayList<BaseNote> notes, boolean isDeep) {
        if (notes.size() == 0) return -1;
        if (0 > time) return 0;
        if (notes.get(notes.size() - 1).timeStamp < time) return notes.size() - 1;
        int searchRes = binarySearchNote(time, 0, notes.size() - 1, isDeep);
        //判断最近的
        if (searchRes == 0) {//找到头部
            int next = searchRes + 1;
            if (next >= notes.size())//既是头也是尾
                return searchRes;
            if (Math.abs(notes.get(next).timeStamp - time) < Math.abs(notes.get(searchRes).timeStamp - time))
                return next;
            else return searchRes;
        } else if (searchRes == notes.size() - 1) {//找到尾部
            int previous = searchRes - 1;
            if (previous <= 0)//既是尾也是头
                return searchRes;
            if (Math.abs(notes.get(previous).timeStamp - time) < Math.abs(notes.get(searchRes).timeStamp - time))
                return previous;
            else return searchRes;
        } else {
            int res = searchRes;
            int previous = searchRes - 1;
            int next = searchRes + 1;
            if (Math.abs(notes.get(previous).timeStamp - time) < Math.abs(notes.get(searchRes).timeStamp - time))
                res = previous;
            if (Math.abs(notes.get(next).timeStamp - time) < Math.abs(notes.get(searchRes).timeStamp - time))
                res = next;

            return res;
        }
    }

    /**
     * 寻找指定按键列表中距离指定时间戳最近的timing下标
     *
     * @param time 指定时间戳
     * @return 时间点下标
     */
    private int findClosestTimingIndex(long time) {
        if (0 > time) return 0;
        if (timingPoints.size() == 0) return -1;
        if (timingPoints.get(timingPoints.size() - 1).timestamp < time) return timingPoints.size() - 1;
        int searchRes = binarySearchTiming(time, 0, timingPoints.size() - 1);
        //判断最近的
        if (searchRes == 0) {//找到头部
            int next = searchRes + 1;
            if (next >= timingPoints.size())//找到的既是头也是尾
                return searchRes;
            if (Math.abs(timingPoints.get(next).timestamp - time) < Math.abs(timingPoints.get(searchRes).timestamp - time))
                return next;
            else return searchRes;
        } else if (searchRes == timingPoints.size() - 1) {//找到最后
            int previous = searchRes - 1;
            if (previous == 0)//找到的既是尾也是头
                return searchRes;
            if (Math.abs(timingPoints.get(previous).timestamp - time) < Math.abs(timingPoints.get(searchRes).timestamp - time))
                return previous;
            else return searchRes;
        } else {
            //找到中间部分
            int res = searchRes;
            int previous = searchRes - 1;
            int next = searchRes + 1;
            if (Math.abs(timingPoints.get(previous).timestamp - time) < Math.abs(timingPoints.get(searchRes).timestamp - time))
                res = previous;
            if (Math.abs(timingPoints.get(next).timestamp - time) < Math.abs(timingPoints.get(searchRes).timestamp - time))
                res = next;

            return res;
        }
    }

    /**
     * 二分搜索note
     * @param time
     * @param lowIndex
     * @param highIndex
     * @param isDeep
     * @return
     */
    private int binarySearchNote(long time, int lowIndex, int highIndex, boolean isDeep) {
        ArrayList<BaseNote> tempNotes;
        if (isDeep) tempNotes = separateNotes;
        else tempNotes = notes;

        int mid = (lowIndex + highIndex) / 2;
        if (time > tempNotes.get(mid).timeStamp) {
            //查找的时间在中点之后
            if (lowIndex == highIndex) {
                return lowIndex;
            } else {
                lowIndex = mid + 1;
            }

        } else {
            //查找的时间在中点之前
            if (lowIndex == highIndex) {
                return highIndex;
            } else {
                highIndex = mid;
            }
        }
        return binarySearchNote(time, lowIndex, highIndex, isDeep);
    }

    /**
     * 二分搜索Timing
     * @param time
     * @param lowIndex
     * @param highIndex
     * @return
     */
    private int binarySearchTiming(long time, int lowIndex, int highIndex) {
        int mid = (lowIndex + highIndex) / 2;
        if (time > timingPoints.get(mid).timestamp) {
            //查找的时间在中点之后
            if (lowIndex == highIndex) {
                return lowIndex;
            } else {
                lowIndex = mid + 1;
            }

        } else {
            //查找的时间在中点之前
            if (lowIndex == highIndex) {
                return highIndex;
            } else {
                highIndex = mid;
            }
        }
        return binarySearchTiming(time, lowIndex, highIndex);
    }
    /**
     * 插入按键,向按键列表中插入一个新按键,返回插入后物件总数变化
     *
     * @param note
     * @return 插入后大小变化
     */
    public int insertNote(BaseNote note) {
        int count=1;
        //检测空图
        if (notes.size()==0)
            if (separateNotes==null||separateNotes.size()==0)
                initSeparateNotes();
        //向原map插入
        notes.add(note);
        //检测组合键
        if (note instanceof ComplexNote complexNote){
            separateNotes.addAll(complexNote.notes);
            //将组合键子键物件总数替换添加计数,此计数不包含组合键本身
            count=complexNote.notes.size();
        } else separateNotes.add(note);
        //排序
        notes.sort(BaseNote::compareTo);
        separateNotes.sort(BaseNote::compareTo);

        return count;
    }

    /**
     * 删除按键
     *
     * @param note
     * @return 删除的物件计数
     */
    public int deleteNote(BaseNote note) {
        int count = 1;

        if (note instanceof ComplexNote complexNote){
            separateNotes.removeAll(complexNote.notes);
            count=complexNote.notes.size();
        }else
            separateNotes.remove(note);

        notes.remove(note);
        return count;
    }


    /**
     * 移动一个按键到指定时间戳
     *
     * @param note 要移动的按键
     * @param time 要移动到的时间戳
     * @return 移动后所处的下标
     */
    public int moveNote(BaseNote note, long time) {
        BaseNote newNote = note.clone();
        if (newNote instanceof ComplexNote complexNote) {
            long previousTime = complexNote.timeStamp;
            for (BaseNote baseNote : complexNote.notes) {
                baseNote.timeStamp += (time - previousTime);
            }
        }
        newNote.timeStamp = time;
        notes.remove(note);
        return insertNote(newNote);
    }

    /**
     * 移动一个按键到指定的轨道
     *
     * @param note  要移动的按键
     * @param orbit 要移动到的轨道
     */
    public void moveNote(BaseNote note, int orbit) {
        if (orbit == note.getOrbit()) {
            //轨道与原先相同
            return;
        }
        if (note instanceof ComplexNote complexNote) {
            //判断为组合键
            int previousOrbit = complexNote.orbit;
            for (BaseNote baseNote : complexNote.notes) {
                baseNote.setOrbit(baseNote.getOrbit() + (orbit - previousOrbit));
            }
        }
        note.setOrbit(orbit);
    }

    private void initSeparateNotes() {
        separateNotes = new ArrayList<>();
        for (BaseNote note : notes) {
            if (note instanceof ComplexNote complexNote) {
                separateNotes.addAll(complexNote.notes);
            }else {
                separateNotes.add(note);
            }
        }
        notes.sort(BaseNote::compareTo);
    }
    public void setEditInfo(ZXNInfo info,String value){
        editorInfo.put(info.name(),value);
    }

    public void saveZXN(Path path) throws IOException {
        File absFile = path.toFile();
        /*if (path.toFile().isFile()){
            String fileName = path.getFileName().toString();
            absFile = path.toFile();
        }else {
            absFile = new File(path.toFile().getAbsoluteFile() +"\\" +unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode)+".zxn");
        }*/

        //System.out.println(absFile);
        if (!absFile.exists()) absFile.createNewFile();

        JSONObject zxn = new JSONObject();

        zxn.put("allInfo",unLocalizedMapInfo.toJson());

        JSONArray timings = new JSONArray();
        for (Timing timing:timingPoints)timings.add(timing.toJson());
        zxn.put("timing",timings);

        Set<String> editorInfoKeyset = editorInfo.keySet();
        JSONObject editJson = new JSONObject();
        for (String editorInfoName:editorInfoKeyset)
            editJson.put(editorInfoName,editorInfo.get(editorInfoName));
        zxn.put("editor",editJson);

        JSONArray notesJson = new JSONArray();
        for (BaseNote note:notes)
            notesJson.add(note.toJson());
        zxn.put("note",notesJson);

        FileOutputStream fileOutputStream = new FileOutputStream(absFile);
        JSON.writeTo(fileOutputStream,zxn);

    }
    public int getSeparateNotesSize(){
        return separateNotes.size();
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
