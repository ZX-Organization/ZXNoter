package team.zxorg.zxnoter.resource;

public enum ZXColor {
    BLUE("blue"),
    RED("red"),
    PURPLE("purple"),
    YELLOW("yellow"),
    GREEN("green"),
    ORANGE("orange"),
    PINK("pink"),
    GRAY("gray"),
    WHITE("white"),
    FONT_USUALLY("font-usually"),
    FONT_LIGHT("font-light");
    private final String id;

    ZXColor(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "color-" + id;
    }
}
