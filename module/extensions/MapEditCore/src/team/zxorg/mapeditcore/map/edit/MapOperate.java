package team.zxorg.mapeditcore.map.edit;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.IBaseData;
import team.zxorg.mapeditcore.mapElement.IMapElement;
import team.zxorg.mapeditcore.mapElement.note.Note;
import team.zxorg.mapeditcore.mapElement.timing.Timing;

import java.util.ArrayList;
import java.util.List;

public class MapOperate {
    /**
     * 操作前物件列表
     */
    private final ArrayList<IMapElement> preOpNotes;
    /**
     * 操作后物件列表
     */
    private final ArrayList<IMapElement> aftOpNotes;

    public MapOperate(){
        preOpNotes = new ArrayList<>();
        aftOpNotes = new ArrayList<>();
    }
    public MapOperate(IMapElement pre,IMapElement aft){
        this();
        if (pre != null)
            preOpNotes.add(pre);
        if (aft !=null)
            aftOpNotes.add(aft);
    }

    public void addPre(IMapElement ele){
        preOpNotes.add(ele);
    }
    public void addPres(IMapElement... eles){
        preOpNotes.addAll(List.of(eles));
    }
    public void addPres(ArrayList<IMapElement> eles){
        preOpNotes.addAll(eles);
    }
    public void addAft(IMapElement ele){
        aftOpNotes.add(ele);
    }
    public void addAft(IMapElement... eles){
        aftOpNotes.addAll(List.of(eles));
    }
    public void addAft(ArrayList<IMapElement> eles){
        aftOpNotes.addAll(eles);
    }

    /**
     * 反向操作，并返回此反向操作的操作
     */
    public MapOperate deOperate(ZXMap map){
        for (IMapElement e:preOpNotes){
            if (e instanceof Note note)
                map.notes.add(note);
            if (e instanceof Timing timing)
                map.timings.add(timing);
        }
        for (IMapElement e:aftOpNotes){
            if (e instanceof Note note)
                map.notes.remove(note);
            if (e instanceof Timing timing)
                map.timings.remove(timing);
        }
        return new MapOperate(this);
    }

    /**
     * 反转构造
     * @param operate 要反转的操作
     */
    private MapOperate(MapOperate operate) {
        preOpNotes = operate.aftOpNotes;
        aftOpNotes = operate.preOpNotes;
    }
}
