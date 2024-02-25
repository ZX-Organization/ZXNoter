package team.zxorg.zxnoter.ui.behavior;

public interface Behavior {
    /**
     * 行动
     */
    void action();

    /**
     * 撤销行动
     */
    default void revoke() {

    }
}
