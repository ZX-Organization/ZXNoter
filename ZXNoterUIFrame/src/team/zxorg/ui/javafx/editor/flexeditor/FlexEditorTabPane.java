package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import team.zxorg.zxncore.ZXLogger;

import java.nio.file.Path;

/**
 * 灵活编辑器 选项卡窗格
 */
public class FlexEditorTabPane extends TabPane {
    private FlexEditorSplitPane parent;

    public FlexEditorTabPane(FlexEditorSplitPane parent) {
        this.parent = parent;
        //监听选项卡列表变化
        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved() && c.getList().isEmpty()) {
                    ZXLogger.info("选项卡窗格列表为空 删除自身");
                    if (parent != null) {
                        parent.getItems().remove(this);
                    }
                }
            }
        });
        setTabDragPolicy(TabDragPolicy.REORDER);
    }

    /**
     * 添加选项卡到末尾
     *
     * @param tab 选项卡
     */
    public void addTab(FlexEditorTab tab) {
        addTab(getTabs().size(), tab);
    }

    /**
     * 添加选项卡
     *
     * @param index 索引
     * @param tab   选项卡
     */
    public void addTab(int index, FlexEditorTab tab) {
        if (tab.getTabPane() instanceof FlexEditorTabPane parent) {
            //如果已有父选项卡窗格 需要脱离
            parent.getTabs().remove(tab);
        }
        getTabs().add(index, tab);
    }

    /**
     * 移除选项卡
     *
     * @param tab 选项卡
     */
    public void removeTab(FlexEditorTab tab) {
        getTabs().remove(tab);
    }

}
