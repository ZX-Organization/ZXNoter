package team.zxorg.fxcl.component.flexview;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlexSkinProcessor {
    /**
     * 灵活标签页数据格式 (用于处理拖拽判断)
     */
    private static final DataFormat FLEX_TAB_DATA_FORMAT = new DataFormat("application/x-flex-tab");
    /**
     * 当前的标签页面板
     */
    FlexTabPane tabPane;


    public FlexSkinProcessor(FlexTabPane tabPane) {
        this.tabPane = tabPane;
    }

    /**
     * 通过反射强行获取 FlexTab
     *
     * @param node 只有部分有这玩意
     * @return FlexTab
     */
    private static Tab getTab(Node node) {
        try {
            Method skinField = node.getClass().getMethod("getTab");
            skinField.setAccessible(true);
            return (Tab) skinField.invoke(node);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    void handleNodeChange(Node node) {
        switch (node.getClass().getSimpleName()) {
            case "TabHeaderArea" -> handleTabHeaderArea(node);
            case "TabContentRegion" -> updateTabContentRegion(node);
        }
    }

    private void handleTabHeaderArea(Node node) {
        updateTabHeaderArea(node);
        if (tabPane.lookup(".headers-region") instanceof Pane headersRegion) {
            //Logger.info("初始化和更新 TabHeaderSkin");
            headersRegion.getChildren().forEach(this::updateTabHeaderSkin);
            headersRegion.getChildren().addListener((ListChangeListener<Node>) c1 -> {
                while (c1.next())
                    if (c1.wasAdded())
                        c1.getAddedSubList().forEach(this::updateTabHeaderSkin);
            });
        }
    }

    /**
     * 更新拖拽处理事件  TabPaneSkin$TabHeaderArea
     */
    private void updateTabHeaderArea(Node tabHeaderArea) {
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
            if (!event.getDragboard().getContentTypes().contains(FLEX_TAB_DATA_FORMAT)) {
                return;
            }
            FlexArea area = tabPane.getArea();
            if (area.draggingTab != null) {
                /*if (tabPane.getTabs().contains(area.draggingTab))
                    return;*/
                tabContentRegionDragStyle.set(true);
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //拖动放下事件
        tabHeaderArea.setOnDragDropped((event) -> {
            if (!event.getDragboard().getContentTypes().contains(FLEX_TAB_DATA_FORMAT)) {
                return;
            }
            FlexArea area = tabPane.getArea();
            Tab draggingTab = area.draggingTab;

            // 从 Dragboard 中获取 Tab 的数据
            if (draggingTab != null) {
                tabContentRegionDragStyle.set(false);
                if (draggingTab.getUserData() instanceof FlexTabPane tabPane) {
                    tabPane.getSelectionModel().selectNext();
                }

                //计算新的位置
                //tabPane.removeTab(draggingTab);
                tabPane.addTab(draggingTab);
                Platform.runLater(() -> {
                    tabPane.requestFocus();
                    tabPane.getSelectionModel().select(draggingTab);
                });
                area.draggingTab = null;
                event.setDropCompleted(true);
            }
            event.consume();

        });
        //System.out.println("更新 " + tabHeaderArea);
    }

    /**
     * 更新选项卡标题皮肤  TabPaneSkin$TabHeaderSkin
     */
    private void updateTabHeaderSkin(Node tabHeaderSkin) {
        Tab tab = getTab(tabHeaderSkin);
        ObjectProperty<TabHeaderDragStyle> tabHeaderDragStyle = new SimpleObjectProperty<>();
        tabHeaderDragStyle.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> style = tabHeaderSkin.getStyleClass();
            if (oldValue != null)
                style.remove(oldValue.toString());
            if (newValue != null)
                style.add(newValue.toString());
        });

        //设置开始拖动
        tabHeaderSkin.setOnDragDetected(event -> {
            FlexArea area = tabPane.getArea();
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
            content.put(FLEX_TAB_DATA_FORMAT, "");
            dragboard.setContent(content);

            Pane pane = (Pane) tabHeaderSkin;
            // 设置拖拽时的图标
            dragboard.setDragView(snapshot, pane.getWidth() / 2, pane.getHeight() / 2);


            event.consume();
        });

        tabHeaderSkin.setOnDragOver((event) -> {
            if (!event.getDragboard().getContentTypes().contains(FLEX_TAB_DATA_FORMAT)) {
                return;
            }
            FlexArea area = tabPane.getArea();
            tabHeaderSkin.getParent().getParent().getOnDragExited().handle(event.copyFor(event.getSource(), event.getTarget(), DragEvent.DRAG_EXITED));
            event.consume();
            if (area.draggingTab != null) {
                if (area.draggingTab.equals(tab))
                    return;
                event.acceptTransferModes(TransferMode.MOVE);
                tabHeaderDragStyle.set((event.getX() < ((Pane) tabHeaderSkin).getWidth() / 2 ? TabHeaderDragStyle.left : TabHeaderDragStyle.right));
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
            if (!event.getDragboard().getContentTypes().contains(FLEX_TAB_DATA_FORMAT)) {
                return;
            }
            FlexArea area = tabPane.getArea();
            Tab draggingTab = area.draggingTab;

            // 从 Dragboard 中获取 Tab 的数据
            if (draggingTab != null) {
                if (draggingTab.getUserData() instanceof FlexTabPane tabPane) {
                    tabPane.getSelectionModel().selectNext();
                }


                ObservableList<Tab> tabs = tabPane.getTabs();
                //计算新的位置
                tabPane.removeTab(draggingTab);
                int index = tabs.indexOf(tab) + (event.getX() < ((Pane) tabHeaderSkin).getWidth() / 2 ? 0 : 1);
                tabPane.addTab(index, draggingTab);

                Platform.runLater(() -> {
                    tabPane.requestFocus();
                    tabPane.getSelectionModel().select(draggingTab);
                });

                area.draggingTab = null;
                event.setDropCompleted(true);
            }
            event.consume();

        });


        Pane closeButton = (Pane) tabHeaderSkin.lookup(".tab-close-button");
        closeButton.setOnMousePressed(Event::consume);//顶掉原先的按下关闭
        closeButton.setOnMouseClicked(event -> {//改为点击触发关闭 处理关闭点击事件
            if (tab.getOnCloseRequest() != null) {
                ActionEvent closeActionEvent = new ActionEvent();
                tab.getOnCloseRequest().handle(closeActionEvent);
                if (closeActionEvent.isConsumed())
                    return;
            }

            tab.getTabPane().getTabs().remove(tab);
            if (tab.getOnClosed() != null) {
                Event.fireEvent(tab, new Event(Tab.CLOSED_EVENT));
            }


        });
    }

    /**
     * 更新选项卡内容区域  TabPaneSkin$TabContentRegion
     */
    private void updateTabContentRegion(Node tabContentRegion) {
        Tab tab = getTab(tabContentRegion);
        ObjectProperty<TabContentRegionDragStyle> tabContentRegionDragStyle = new SimpleObjectProperty<>();
        tabContentRegionDragStyle.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> style = tabContentRegion.getStyleClass();
            if (oldValue != null)
                style.remove(oldValue.toString());
            if (newValue != null)
                style.add(newValue.toString());
        });
        //拖动离开
        tabContentRegion.setOnDragExited((event) -> {
            FlexArea area = tabPane.getArea();
            if (area.draggingTab != null) {
                tabContentRegionDragStyle.set(null);
            }
            event.consume();
        });
        //拖动悬停
        tabContentRegion.setOnDragOver((event) -> {
            FlexArea area = tabPane.getArea();
            if (area.draggingTab != null) {

                //计算拖拽位置
                Pane tabContentRegionPane = (Pane) tabContentRegion;
                double w = tabContentRegionPane.getWidth();
                double h = tabContentRegionPane.getHeight();
                double x = event.getX();
                double y = event.getY();

                TabContentRegionDragStyle style = TabContentRegionDragStyle.center;

                //更具拖拽位置设置样式
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


        //拖动放下事件
        tabContentRegion.setOnDragDropped((event) -> {
            if (!event.getDragboard().getContentTypes().contains(FLEX_TAB_DATA_FORMAT)) {
                return;
            }
            FlexArea area = tabPane.getArea();
            Tab draggingTab = area.draggingTab;
            if (draggingTab != null) {
                //如果数量不足1则无效
                if (draggingTab.getTabPane().getTabs().size() <= 1 && draggingTab == tab)
                    return;

                //获取之前的标签页面板
                if (draggingTab.getUserData() instanceof FlexTabPane tabPane) {
                    tabPane.getSelectionModel().selectNext();
                }


                FlexSplitPane parentFlexSplitPane = tabPane.getParentSplitPane();
                //判断分隔方向和父分隔面板方向是否一样
                Orientation parentOrientation = parentFlexSplitPane.getOrientation();
                TabContentRegionDragStyle style = tabContentRegionDragStyle.get();

                //System.out.println(style);
                //System.out.println(parentOrientation);
                int splitPaneIndex = parentFlexSplitPane.getItems().indexOf(tabPane);
                if (parentOrientation.equals(style.getOrientation())) {
                    //System.out.println("方向一样");

                    //System.out.println("同方向拆分窗格");

                    FlexTabPane newTabPane = parentFlexSplitPane.createTabPane(splitPaneIndex + style.offset);
                    //计算新的位置
                    newTabPane.addTab(draggingTab);
                } else if (style.getOrientation() == null) {
                    //中间放入
                    if (draggingTab != tab) {
                        tabPane.addTab(draggingTab);
                    }
                } else {
                    //新分隔面板
                    //System.out.println("非同方向拆分窗格");

                    //创建新的分隔面板
                    //System.out.println("当前索引: " + splitPaneIndex + " 目标索引: " + (splitPaneIndex + style.offset));
                    FlexSplitPane newSplitPane = parentFlexSplitPane.createSplitPane(splitPaneIndex);

                    //为新分隔面板添加标签页面板
                    //将被放下的标签页面板移动到新分隔面板中
                    newSplitPane.addTabPane(tabPane, 0);
                    //为拖动标签页创建新面板
                    FlexTabPane newTabPane = newSplitPane.createTabPane(style.getOffset());
                    newTabPane.addTab(draggingTab);
                }

                //延迟设置新的选中标签页 (处理有瑕疵)
                PauseTransition pause = new PauseTransition(Duration.millis(20));
                pause.setOnFinished(e -> {
                    draggingTab.getTabPane().requestFocus();
                    draggingTab.getTabPane().getSelectionModel().select(draggingTab);
                });
                pause.play();

                //拖拽完成
                area.draggingTab = null;
                event.setDropCompleted(true);
                tabContentRegionDragStyle.set(null);
            }
            event.consume();
        });


        tabContentRegion.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            tabContentRegion.getParent().requestFocus();
        });
        //System.out.println("更新 " + tabContentRegion);
    }

    private enum TabHeaderDragStyle {
        left, right;

        @Override
        public String toString() {
            return "drag-" + name();
        }
    }

    private enum TabContentRegionDragStyle {
        left(Orientation.HORIZONTAL, 0), right(Orientation.HORIZONTAL, 1), top(Orientation.VERTICAL, 0), bottom(Orientation.VERTICAL, 1), center(null, 0);
        private final Orientation orientation;
        private final int offset;

        TabContentRegionDragStyle(Orientation orientation, int offset) {
            this.orientation = orientation;
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        public Orientation getOrientation() {
            return orientation;
        }

        @Override
        public String toString() {
            return "drag-" + name();
        }
    }
}
