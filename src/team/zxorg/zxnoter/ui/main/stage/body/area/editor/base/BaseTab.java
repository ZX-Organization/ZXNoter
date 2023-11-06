package team.zxorg.zxnoter.ui.main.stage.body.area.editor.base;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXStatus;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public abstract class BaseTab extends Tab {
    public ArrayList<ZXStatus> zxStatuses = new ArrayList<>();
    private final ObjectProperty<TabDragStyle> tabDragStyle = new SimpleObjectProperty<>(null);
    public ZXIcon icon = new ZXIcon();
    protected EditorArea editorArea;
    protected BorderPane body = new BorderPane();

    @Override
    public String toString() {
        return "编辑器[" + getText() + "]";
    }

    public BaseTab(EditorArea editorArea) {
        UUID uuid = UUID.randomUUID();
        setId(uuid.toString());
        icon.setSize(18);
        setGraphic(icon);
        setContent(body);
        this.editorArea = editorArea;
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
        pane.setOnMousePressed(Event::consume);//顶掉原先的按下关闭
        pane.setOnMouseClicked(event -> {//改为点击触发关闭 处理关闭点击事件
            close();
        });
    }


    public void removeParentThis() {
        if (getTabPane() != null) {
            getTabPane().getTabs().remove(this);
        } else {
            System.err.println("BaseEditor:没删成功");
        }
    }

    /**
     * 关闭此编辑器
     */
    public boolean close() {
        if (!closeRequest()) {
            return false;
        }


        EditorTabPane editorTabPane = (EditorTabPane) getTabPane();
        if (editorTabPane == null)
            return false;
        int index = editorTabPane.getTabs().indexOf(this);
        if (editorTabPane.getTabs().remove(this)) {
            System.out.println("关闭: " + this);
            closed();//关闭完
            if (index - 1 > 0) {
                EditorArea.dragTab = (BaseTab) editorTabPane.getTabs().get(index - 1);
            }
            EditorArea.dragTabPane = editorTabPane;
            editorTabPane.handleDragDropped();
        }

        /*if (this instanceof BaseFileEditor fileEditor)
            zxStage.fileEditorMap.remove(fileEditor.fileItem.path);*/
        return true;
    }

    /**
     * 关闭后 (用于释放)
     */
    protected abstract void closed();


    /**
     * 关闭请求
     *
     * @return 是否可以关闭
     */
    protected abstract boolean closeRequest();


    private enum TabDragStyle {
        left, right
    }

}
