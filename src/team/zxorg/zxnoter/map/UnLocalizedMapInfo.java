package team.zxorg.zxnoter.map;

import java.util.HashMap;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<String , String > allInfo;

    public UnLocalizedMapInfo() {
        allInfo  = new HashMap<>();
    }

    public void addInfo(String key , String value){
        allInfo.put(key , value);
    }
    public String getInfo(String key){
        return allInfo.get(key);
    }

    @Override
    public String toString() {
        return '\n' +"LocalizedMapInfo{" +allInfo +
                '}';
    }

}
