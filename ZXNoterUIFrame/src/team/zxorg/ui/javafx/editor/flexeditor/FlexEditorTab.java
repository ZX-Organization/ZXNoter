package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.nio.file.Path;

/**
 * 灵活编辑器 选项卡
 */
public abstract class FlexEditorTab extends Tab {
    protected final FlexEditorArea editorArea;
    protected final Path path;

    public FlexEditorTab(FlexEditorArea editorArea, Path path) {
        this.editorArea = editorArea;
        this.path = path;

        //监听所属选项卡窗格 重新注册事件
        tabPaneProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.getTabDragPolicy());
        });
    }


    /**
     * 更新拖拽处理事件
     */
    public void updateDrag(Pane tabHead, Region title) {
        /*tabDragStyle.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                title.getStyleClass().remove(oldValue.name());
            }
            if (newValue != null) {
                title.getStyleClass().add(newValue.name());
            }
        });
*/

        tabHead.setOnDragDetected(event -> {

            /*editorArea.dragTab = this;
            editorArea.dragTabPane = (EditorTabPane) getTabPane();
            if (EditorArea.dragTabPane == null) {
                return;
            }*/

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
            close();
        });
    }


    /**
     * 关闭此选项卡
     */
    public void close() {

    }

}
