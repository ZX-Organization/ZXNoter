package team.zxorg.zxnoter.map;

import java.util.HashMap;

public class LocalizedMapInfo{
    public HashMap<String , String > allInfo;

    public LocalizedMapInfo() {
        allInfo  = new HashMap<>();
    }

    public void addInfo(String key , String value){
        allInfo.put(key , value);
    }
    public String getInfo(String key){
        return allInfo.get(key);
    }
    public interface UnLocalizing {
        String unLocalize(String name);
    }
    public interface Localizing {
        String Localize(String name);
    }
}
