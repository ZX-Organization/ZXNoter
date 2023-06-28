package team.zxorg.zxnoter.map.mapInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String> allInfo;
    ArrayList<AddCallBack> addInterfaceList;

    public UnLocalizedMapInfo() {
        allInfo = new HashMap<>();
        addInterfaceList = new ArrayList<>();
    }

    public void addInfo(ZXMInfo key, String value) {

        allInfo.put(key, value);
        for (AddCallBack addCallBack:addInterfaceList)
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
        for (OsuInfo osuInfo:OsuInfo.values()){
            info.addInfo(osuInfo.unLocalize(), osuInfo.getDefaultValue());
        }
        for (ImdInfo imdInfo:ImdInfo.values()){
            info.addInfo(imdInfo.unLocalize(), imdInfo.getDefaultValue());
        }
        info.addInfo(ZXMInfo.ObjectCount,"0");
        return info;
    }

    /**
     *  设置一个添加接口
     * @param addInterface 添加接口
     */
    public void addAddInterface(AddCallBack addInterface) {
        this.addInterfaceList.add(addInterface) ;
    }

    @Override
    public String toString() {
        return '\n' + "LocalizedMapInfo{" + allInfo +
                '}';
    }
    public interface AddCallBack{
        void callInfo(ZXMInfo info,String value);
    }

}
