package team.zxorg.ui.javafx.sub.editor.temp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import team.zxorg.ui.javafx.ProjectView;
import team.zxorg.ui.javafx.sub.FunctionalComponent;

public class EditorArea extends FunctionalComponent {
    /**
     * 根编辑器分隔面板
     */
    private final EditorSplitPane rootEditorSplitPane = new EditorSplitPane(this);
    /**
     * 焦点编辑器标签页面板
     */
    private final ObjectProperty<EditorTabPane> focusEditorTabPane = new SimpleObjectProperty<>();

    public EditorArea(ProjectView projectView, FunctionalComponent parent) {
        super(projectView, parent, "editorArea");
    }

    @Override
    public Node getNode() {
        return null;
    }
}
