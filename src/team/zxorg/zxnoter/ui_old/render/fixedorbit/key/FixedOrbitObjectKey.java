package team.zxorg.zxnoter.ui_old.render.fixedorbit.key;

public enum FixedOrbitObjectKey {
    JUDGED_LINE("judged-line"),
    PREVIEW_JUDGED_LINE("preview-judged-line"),
    ORBIT("orbit"),
    ORBIT_LINE("orbit-line"),
    BEAT_LINE("beat-line"),
    PREVIEW_BEAT_LINE("preview-beat-line"),
    BOTTOM_LINE("bottom-line"),
    SUB_BEAT_LINE("sub-beat-line"),
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
