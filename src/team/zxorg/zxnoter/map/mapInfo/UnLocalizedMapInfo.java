package team.zxorg.zxnoter.map.mapInfo;

import java.util.HashMap;

/**
 * 本地化信息类
 */
public class UnLocalizedMapInfo {
    public HashMap<ZXMInfo, String> allInfo;

    public UnLocalizedMapInfo() {
        allInfo = new HashMap<>();
    }

    public void addInfo(ZXMInfo key, String value) {
        allInfo.put(key, value);
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

    @Override
    public String toString() {
        return '\n' + "LocalizedMapInfo{" + allInfo +
                '}';
    }

}
