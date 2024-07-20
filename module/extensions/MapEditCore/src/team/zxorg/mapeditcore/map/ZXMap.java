package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.util.ArrayList;

public class ZXMap {
    /**
     * 元数据
     */
    public IBaseData metaData;
    /**
     * 全部timing点
     */
    public final ArrayList<IMapElement> timings;
    /**
     * 轨道（如果有的话）
     */
    public int orbitCount;
    /**
     * 参考基准bpm（计算变速下落速度）
     */
    public double preferenceBpm;
    /**
     * 全部物件
     */
    public final ArrayList<IMapElement> notes;
    public ZXMap(){
        notes = new ArrayList<>();
        timings = new ArrayList<>();
    }
    /**
     * 有序地插入物件
     * @param note 物件
     */
    public void insertNote(Note note) {
        addElement(note , notes);
    }
    /**
     * 有序地插入timing
     * @param timing 要插入点timing
     */
    public void insertTiming(Timing timing){
        addElement(timing,timings);
    }

    private void addElement(IMapElement element,ArrayList<IMapElement> list){
        int time = element.getTime();
        if (list.size() > 0) {
            if (list.size() > 1) {
                //表中有两个及以上timing
                list.add(MapUtil.binarySearchMapElement(list, time), element);
            } else {
                //表中只有一个timing
                if (list.get(0).getTime() <= time)
                    list.add(element);
            }
        }else
            list.add(element);
    }



    @Override
    public String toString() {
        return "ZXMap{" +
                "metaData=" + metaData +
                ", timings=" + timings +
                ", notes=" + notes +
                '}';
    }
}
