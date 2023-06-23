package team.zxorg.zxnoter.map.mapInfo;

import java.util.HashMap;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String > allInfo;

    public UnLocalizedMapInfo() {
        allInfo  = new HashMap<>();
    }

    public void addInfo(ZXMInfo key , String value){
        allInfo.put(key , value);
    }
    public String getInfo(ZXMInfo key){
        return allInfo.get(key);
    }

    @Override
    public String toString() {
        return '\n' +"LocalizedMapInfo{" +allInfo +
                '}';
    }

}
