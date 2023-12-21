package team.zxorg.ui.javafx.sub.editor.flexeditor;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlexSkinProcessor {
    /**
     * 编辑器标签页数据格式 (用于处理拖拽判断)
     */
    private static final DataFormat EDITOR_TAB_DATA_FORMAT = new DataFormat("application/x-editor-tab");

    /**
     * 通过反射强行获取FlexTab
     *
     * @param node 只有部分有这玩意
     * @return FlexTab
     */
    private static FlexTab getTab(Node node) {
        try {
            Method skinField = node.getClass().getMethod("getTab");
            skinField.setAccessible(true);
            if (skinField.invoke(node) instanceof FlexTab tab) {
                return tab;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 更新拖拽处理事件    TabPaneSkin$TabHeaderArea
     */
    public void updateTabHeaderArea(Node tabHeaderArea) {

    }

    /**
     * TabPaneSkin$TabHeaderSkin
     *
     */
    public void updateTabHeaderSkin(Node tabHeaderSkin) {
        FlexTab tab = getTab(tabHeaderSkin);
        //设置开始拖动
        tabHeaderSkin.setOnDragDetected(event -> {
            FlexArea area = tab.getArea();
            area.draggingTab = tab;
            //draggingTab = tab;
            /*editorArea.dragTab = this;
            editorArea.dragTabPane = (EditorTabPane) getTabPane();
            if (EditorArea.dragTabPane == null) {
                return;
            }*/
            //dragTab = tab;

            // 创建快照
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT); // 快照背景透明
            Image snapshot = tabHeaderSkin.snapshot(params, null);

            // 开始拖拽操作
            Dragboard dragboard = tabHeaderSkin.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            // 将自定义数据格式作为拖拽的内容
            content.put(EDITOR_TAB_DATA_FORMAT, "");
            dragboard.setContent(content);

            // 设置拖拽时的图标
            dragboard.setDragView(snapshot);

            event.consume();
        });

        tabHeaderSkin.setOnDragOver((event) -> {
            FlexArea area = tab.getArea();
            if (area.draggingTab != null) {
                if (area.draggingTab.equals(tab))
                    return;
                event.acceptTransferModes(TransferMode.MOVE);
                //tabDragStyle.set((event.getX() < tabHead.getWidth() / 2 ? TabDragStyle.left : TabDragStyle.right));
            }
            event.consume();
        });
        //拖动进入事件
        tabHeaderSkin.setOnDragEntered(event -> {
        });
        //拖动离开事件
        tabHeaderSkin.setOnDragExited((event) -> {
            event.acceptTransferModes(TransferMode.MOVE);

           /* if (EditorArea.dragTab != null) {
                tabDragStyle.setValue(null);
            }*/
            event.consume();
        });
        //拖动放下事件
        tabHeaderSkin.setOnDragDropped((event) -> {
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


        Pane closeButton = (Pane) tabHeaderSkin.lookup(".tab-close-button");
        closeButton.setOnMousePressed(Event::consume);//顶掉原先的按下关闭
        closeButton.setOnMouseClicked(event -> {//改为点击触发关闭 处理关闭点击事件
            //close();
        });
    }

    public void updateTabContentRegion(Node tabContentRegion) {
        FlexTab tab = getTab(tabContentRegion);
        System.out.println("更新 "+tabContentRegion);
    }

    private enum TabDragStyle {
        left, right
    }
}
