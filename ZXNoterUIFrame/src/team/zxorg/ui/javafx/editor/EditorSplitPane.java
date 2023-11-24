package team.zxorg.ui.javafx.editor;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import team.zxorg.ui.javafx.FunctionalComponent;

public class EditorSplitPane extends FunctionalComponent {
    SplitPane splitPane = new SplitPane();

    public EditorSplitPane() {
        super(null, null, "editorLayout");
    }

    @Override
    public Node getNode() {
        return splitPane;
    }
}
