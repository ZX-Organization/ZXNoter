package team.zxorg.zxnoter.ui.main.one.two.three.four.five;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.one.two.three.EditorArea;
import team.zxorg.zxnoter.ui.main.one.two.three.four.EditorTabPane;

import java.util.UUID;

public abstract class BaseEditor extends Tab {
    private final UUID uuid = UUID.randomUUID();
    private final EditorArea rootArea;

    @Override
    public String toString() {
        return "编辑器[" + getText() + "]";
    }

    public BaseEditor(EditorArea rootArea) {
        this.rootArea = rootArea;
        setId(uuid.toString());
        Pane icon = ZXResources.getIconPane("media.closed-captioning", 22, ZXColor.red);
        setGraphic(icon);
    }


    /**
     * 更新处理事件 (fx特有的查找困难)
     */
    public void updateDrag(Pane tabHead, Region title) {
        //tabHead = (Region) getTabPane().lookup("#" + uuid);
        //System.out.println("aaa>" + tabHead);


        //title = (Label) getTabPane().lookup("#" + uuid + " > .tab-container > .tab-label");
        //System.out.println("更新布局Tab");

        tabHead.setOnDragDetected(event -> {

            EditorArea.dragTab = this;
            EditorArea.dragTabPane = (EditorTabPane) getTabPane();
            if (EditorArea.dragTabPane == null) {
                return;
            }

            // 创建快照
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT); // 快照背景透明
            Image snapshot = tabHead.snapshot(params, null);
            //getTabPane().getTabs().remove(this);
            // 开始拖拽操作
            Dragboard dragboard = tabHead.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(snapshot); // 将快照作为拖拽的内容
            dragboard.setContent(content);
            event.consume();
        });

        tabHead.setOnDragOver((event) -> {
            event.consume();
            if (EditorArea.dragTab != null) {
                if (EditorArea.dragTab.equals(this))
                    return;
                event.acceptTransferModes(TransferMode.MOVE);

                title.getStyleClass().remove("left");
                title.getStyleClass().remove("right");
                if (event.getX() < tabHead.getWidth() / 2) {
                    title.getStyleClass().add("left");
                } else {
                    title.getStyleClass().add("right");
                }
            }
        });

        tabHead.setOnDragEntered(event -> {

        });

        tabHead.setOnDragExited((event) -> {
            if (EditorArea.dragTab != null) {
                title.getStyleClass().remove("left");
                title.getStyleClass().remove("right");
            }
            event.consume();
        });

        tabHead.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            if (EditorArea.dragTab != null) {
                title.getStyleClass().remove("left");
                title.getStyleClass().remove("right");

                EditorTabPane tabPane = (EditorTabPane) getTabPane();
                ObservableList<Tab> tabs = tabPane.getTabs();

                EditorArea.dragTabPane.getTabs().remove(EditorArea.dragTab);

                tabs.add(tabs.indexOf(this) + (event.getX() < tabHead.getWidth() / 2 ? 0 : 1), EditorArea.dragTab);
                tabPane.getSelectionModel().select(EditorArea.dragTab);
                tabPane.requestFocus();

                //检查清除拖拽Tab之前的TabPane
                if (EditorArea.dragTabPane.getTabs().size() == 0) {
                    EditorArea.dragTabPane.removeParentThis();
                }
                EditorArea.dragTabPane.parentLayout.checkItems();


                event.setDropCompleted(true);
                EditorArea.dragTab = null;
            }
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
        pane.setOnMousePressed(event -> {
        });//顶掉原先的按下关闭
        pane.setOnMouseClicked(event -> getTabPane().getTabs().remove(this));//改为点击触发关闭
    }

    public void removeParentThis() {
        if (getTabPane() != null) {
            getTabPane().getTabs().remove(this);
        } else {
            System.err.println("BaseEditor:没删成功");
        }
    }
}
