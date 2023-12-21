package team.zxorg.ui.javafx.sub.editor.flexeditor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import team.zxorg.zxncore.ZXLogger;

/**
 * 灵活编辑器 拆分窗格
 */
public class FlexSplitPane extends SplitPane {
    /**
     * 编辑器区域 编辑器根
     */
    private final ObjectProperty<FlexArea> editorArea = new SimpleObjectProperty<>();

    /**
     * 父编辑器拆分窗格
     */
    private final ObjectProperty<FlexSplitPane> parentEditorSplitPane = new SimpleObjectProperty<>();

    {
        getStyleClass().addAll("flex-editor");
    }

    public FlexSplitPane() {
        /*parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof FlexEditorSplitPane editorSplitPane) {

            }
            System.out.println(newValue);
        });*/

        //监听项目列表变化
        getItems().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved() && c.getList().isEmpty()) {
                    ZXLogger.info("拆分窗格列表为空 删除自身");

                    if (getParent() instanceof FlexSplitPane editorSplitPane) {
                        editorSplitPane.getItems().remove(this);
                    }
                }
            }
        });
    }

    public ReadOnlyObjectProperty<FlexArea> editorAreaProperty() {
        return editorArea;
    }

    public ReadOnlyObjectProperty<FlexSplitPane> parentEditorSplitPaneProperty() {
        return parentEditorSplitPane;
    }

    public FlexArea getEditorArea() {
        return editorArea.get();
    }

    protected void setEditorArea(FlexArea editorArea) {
        this.editorArea.set(editorArea);
    }

    public FlexSplitPane getParentEditorSplitPane() {
        return parentEditorSplitPane.get();
    }

    public FlexTabPane createTabPane() {
        return createTabPane(getItems().size());
    }

    /**
     * 在指定索引创建一个选项卡窗格
     *
     * @param index 索引
     * @return 新的选项卡窗格
     */
    public FlexTabPane createTabPane(int index) {
        FlexTabPane tabPane = new FlexTabPane();
        tabPane.parentEditorSplitPane.set(this);
        getItems().add(index, tabPane);
        return tabPane;
    }

    public FlexSplitPane createSplitPane() {
        return createSplitPane(getItems().size());
    }

    /**
     * 在指定索引创建一个拆分窗格
     *
     * @param index 索引
     * @return 新的拆分窗格
     */
    public FlexSplitPane createSplitPane(int index) {
        FlexSplitPane newSplitPane = new FlexSplitPane();
        newSplitPane.parentEditorSplitPane.set(this);
        newSplitPane.editorArea.bind(editorAreaProperty());
        newSplitPane.setOrientation((getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL));
        getItems().add(index, newSplitPane);
        return newSplitPane;
    }

    public void removeSplitPane(int index) {
        ObservableList<Node> items = getItems();
        if (items.get(index) instanceof FlexSplitPane flexSplitPane) {
            flexSplitPane.parentEditorSplitPane.set(null);
            flexSplitPane.editorArea.set(null);
        }
        items.remove(index);
    }


    /**
     * 关闭此拆分窗格
     */
    public void close() {
        //移除所有子物品
        for (Node item : getItems()) {
            if (item instanceof FlexSplitPane flexSplitPane) {
                //可能有子拆分窗格

            } else if (item instanceof FlexTabPane flexEditorTabPane) {
                //可能有子选项卡窗格

            }
        }
    }


}
