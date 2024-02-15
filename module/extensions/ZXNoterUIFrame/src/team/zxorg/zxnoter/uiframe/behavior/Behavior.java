package team.zxorg.zxnoter.uiframe.behavior;

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
