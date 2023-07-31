package team.zxorg.zxnoter.info;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.info.map.ZXMInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 本地化信息类
 * @author xiang2333
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String> allInfo;
    ArrayList<ChangeListener> addInterfaceList;

    public UnLocalizedMapInfo() {
        allInfo = new HashMap<>();
        addInterfaceList = new ArrayList<>();
    }
    public UnLocalizedMapInfo(JSONObject infoJson) {
        allInfo = new HashMap<>();
        addInterfaceList = new ArrayList<>();

        Set<String> keys = infoJson.keySet();
        for (String key:keys) {
            allInfo.put(ZXMInfo.valueOf(key),infoJson.getString(key));
        }

    }

    public void setInfo(ZXMInfo key, String value) {

        allInfo.put(key, value);
        for (ChangeListener addCallBack:addInterfaceList) {
            addCallBack.callInfo(key,value);
        }
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
        for (ZXMInfo zxmInfo:ZXMInfo.values()) {
            info.setInfo(zxmInfo, zxmInfo.getDefaultValue());
        }
        return info;
    }

    /**
     *  设置一个添加接口
     * @param listener 添加接口
     */
    public void addListener(ChangeListener listener) {
        this.addInterfaceList.add(listener) ;
    }

    /**
     * 转换为json存储
     * @return json对象
     */
    public JSONObject toJson(){
        JSONObject unlocalizedInfoJson = new JSONObject();

        Set<ZXMInfo> keyset = allInfo.keySet();
        for (ZXMInfo info:keyset) {
            unlocalizedInfoJson.put(info.name(), allInfo.get(info));
        }

        return unlocalizedInfoJson;
    }
    @Override
    public String toString() {
        return '\n' + "LocalizedMapInfo{" + allInfo +
                '}';
    }
    public interface ChangeListener {
        void callInfo(ZXMInfo info,String value);
    }

}
