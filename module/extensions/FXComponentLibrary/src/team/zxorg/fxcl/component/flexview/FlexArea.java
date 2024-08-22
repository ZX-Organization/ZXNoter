package team.zxorg.fxcl.component.flexview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;

public class FlexArea extends FlexSplitPane {

    /**
     * 焦点编辑器标签页面板
     */
    protected final ObjectProperty<FlexTabPane> focusEditorTabPane = new SimpleObjectProperty<>();
    /**
     * 拖动中的选项卡
     */
    protected Tab draggingTab;

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

    public void addTab(Tab tab) {
        FlexTabPane tabPane = focusEditorTabPane.get();
        if (tabPane == null) {
            tabPane = createTabPane();
            tabPane.requestFocus();
        }
        tabPane.addTab(tab);
        tabPane.getSelectionModel().select(tab);

    }


}
