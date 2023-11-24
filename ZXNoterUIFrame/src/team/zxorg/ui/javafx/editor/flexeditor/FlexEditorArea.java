package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class FlexEditorArea extends FlexEditorSplitPane {
    /**
     * 拖拽的选项卡
     */
    protected FlexEditorTab draggingTab = null;
    /**
     * 被拖拽的选项卡窗格
     */
    protected FlexEditorTabPane draggingTabPane = null;

    public FlexEditorArea() {
        super(null, null);


    }



}
