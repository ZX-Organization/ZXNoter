package team.zxorg.ui.javafx.editor;

import javafx.scene.Node;
import team.zxorg.ui.javafx.FunctionalComponent;
import team.zxorg.ui.javafx.ProjectView;

public class EditorArea extends FunctionalComponent {
    private final EditorSplitPane rootEditorSplitPane = new EditorSplitPane();

    public EditorArea(ProjectView projectView, FunctionalComponent parent) {
        super(projectView, parent, "editorArea");
    }

    @Override
    public Node getNode() {
        return null;
    }
}
