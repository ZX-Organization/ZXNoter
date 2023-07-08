package team.zxorg.zxnoter.ui.main.one.two.three.four;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.ui.main.one.two.three.EditorArea;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.BaseEditor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class EditorTabPane extends TabPane {
    private final UUID uuid = UUID.randomUUID();
    private final EditorArea area;//编辑器区域
    private final EditorLayout layout;//上一层布局


    public EditorTabPane(EditorArea area, EditorLayout layout) {
        this.area = area;
        this.layout = layout;
        setId(uuid.toString());
        setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        getStyleClass().add("editor-tab-pane");

        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    List<? extends Tab> removedList = c.getAddedSubList();
                    for (Tab tab : removedList) {
                        area.editorHashMap.remove(tab.getId());
                    }
                    if (getTabs().size() == 0) {//检查是否还有tab 没有则删除自身
                        layout.getItems().remove(this);
                    }
                } else if (c.wasAdded()) {
                    List<? extends Tab> addedList = c.getAddedSubList();
                    for (Tab tab : addedList) {

                        area.editorHashMap.put(tab.getId(), (BaseEditor) tab);
                    }
                }
            }

/*
             else {
                Platform.runLater(() -> {
                    for (Tab tab : getTabs()) {
                        if (tab instanceof BaseEditor baseEditor) {
                            baseEditor.updateDrag();
                        }
                    }
                });
            }*/
        });


        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //".tab-container > .tab-label >.text"
            }
        });



       /* setOnDragDropped((event) -> {
            if (EditorArea.dragTab!=null) {
                // 创建控件来显示快照

                event.setDropCompleted(true);
            }
            event.consume();
        });
*/


        setOnDragDropped((event) -> {
            //System.out.println(event);
            // 从 Dragboard 中获取 Tab 的数据
            /*Tab tabToMove = EditorArea.dragTab;
            if (tabToMove != null) {
                getTabs().add(tabToMove);
                event.setDropCompleted(true);
            }
*/
            event.consume();
        });


        //tabPane监听子节点变化
        getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    List<? extends Node> addedTabPaneNodes = c.getAddedSubList();
                    for (Node addedTabPaneNode : addedTabPaneNodes) {
                        if (addedTabPaneNode instanceof Pane addedTabPanePane) {
                            ObservableList<String> addedTabPaneStyleClass = addedTabPanePane.getStyleClass();
                            if (addedTabPaneStyleClass.contains("tab-content-area")) {
                                // 处理添加的内容区域
                                handleContentArea(addedTabPanePane);
                            } else if (addedTabPaneStyleClass.contains("tab-header-area")) {
                                // 处理添加的头部区域
                                handleHeaderArea(addedTabPanePane);
                            }
                        }
                    }
                }
                if (c.wasRemoved()) {
                    List<? extends Node> removedNodes = c.getRemoved();
                    // 处理移除的子节点列表
                    handleRemovedNodes(removedNodes);
                }
            }
        });
    }


    private void handleContentArea(Pane contentArea) {
        contentArea.setOnDragOver(event -> {
            for (int i = 0; i < 4; i++) {
                contentArea.getStyleClass().remove("layout-" + i);
            }
            if (area.dragTab != null) {
                int layoutMode = getLayout(contentArea, event);
                contentArea.getStyleClass().add("layout-" + layoutMode);
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        contentArea.setOnDragExited((event) -> {
            if (area.dragTab != null) {
                for (int i = 0; i < 4; i++) {
                    contentArea.getStyleClass().remove("layout-" + i);
                }
            }
            event.consume();
        });


        contentArea.setOnDragDropped((event) -> {

            // 从 Dragboard 中获取 Tab 的数据
            if (area.dragTab != null) {

                for (int i = 0; i < 4; i++) {
                    contentArea.getStyleClass().remove("layout-" + i);
                }

                area.dragTabPane.getTabs().remove(area.dragTab);
                int layoutMode = getLayout(contentArea, event);

                Orientation source = layout.getOrientation();
                getLayout(contentArea, event);

                if (layoutMode == 1 || layoutMode == 3) {

                    if (source == Orientation.HORIZONTAL) {//源水平
                        int index = layout.getItems().indexOf(this);

                        EditorTabPane newEditorTabPane = new EditorTabPane(area, layout);
                        layout.getItems().add((layoutMode == 1 ? index : index + 1), newEditorTabPane);
                        newEditorTabPane.getTabs().add(area.dragTab);
                        System.out.println("分隔水平");

                    } else if (source == Orientation.VERTICAL) {//源垂直
                        EditorLayout editorLayout = new EditorLayout();
                        editorLayout.setOrientation(Orientation.HORIZONTAL);

                        EditorTabPane newEditorTabPane = new EditorTabPane(area, layout);

                        editorLayout.getItems().add(newEditorTabPane);

                        newEditorTabPane.getTabs().add(area.dragTab);
                        System.out.println("分隔水平");

                        layout.getItems().add(editorLayout);
                    }
                }


                area.dragTab = null;
                event.setDropCompleted(true);
            }
            event.consume();

        });
    }

    private int getLayout(Pane contentArea, DragEvent event) {
        int layoutMode = 0;//其他部位
        if (event.getX() < 32) {
            layoutMode = 1;//左边
        } else if (contentArea.getHeight() - event.getY() < 32) {
            layoutMode = 2;//底部
        } else if (contentArea.getWidth() - event.getX() < 32) {
            layoutMode = 3;//右边
        }
        return layoutMode;
    }

    private void handleHeaderArea(Pane headerArea) {
        headerArea.setOnDragOver(event -> {
            if (area.dragTab != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        headerArea.setOnDragExited((event) -> {
            if (area.dragTab != null) {
            }
            event.consume();
        });

        headerArea.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            if (area.dragTab != null) {
                ObservableList<Tab> tabs = getTabs();

                area.dragTabPane.getTabs().remove(area.dragTab);

                tabs.add(area.dragTab);
                getSelectionModel().select(area.dragTab);
                requestFocus();
                event.setDropCompleted(true);
                area.dragTab = null;
            }
            event.consume();
        });

        for (Node subNode : headerArea.getChildren()) {
            if (subNode.getStyleClass().contains("headers-region")) {
                if (subNode instanceof Pane subPane) {
                    handleTabs(subPane.getChildren());
                    listenForTabChanges(subPane);
                }
            }
        }
    }

    private void handleTabs(List<Node> tabs) {
        for (Node tab : tabs) {
            if (tab instanceof Pane pane) {
                BaseEditor baseEditor = area.editorHashMap.get(tab.getId());
                baseEditor.updateDrag(pane, (Region) pane.getChildren().get(0));
            }

        }
    }

    private void listenForTabChanges(Pane headerArea) {
        headerArea.getChildren().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    handleTabs((List<Node>) c.getAddedSubList());
                }
            }
        });
    }

    private void handleRemovedNodes(List<? extends Node> removedNodes) {
// 处理移除的子节点列表
//System.out.println("移除的子节点：" + removedNodes);
    }


    public BaseEditor createEditor(Class editorClass) {
        try {
            BaseEditor editor = (BaseEditor) editorClass.getDeclaredConstructor(EditorArea.class).newInstance(area);
            getTabs().add(editor);
            getSelectionModel().select(editor);
            return editor;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
