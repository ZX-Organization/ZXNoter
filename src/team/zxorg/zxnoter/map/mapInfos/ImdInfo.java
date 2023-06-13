package team.zxorg.zxnoter.map.mapInfos;

/**
 * imd词条及反本地化名
 */
public enum ImdInfo {

    MapLength("MapLength", ""),
    TimingCount("TimingCount", ""),
    TabRows("TabRows", ""),
    ImdBpm("BaseBpm", ""),
    ImdTitle("Title", ""),
    ImdVersion("Version", "ez"),
    ImdKeyCount("KeyCount", "4"),
    ImdBgPath("BgPath", ""),
    ImdAudioPath("AudioPath", "");
    private final String unLocalizedName;
    private final String defaultValue;

    ImdInfo(String unLocalizedName, String defaultValue) {
        this.unLocalizedName = unLocalizedName;
        this.defaultValue = defaultValue;
    }

    public String unLocalize() {
        return unLocalizedName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
