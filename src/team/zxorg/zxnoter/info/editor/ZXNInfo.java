package team.zxorg.zxnoter.info.editor;

public enum ZXNInfo{

    TimeLinePosition("0"),
    TimeLineZoom("0.4"),
    PreviewZoom("0.1"),
    TimeMode("formatted"),
    AudioPlaySpeed("1.0"),
    AudioChangePitch("false"),
    AudioVolume("0.20"),
    KeySoundVolume("0.20"),
    GlobalBeatDivisor("4"),
    EditorMode("preview"),
    ScrollMagnet("true"),
    ScrollReverse("false"),
    ScrollSpeed("1.0"),
    DisplayInfo("true"),
    DisplayPreview("true"),

    ;
    final String defVal;

    ZXNInfo( String defVal) {
        this.defVal = defVal;
    }

    public String getDefVal() {
        return defVal;
    }

}
