package team.zxorg.zxnoter.ui.main.one.two.three.four;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.BaseEditor;

public class EditorLayout extends SplitPane {
    public EditorLayout() {
        getStyleClass().add("editor-layout");
        getItems().addListener((ListChangeListener<Node>) c -> {
            int size = getItems().size();
            if (size == 0) return;
            double average = 1. / size;
            for (int i = 0; i < size; i++) {
                setDividerPosition(i, average * (i + 1));
            }


        });

    }
}
