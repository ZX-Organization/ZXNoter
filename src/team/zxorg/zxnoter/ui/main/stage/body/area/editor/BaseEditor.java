package team.zxorg.zxnoter.ui.main.stage.body.area.editor;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;

import java.util.UUID;

public abstract class BaseEditor extends Tab {
    private final ObjectProperty<TabDragStyle> tabDragStyle = new SimpleObjectProperty<>(null);
    public ZXIcon icon = new ZXIcon();

    @Override
    public String toString() {
        return "编辑器[" + getText() + "]";
    }

    public BaseEditor() {
        UUID uuid = UUID.randomUUID();
        setId(uuid.toString());
        icon.setSize(18);
        setGraphic(icon);
    }


    /**
     * 更新拖拽处理事件
     */
    public void updateDrag(Pane tabHead, Region title) {
        tabDragStyle.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                title.getStyleClass().remove(oldValue.name());
            }
            if (newValue != null) {
                title.getStyleClass().add(newValue.name());
            }
        });


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

                tabDragStyle.set((event.getX() < tabHead.getWidth() / 2 ? TabDragStyle.left : TabDragStyle.right));
            }
        });

        tabHead.setOnDragEntered(event -> {

        });

        tabHead.setOnDragExited((event) -> {
            if (EditorArea.dragTab != null) {
                tabDragStyle.setValue(null);
            }
            event.consume();
        });

        tabHead.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            if (EditorArea.dragTab != null) {
                tabDragStyle.setValue(null);

                EditorTabPane tabPane = (EditorTabPane) getTabPane();
                ObservableList<Tab> tabs = tabPane.getTabs();

                EditorArea.dragTab.removeParentThis();

                tabs.add(tabs.indexOf(this) + (event.getX() < tabHead.getWidth() / 2 ? 0 : 1), EditorArea.dragTab);
                tabPane.handleDragDropped();

                event.setDropCompleted(true);
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
        pane.setOnMouseClicked(event -> {
            boolean close = true;
            if (getOnCloseRequest() != null) {
                getOnCloseRequest().handle(event);
                close = !event.isConsumed();
            }


            if (close) {
                EditorTabPane editorTabPane = (EditorTabPane) getTabPane();
                int index = editorTabPane.getTabs().indexOf(this);
                if (editorTabPane.getTabs().remove(this)) {
                    System.out.println("关闭: " + this);
                    if (index - 1 > 0) {
                        EditorArea.dragTab = (BaseEditor) editorTabPane.getTabs().get(index - 1);
                    }
                    EditorArea.dragTabPane = editorTabPane;
                    editorTabPane.handleDragDropped();
                }
            }

        });//改为点击触发关闭
    }

    public void removeParentThis() {
        if (getTabPane() != null) {
            getTabPane().getTabs().remove(this);
        } else {
            System.err.println("BaseEditor:没删成功");
        }
    }

    private enum TabDragStyle {
        left, right
    }
}
