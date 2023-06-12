package team.zxorg.zxnoter.map.mapInfos;

/**
 * imd词条及反本地化名
 */
public enum ImdInfos{

    MapLength("MapLength"),
    TimingCount("TimingCount"),
    TabRows("TabRows"),
    ImdTitle("Title"),
    ImdVersion("Version"),
    ImdKeyCount("KeyCount"),
    ImdBgPath("BgPath"),
    ImdAudioPath("AudioPath")
    ;
    private final String unLocalizedName;
    ImdInfos(String unLocalizedName) {
        this.unLocalizedName = unLocalizedName;
    }
    public String unLocalize() {
        return unLocalizedName;
    }
}
