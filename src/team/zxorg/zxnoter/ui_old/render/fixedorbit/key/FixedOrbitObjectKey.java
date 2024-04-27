package team.zxorg.zxnoter.ui_old.render.fixedorbit.key;

public enum FixedOrbitObjectKey {
    JUDGED_LINE("judged-line"),
    PREVIEW_JUDGED_LINE("preview-judged-line"),
    ORBIT("orbit"),
    ORBIT_LINE("orbit-line"),
    BEAT_LINE("beat-line"),
    BEAT_LINE1("beat-line-1"),
    BEAT_LINE2("beat-line-2"),
    BEAT_LINE3("beat-line-3"),
    BEAT_LINE4("beat-line-4"),
    BEAT_LINE5("beat-line-5"),
    PREVIEW_BEAT_LINE("preview-beat-line"),
    BOTTOM_LINE("bottom-line"),
    SUB_BEAT_LINE("sub-beat-line"),
    SUB_BEAT_LINE_Y("sub-beat-line-1"),
    SUB_BEAT_LINE_R("sub-beat-line-2"),
    SUB_BEAT_LINE_P("sub-beat-line-3"),
    SUB_BEAT_LINE_B("sub-beat-line-4"),
    TOP_LINE("top-line"),
    TIMING_BASE("timing-base"),
    TIMING_BASE_DELETE("timing-base-delete"),
    TIMING("timing"),
    TIMING_DELETE("timing-delete"),
    RED_LINE("red-line"),
    LOGO("logo"),
    IKUN("ikun");

    FixedOrbitObjectKey(String name) {
        this.name = name;
    }

    public String name;
}
