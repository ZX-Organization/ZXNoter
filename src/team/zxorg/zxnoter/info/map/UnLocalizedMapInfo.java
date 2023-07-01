package team.zxorg.zxnoter.info.map;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.map.ZXMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String> allInfo;
    ArrayList<AddListener> addInterfaceList;

    public UnLocalizedMapInfo() {
        allInfo = new HashMap<>();
        addInterfaceList = new ArrayList<>();
    }

    public void setInfo(ZXMInfo key, String value) {

        allInfo.put(key, value);
        for (AddListener addCallBack:addInterfaceList)
            addCallBack.callInfo(key,value);
    }

    public String getInfo(ZXMInfo key) {
        return allInfo.get(key);
    }

    /**
     * 初始化新的info
     * @return 默认mapInfo
     */
    public static UnLocalizedMapInfo getDefaultInfo() {
        UnLocalizedMapInfo info = new UnLocalizedMapInfo();
        for (ZXMInfo zxmInfo:ZXMInfo.values())
            info.setInfo(zxmInfo, zxmInfo.getDefaultValue());
        return info;
    }

    /**
     *  设置一个添加接口
     * @param addInterface 添加接口
     */
    public void addListener(AddListener addInterface) {
        this.addInterfaceList.add(addInterface) ;
    }

    public JSONObject toJson(){
        JSONObject unlocalizedInfoJson = new JSONObject();

        Set<ZXMInfo> keyset = allInfo.keySet();
        for (ZXMInfo info:keyset)
            unlocalizedInfoJson.put(info.name(), allInfo.get(info));

        return unlocalizedInfoJson;
    }
    @Override
    public String toString() {
        return '\n' + "LocalizedMapInfo{" + allInfo +
                '}';
    }
    public interface AddListener {
        void callInfo(ZXMInfo info,String value);
    }

}
