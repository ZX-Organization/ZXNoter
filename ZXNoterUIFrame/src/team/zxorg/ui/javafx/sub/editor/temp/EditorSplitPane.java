package team.zxorg.ui.javafx.sub.editor.temp;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import team.zxorg.ui.javafx.sub.FunctionalComponent;

public class EditorSplitPane extends FunctionalComponent {
    SplitPane splitPane = new SplitPane();
    EditorArea editorArea;

    public EditorSplitPane(EditorArea editorArea) {
        super(null, null, "editorLayout");
        this.editorArea = editorArea;
        //监听所有物品
        splitPane.getItems().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {

            }
        });
    }

    @Override
    public Node getNode() {
        return splitPane;
    }
}
