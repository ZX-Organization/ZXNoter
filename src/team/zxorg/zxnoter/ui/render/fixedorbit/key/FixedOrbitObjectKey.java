package team.zxorg.zxnoter.ui.render.fixedorbit.key;

public enum FixedOrbitObjectKey {
    JUDGED_LINE("judged-line"),
    ORBIT("orbit"),
    BEAT_LINE("beat-line"),
    BOTTOM_LINE("bottom-line"),
    SUB_BEAT_LINE("sub-beat-line"),
    TOP_LINE("top-line"),
    TIMING_LINE("timing-line"),
    RED_LINE("red-line"),
    IKUN("ikun");

    FixedOrbitObjectKey(String name) {
        this.name = name;
    }

    public String name;
}
