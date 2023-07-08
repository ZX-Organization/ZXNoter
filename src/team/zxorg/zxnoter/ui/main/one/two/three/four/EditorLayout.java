package team.zxorg.zxnoter.ui.main.one.two.three.four;

import com.alibaba.fastjson2.JSONObject;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ui.main.one.two.three.EditorArea;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.BaseEditor;

import java.util.UUID;

public class EditorLayout extends SplitPane {
    protected EditorLayout parentLayout;//如果是null则为根(root)

    @Override
    public String toString() {
        return "编辑器布局(" + (getOrientation() == Orientation.HORIZONTAL ? "水平)" : "垂直) ") + getItems() + " ";
    }

    public EditorLayout(EditorLayout parentLayout) {
        this.parentLayout = parentLayout;
        getStyleClass().add("editor-layout");
        getItems().addListener((ListChangeListener<Node>) c -> {
            int size = getItems().size();
            if (size == 0) {
                parentLayout.getChildren().remove(this);
                return;
            } else if (size == 1) {
                if (parentLayout != null) {//子物品去除布局
                    int index = parentLayout.getItems().indexOf(this);

                    for (Node child : getItems()) {
                        if (child instanceof EditorTabPane editorTabPane) {
                            editorTabPane.parentLayout = parentLayout;
                        }
                    }

                    parentLayout.getItems().addAll(Math.max(index, 0), getItems());
                    parentLayout.getItems().remove(this);

                    System.out.println("优化后:" + parentLayout);
                    return;
                }
            }
            double average = 1. / size;
            for (int i = 0; i < size; i++) {
                setDividerPosition(i, average * (i + 1));
            }
        });

    }

}
