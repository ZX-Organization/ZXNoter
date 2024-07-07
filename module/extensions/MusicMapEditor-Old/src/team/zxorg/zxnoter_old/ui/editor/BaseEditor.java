package team.zxorg.zxnoter_old.ui.editor;

import javafx.scene.layout.VBox;

public abstract class BaseEditor extends VBox {
    public BaseEditor() {
        this.getStyleClass().add("editor");
    }

    /**
     * 屏幕更新
     */
    public abstract void render();

    /**
     * 关闭事件
     * @return 是否可以关闭
     */
    public abstract boolean close();
}
