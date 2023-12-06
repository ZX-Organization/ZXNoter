package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.skin.TabPaneSkin;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import team.zxorg.zxncore.ZXLogger;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 灵活编辑器 选项卡窗格
 */
public class FlexEditorTabPane extends TabPane {
    private static final DataFormat editorTabDataFormat = new DataFormat("application/x-editor-tab");
    private static FlexEditorTab dragTab;
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
                if (c.wasAdded()) {
                    for (Tab tab : c.getAddedSubList()) {
                        if (tab instanceof FlexEditorTab flexEditorTab) {
                            System.out.println("新加入选项卡 " + tab.getId());
                            Platform.runLater(() -> {
                                System.out.println("更新选项卡 " + tab.getId());
                                Pane tabHeaderArea = (Pane) lookup("#" + tab.getId());
                                updateDrag(flexEditorTab, tabHeaderArea, null);
                            });
                        } else {
                            throw new RuntimeException("只支持 FlexEditorTab");
                        }
                    }
                }
            }
        });
        //setTabDragPolicy(TabDragPolicy.REORDER);
        Platform.runLater(() -> {
            System.out.println(lookupAll(".tab-header-area"));
        });
    }


    /**
     * 更新拖拽处理事件
     */
    public void updateDrag(FlexEditorTab tab, Pane tabHead, Region title) {
        /*tabDragStyle.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                title.getStyleClass().remove(oldValue.name());
            }
            if (newValue != null) {
                title.getStyleClass().add(newValue.name());
            }
        });
*/
        //设置开始拖动
        tabHead.setOnDragDetected(event -> {

            /*editorArea.dragTab = this;
            editorArea.dragTabPane = (EditorTabPane) getTabPane();
            if (EditorArea.dragTabPane == null) {
                return;
            }*/
            dragTab = tab;

            // 创建快照
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT); // 快照背景透明
            Image snapshot = tabHead.snapshot(params, null);

            // 开始拖拽操作
            Dragboard dragboard = tabHead.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();

            // 将自定义数据格式作为拖拽的内容
            content.put(editorTabDataFormat, "");
            dragboard.setContent(content);

            // 设置拖拽时的图标
            dragboard.setDragView(snapshot);

            event.consume();
        });

        tabHead.setOnDragOver((event) -> {
            event.consume();
           /* if (EditorArea.dragTab != null) {
                if (EditorArea.dragTab.equals(this))
                    return;
                event.acceptTransferModes(TransferMode.MOVE);

                tabDragStyle.set((event.getX() < tabHead.getWidth() / 2 ? TabDragStyle.left : TabDragStyle.right));
            }*/
        });

        tabHead.setOnDragEntered(event -> {

        });

        tabHead.setOnDragExited((event) -> {
           /* if (EditorArea.dragTab != null) {
                tabDragStyle.setValue(null);
            }*/
            event.consume();
        });

        tabHead.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            /*if (EditorArea.dragTab != null) {
                tabDragStyle.setValue(null);

                EditorTabPane tabPane = (EditorTabPane) getTabPane();
                ObservableList<Tab> tabs = tabPane.getTabs();

                EditorArea.dragTab.removeParentThis();

                tabs.add(tabs.indexOf(this) + (event.getX() < tabHead.getWidth() / 2 ? 0 : 1), EditorArea.dragTab);
                tabPane.handleDragDropped();

                event.setDropCompleted(true);
            }*/
            event.consume();

        });


        for (Node node : tabHead.getChildren()) {
            if (node instanceof Pane pane) {
                handleTabContainer(pane);
            }
        }

    }

    private void handleTabContainer(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Pane subPane) {
                if (subPane.getStyleClass().contains("tab-close-button")) {
                    handleTabCloseButton(subPane);
                }
            }
        }
    }


    private void handleTabCloseButton(Pane pane) {
        pane.setOnMousePressed(Event::consume);//顶掉原先的按下关闭
        pane.setOnMouseClicked(event -> {//改为点击触发关闭 处理关闭点击事件
            //close();
        });
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
