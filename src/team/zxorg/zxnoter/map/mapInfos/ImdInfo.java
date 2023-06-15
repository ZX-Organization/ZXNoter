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
    ImdKeyCount("KeyCount", ""),
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
    public enum ConvertMethod{
        BASE("滑键转成单键,组合键不拆分,直接转为单键"),
        BASE_SLIDE("拆分组合键,滑键转为同时间戳的两个或多个单键"),
        ADVANCE_SLIDE("拆分组合键,滑键转为略微岔开时间戳的两个或多个 单键(按节奏大师判定25ms岔开)"),
        ;
        public final String description;

        ConvertMethod(String description) {
            this.description = description;
        }
    }
}
