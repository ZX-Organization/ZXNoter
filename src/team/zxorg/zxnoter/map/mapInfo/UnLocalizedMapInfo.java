package team.zxorg.zxnoter.map.mapInfo;

import java.util.HashMap;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String> allInfo;
    AddCallBack addInterface;

    public UnLocalizedMapInfo() {
        allInfo = new HashMap<>();
    }

    public void addInfo(ZXMInfo key, String value) {

        allInfo.put(key, value);
        if (addInterface != null)
            addInterface.callInfo(key,value);
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
        return info;
    }

    /**
     *  设置一个添加接口
     * @param addInterface 添加接口
     */
    public void setAddInterface(AddCallBack addInterface) {
        this.addInterface = addInterface;
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
