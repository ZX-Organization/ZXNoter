package team.zxorg.fxcl.component.flexview;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Collections;


/**
 * 灵活编辑器 选项卡窗格
 */
public class FlexTabPane extends TabPane {
    /**
     * 父编辑器分隔面板
     */
    protected final ObjectProperty<FlexSplitPane> parentFlexSplitPane = new SimpleObjectProperty<>();
    /**
     * 根区域
     */
    private final ObjectProperty<FlexArea> flexArea = new SimpleObjectProperty<>();
    private final FlexSkinProcessor skinProcessor = new FlexSkinProcessor(this);

    public FlexTabPane() {

        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

        //监听选项卡列表变化
        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved()) {
                    Platform.runLater(() -> {
                        if (c.getList().isEmpty()) {
                            //Logger.info("选项卡窗格列表为空 删除自身");
                            parentFlexSplitPane.get().getItems().remove(this);
                        }
                    });
                } else if (c.wasAdded()) {
                    for (Tab tab : c.getAddedSubList()) {
                        //解除旧的标签页面板
                        if (tab.getUserData() instanceof FlexTabPane previousParent) {
                            if (previousParent == this)
                                continue;
                            previousParent.getTabs().remove(tab);
                        }
                        //设置新的
                        tab.setUserData(this);
                    }
                }
            }
        });

        //绑定根
        parentFlexSplitPane.addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                flexArea.bind(newValue.flexAreaProperty());
            else
                flexArea.unbind();
        });
        //setTabDragPolicy(TabDragPolicy.REORDER);
        /*Platform.runLater(() -> {
            System.out.println(lookupAll(".tab-header-area"));
        });*/
        //监听父
        /*parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof FlexEditorSplitPane flexEditorSplitPane)
                parentEditorSplitPane.set(flexEditorSplitPane);
        });
*/
        /**
         * 焦点处理
         */
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                parentFlexSplitPane.get().getFlexArea().focusEditorTabPane.set(this);
        });

        /**
         * 为监听和处理拖拽
         */
        getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Node node : c.getAddedSubList()) {
                        skinProcessor.handleNodeChange(node);
                    }
                }
            }
        });

    }

    public ObjectProperty<FlexArea> flexAreaProperty() {
        return flexArea;
    }

    public FlexArea getArea() {
        return flexArea.get();
    }

    /**
     * 添加选项卡到末尾
     *
     * @param tab 选项卡
     */
    public void addTab(Tab tab) {

        addTab(getTabs().size(), tab);
    }

    /**
     * 添加选项卡
     *
     * @param index 索引
     * @param tab   选项卡
     */
    public void addTab(int index, Tab tab) {

        ObservableList<Tab> tabs = getTabs();
        if (tabs.contains(tab)) {
            int currentIndex = tabs.indexOf(tab);
            int lastIndex = tabs.size() - 1;
            // 交换两个Tab的位置
            Collections.swap(tabs, currentIndex, lastIndex);
        }else {
            if (tab.getTabPane() instanceof TabPane parent) {
                if (parent != this)
                    //如果已有父选项卡窗格 需要脱离
                    parent.getTabs().remove(tab);
            }
            getTabs().add(index, tab);
        }


    }

    /**
     * 移除选项卡
     *
     * @param tab 选项卡
     */
    public void removeTab(Tab tab) {
        getTabs().remove(tab);
    }

    public FlexSplitPane getParentSplitPane() {
        return parentFlexSplitPane.get();
    }

}
