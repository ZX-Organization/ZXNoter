package team.zxorg.ui.main.stage.area;

import javafx.geometry.Orientation;

public enum LayoutPosition {
    LEFT("left"), RIGHT("right"), TOP("top"), BOTTOM("bottom"), CENTER("center");

    LayoutPosition(String id) {
        this.id = id;
    }

    private final String id;

    public String getId() {
        return this.id;
    }

    /**
     * 获取方向
     */
    public Orientation getOrientation() {
        if (this == LEFT || this == RIGHT) {
            return Orientation.HORIZONTAL;//水平
        }
        if (this == TOP || this == BOTTOM) {
            return Orientation.VERTICAL;//垂直
        }
        return null;
    }

    /**
     * 是否优先
     */
    public boolean isPriority() {
        return (this == LEFT || this == TOP);
    }
}
