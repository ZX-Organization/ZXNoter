package team.zxorg.zxnoter.resource;

public enum ZXColor {
    blue("blue"),
    red("red"),
    purple("purple"),
    yellow("yellow"),
    green("green"),
    orange("orange"),
    pink("pink"),
    gray("gray"),
    white("white"),
    font("font-light");
    private final String id;

    ZXColor(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "color-" + id;
    }
}
