package team.zxorg.zxnoter.map.mapInfo;

/**
 * imd词条及反本地化名
 */
public enum ImdInfo{

    MapLength(ZXMInfo.MapLength, ""),
    TimingCount(ZXMInfo.TimingCount, ""),
    TabRows(ZXMInfo.ObjectCount, ""),
    ImdBpm(ZXMInfo.Bpm, ""),
    ImdTitle(ZXMInfo.Title, ""),
    ImdVersion(ZXMInfo.Version, "ez"),
    ImdKeyCount(ZXMInfo.KeyCount, ""),
    ImdBgPath(ZXMInfo.BgPath, ""),
    ImdAudioPath(ZXMInfo.AudioPath, "");
    private final ZXMInfo unLocalizedInfo;
    private final String defaultValue;

    ImdInfo(ZXMInfo unLocalizedInfo, String defaultValue) {
        this.unLocalizedInfo = unLocalizedInfo;
        this.defaultValue = defaultValue;
    }

    public ZXMInfo unLocalize() {
        return unLocalizedInfo;
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
