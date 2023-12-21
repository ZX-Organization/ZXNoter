package team.zxorg.ui.javafx.sub.editor.flexeditor;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import team.zxorg.zxncore.ZXLogger;


/**
 * 灵活编辑器 选项卡窗格
 */
public class FlexTabPane extends TabPane {

    /**
     * 父编辑器分隔面板
     */
    protected final ObjectProperty<FlexSplitPane> parentEditorSplitPane = new SimpleObjectProperty<>();

    private final FlexSkinProcessor skinProcessor = new FlexSkinProcessor();

    public FlexTabPane() {

        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

        //监听选项卡列表变化
        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved() && c.getList().isEmpty()) {
                    ZXLogger.info("选项卡窗格列表为空 删除自身");
                    if (getParent() instanceof FlexSplitPane editorSplitPane) {
                        editorSplitPane.getItems().remove(this);
                    }
                }
                if (c.wasAdded()) {
                    for (Tab tab : c.getAddedSubList()) {
                        if (tab instanceof FlexTab flexTab) {
                            System.out.println("新加入选项卡 " + tab.getId());
                            Platform.runLater(() -> {
                                System.out.println("更新选项卡 " + tab.getId());
                                Pane tabHeaderArea = (Pane) lookup("#" + tab.getId());
                                //FlexSkin.updateDrag(flexTab, tabHeaderArea, null);
                            });
                        } else {
                            throw new RuntimeException("只支持 FlexEditorTab");
                        }
                    }
                }
            }
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
            if (newValue.booleanValue())
                parentEditorSplitPane.get().getEditorArea().focusEditorTabPane.set(this);
        });


        getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Node node : c.getAddedSubList()) {
                        handleNodeChange(node);
                    }
                }
            }
        });

    }

    private void handleNodeChange(Node node) {
        switch (node.getClass().getSimpleName()) {
            case "TabHeaderArea" -> handleTabHeaderArea(node);
            case "TabContentRegion" -> skinProcessor.updateTabContentRegion(node);
            default -> System.out.println("Unknown Node Type: " + node.getClass().getSimpleName());
        }
    }

    private void handleTabHeaderArea(Node node) {
        if (lookup(".headers-region") instanceof Pane headersRegion) {
            ZXLogger.info("初始化和更新 TabHeaderSkin");
            headersRegion.getChildren().forEach(skinProcessor::updateTabHeaderSkin);
            headersRegion.getChildren().addListener((ListChangeListener<Node>) c1 -> {
                while (c1.next())
                    if (c1.wasAdded())
                        c1.getAddedSubList().forEach(skinProcessor::updateTabHeaderSkin);

            });
        }
    }



    public FlexArea getArea() {
        return parentEditorSplitPane.get().getEditorArea();
    }

    /**
     * 添加选项卡到末尾
     *
     * @param tab 选项卡
     */
    public void addTab(FlexTab tab) {
        addTab(getTabs().size(), tab);
    }

    /**
     * 添加选项卡
     *
     * @param index 索引
     * @param tab   选项卡
     */
    public void addTab(int index, FlexTab tab) {
        if (tab.getTabPane() instanceof FlexTabPane parent) {
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
    public void removeTab(FlexTab tab) {
        getTabs().remove(tab);
    }


}
