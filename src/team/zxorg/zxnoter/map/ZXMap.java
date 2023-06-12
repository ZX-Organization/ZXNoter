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
    public ArrayList<BaseNote> hitObjects = new ArrayList<>();
    /**
     * 所有时间点
     */
    public ArrayList<Timing> timingPoints;
    public LocalizedMapInfo localizedMapInfo;

    @Override
    public String toString() {
        return "ZXMap{" +
                "物件列表=" + hitObjects + '\n' +
                ", 时间点=" + timingPoints + '\n' +
                ", 本地化信息=" + localizedMapInfo + '\n' +
                '}';
    }
}
