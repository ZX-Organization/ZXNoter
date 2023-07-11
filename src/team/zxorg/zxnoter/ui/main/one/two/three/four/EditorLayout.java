package team.zxorg.zxnoter.ui.main.one.two.three.four;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import team.zxorg.zxnoter.ZXLogger;

import java.util.UUID;

public class EditorLayout extends SplitPane {
    protected EditorLayout parentLayout;//如果是null则为根(root)
    private UUID uuid = UUID.randomUUID();


    public String getName() {
        return uuid.toString().substring(19);
    }

    @Override
    public String toString() {
        return (getOrientation() == Orientation.HORIZONTAL ? "(水平)" : "(垂直)") + "布局 父布局{" + (parentLayout == null ? null : parentLayout.getName()) + "} 此{" + getName() + "}";
    }

    public static void printLayout(EditorLayout layout, String indent) {
        System.out.println(indent + layout.toString());

        for (Node item : layout.getItems()) {
            if (item instanceof EditorLayout nestedLayout) {
                printLayout(nestedLayout, indent + "\t");
            } else {
                System.out.println(indent + "\t" + item.toString());
            }
        }
    }

    public EditorLayout(EditorLayout parentLayout) {
        this.parentLayout = parentLayout;
        getStyleClass().add("editor-layout");


    }

    public void checkItems() {
        int size = getItems().size();
        if (size == 0) {
            ZXLogger.info("检查到布局{" + getName() + "}没有物品，直接移除");
            parentLayout.getChildren().remove(this);
        } else if (size == 1) {
            if (parentLayout != null) {//子物品去除布局
                ZXLogger.info("检查到布局{" + getName() + "}只剩1个物品");
                int index = parentLayout.getItems().indexOf(this);
                for (Node child : getItems()) {
                    if (child instanceof EditorTabPane editorTabPane) {
                        editorTabPane.parentLayout = parentLayout;
                    } else if (child instanceof EditorLayout editorLayout) {
                        editorLayout.parentLayout = parentLayout;
                    }
                }

                parentLayout.getItems().addAll(Math.max(index, 0), getItems());//交给父布局
                parentLayout.getItems().remove(this);//删除自身
            }
        }

    }

    public void autoLayout() {
        int size = getItems().size();
        double average = 1.0 / size;
        //ZXLogger.logger.info("自动计算:" + size + " 平均:" + average + " 来自{" + getName() + "}");
        for (int i = 0; i < size; i++) {
            setDividerPosition(i, average * (i + 1));
        }
    }
}
