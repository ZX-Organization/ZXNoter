package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.MixNote;
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

    @Override
    public String toString() {
        return "ZXMap{" +
                "metaData=" + metaData +
                ", timings=" + timings +
                ", notes=" + notes +
                '}';
    }

    public ZXMap convert() {
        ZXMap map = new ZXMap();
        map.orbitCount = orbitCount;
        map.preferenceBpm = preferenceBpm;
        map.timings.addAll(timings);
        map.metaData = metaData;
        for (IMapElement note:notes){
            if (note instanceof MixNote mixNote){
                map.notes.addAll(mixNote.getChildNotes());
            }else {
                map.notes.add(note);
            }
        }
        map.notes.sort(IMapElement::compareTo);
        return map;
    }
}
