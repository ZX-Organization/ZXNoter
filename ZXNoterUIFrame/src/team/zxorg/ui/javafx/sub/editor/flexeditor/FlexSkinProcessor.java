package team.zxorg.ui.javafx.sub.editor.flexeditor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.*;
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
     * 通过反射强行获取 FlexTab
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
     * 更新拖拽处理事件  TabPaneSkin$TabHeaderArea
     */
    public void updateTabHeaderArea(FlexTabPane tabPane, Node tabHeaderArea) {

        ObjectProperty<Boolean> tabContentRegionDragStyle = new SimpleObjectProperty<>();
        tabContentRegionDragStyle.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> style = tabHeaderArea.getStyleClass();
            if (newValue)
                style.add("drag-hover");
            else
                style.remove("drag-hover");
        });

        tabHeaderArea.setOnDragExited((event) -> {
            FlexArea area = tabPane.getArea();
            if (area.draggingTab != null) {
                tabContentRegionDragStyle.set(false);
            }
            event.consume();
        });
        tabHeaderArea.setOnDragOver((event) -> {
            FlexArea area = tabPane.getArea();
            if (area.draggingTab != null) {
                tabContentRegionDragStyle.set(true);
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        System.out.println("更新 " + tabHeaderArea);
    }

    /**
     * 更新选项卡标题皮肤  TabPaneSkin$TabHeaderSkin
     */
    public void updateTabHeaderSkin(Node tabHeaderSkin) {
        FlexTab tab = getTab(tabHeaderSkin);
        ObjectProperty<TabHeaderDragStyle> tabHeaderDragStyle = new SimpleObjectProperty<>();
        tabHeaderDragStyle.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> style = tabHeaderSkin.getStyleClass();
            if (oldValue != null)
                style.remove(oldValue.toString());
            if (newValue != null)
                style.add(newValue.toString());
            System.out.println(style);
        });

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
            event.consume();
            if (area.draggingTab != null) {
                if (area.draggingTab.equals(tab))
                    return;
                event.acceptTransferModes(TransferMode.MOVE);
                tabHeaderDragStyle.set((event.getX() < ((Pane) tabHeaderSkin).getWidth() / 2 ? TabHeaderDragStyle.left : TabHeaderDragStyle.right));
                tabHeaderSkin.getParent().getParent().getOnDragExited().handle(event.copyFor(event.getSource(), event.getTarget(), DragEvent.DRAG_EXITED));
            }
        });
        //拖动进入事件
        tabHeaderSkin.setOnDragEntered(event -> {
            event.consume();
        });
        //拖动离开事件
        tabHeaderSkin.setOnDragExited((event) -> {
            //event.acceptTransferModes(TransferMode.MOVE);

            tabHeaderDragStyle.setValue(null);
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

    /**
     * 更新选项卡内容区域  TabPaneSkin$TabContentRegion
     */
    public void updateTabContentRegion(Node tabContentRegion) {
        FlexTab tab = getTab(tabContentRegion);

        ObjectProperty<TabContentRegionDragStyle> tabContentRegionDragStyle = new SimpleObjectProperty<>();
        tabContentRegionDragStyle.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> style = tabContentRegion.getStyleClass();
            if (oldValue != null)
                style.remove(oldValue.toString());
            if (newValue != null)
                style.add(newValue.toString());
            System.out.println(style);
        });

        tabContentRegion.setOnDragExited((event) -> {
            FlexArea area = tab.getArea();
            if (area.draggingTab != null) {
                tabContentRegionDragStyle.set(null);
            }
            event.consume();
        });
        tabContentRegion.setOnDragOver((event) -> {
            FlexArea area = tab.getArea();
            if (area.draggingTab != null) {

                Pane tabContentRegionPane = (Pane) tabContentRegion;
                double w = tabContentRegionPane.getWidth();
                double h = tabContentRegionPane.getHeight();
                double x = event.getX();
                double y = event.getY();

                TabContentRegionDragStyle style = TabContentRegionDragStyle.center;

                double threshold = 0.1;
                double edgeThreshold = 0.25;

                if (y < h * threshold) {
                    style = TabContentRegionDragStyle.top;
                } else if (h - y < h * threshold) {
                    style = TabContentRegionDragStyle.bottom;
                } else if (x < w * threshold) {
                    style = TabContentRegionDragStyle.left;
                } else if (w - x < w * threshold) {
                    style = TabContentRegionDragStyle.right;
                }

                // 对于 top 和 bottom 方向，再次检查 x 轴位置
                if ((style == TabContentRegionDragStyle.top || style == TabContentRegionDragStyle.bottom) &&
                        (x < w * edgeThreshold || w - x < w * edgeThreshold)) {
                    style = (x < w * edgeThreshold) ? TabContentRegionDragStyle.left : TabContentRegionDragStyle.right;
                }

                tabContentRegionDragStyle.set(style);

                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        System.out.println("更新 " + tabContentRegion);
    }

    private enum TabHeaderDragStyle {
        left, right;

        @Override
        public String toString() {
            return "drag-" + name();
        }
    }

    private enum TabContentRegionDragStyle {
        left, right, top, bottom, center;

        @Override
        public String toString() {
            return "drag-" + name();
        }
    }
}
