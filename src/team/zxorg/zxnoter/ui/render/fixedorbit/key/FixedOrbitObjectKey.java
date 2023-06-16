package team.zxorg.zxnoter.ui.render.fixedorbit.key;

public enum FixedOrbitObjectKey {
    JUDGED_LINE("judged-line"),
    ORBIT("orbit"),
    BEAT_LINE("beat-line"),
    BOTTOM_LINE("bottom-line"),
    SUB_BEAT_LINE("sub-beat-line"),
    TOP_LINE("top-line");

    FixedOrbitObjectKey(String name) {
        this.name = name;
    }

    public String name;
}
