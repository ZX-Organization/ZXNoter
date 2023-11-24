package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import team.zxorg.zxncore.ZXLogger;

/**
 * 灵活编辑器 拆分窗格
 */
public class FlexEditorSplitPane extends SplitPane {
    /**
     * 编辑器区域 编辑器根
     */
    protected final FlexEditorArea editorArea;

    /**
     * 父编辑器拆分窗格
     */
    protected FlexEditorSplitPane parentEditorSplitPane;

    {
        getStyleClass().addAll("flex-editor");
    }

    public FlexEditorSplitPane(FlexEditorArea editorArea, FlexEditorSplitPane parentEditorSplitPane) {
        this.editorArea = editorArea;
        this.parentEditorSplitPane = parentEditorSplitPane;
        //监听项目列表变化
        getItems().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved() && c.getList().isEmpty()) {
                    ZXLogger.info("拆分窗格列表为空 删除自身");
                    if (parentEditorSplitPane != null) {
                        parentEditorSplitPane.getItems().remove(this);
                    }
                }
            }
        });
    }

    public FlexEditorTabPane createTabPane() {
        return createTabPane(getItems().size());
    }

    /**
     * 在指定索引创建一个选项卡窗格
     *
     * @param index 索引
     * @return 新的选项卡窗格
     */
    public FlexEditorTabPane createTabPane(int index) {
        FlexEditorTabPane tabPane = new FlexEditorTabPane(this);
        getItems().add(index, tabPane);
        return tabPane;
    }

    public FlexEditorSplitPane createSplitPane() {
        return createSplitPane(getItems().size());
    }

    /**
     * 在指定索引创建一个拆分窗格
     *
     * @param index 索引
     * @return 新的拆分窗格
     */
    public FlexEditorSplitPane createSplitPane(int index) {
        FlexEditorSplitPane newSplitPane = new FlexEditorSplitPane(editorArea, this);
        newSplitPane.setOrientation((getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL));
        getItems().add(index, newSplitPane);
        return newSplitPane;
    }


    /**
     * 关闭此拆分窗格
     */
    public void close() {
        //移除所有子物品
        for (Node item : getItems()) {
            if (item instanceof FlexEditorSplitPane flexEditorSplitPane) {
                //可能有子拆分窗格

            } else if (item instanceof FlexEditorTabPane flexEditorTabPane) {
                //可能有子选项卡窗格

            }
        }
    }


}
