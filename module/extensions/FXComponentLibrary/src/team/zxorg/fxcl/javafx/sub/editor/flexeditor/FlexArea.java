package team.zxorg.fxcl.javafx.sub.editor.flexeditor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;

public class FlexArea extends FlexSplitPane {

    /**
     * 拖动中的选项卡
     */
    protected Tab draggingTab;
    /**
     * 焦点编辑器标签页面板
     */
    protected final ObjectProperty<FlexTabPane> focusEditorTabPane = new SimpleObjectProperty<>();

    public FlexArea() {
        super();
        setFlexArea(this);
        focusEditorTabPane.addListener((observable, oldValue, newValue) -> {
            if (oldValue instanceof FlexTabPane tabPane)
                tabPane.getStyleClass().remove("focused");
            if (newValue instanceof FlexTabPane tabPane)
                tabPane.getStyleClass().add("focused");
        });
    }


}
