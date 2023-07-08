package team.zxorg.zxnoter.ui.main.one.two.three.four;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
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
import java.util.List;
import java.util.UUID;

public class EditorTabPane extends TabPane {
    private final UUID uuid = UUID.randomUUID();
    private final EditorArea rootArea;//编辑器区域
    protected EditorLayout parentLayout;//上一层布局

    @Override
    public String toString() {
        return "编辑器堆叠 " + getTabs() + " ";
    }

    public EditorTabPane(EditorArea rootArea, EditorLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.rootArea = rootArea;
        setId(uuid.toString());
        setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        getStyleClass().add("editor-tab-pane");

        getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {

                    List<? extends Tab> removedList = c.getAddedSubList();
                    for (Tab tab : removedList) {
                        rootArea.editorHashMap.remove(tab.getId());
                    }
                    /*if (getTabs().size() == 0) {//检查是否还有tab 没有则删除自身
                        if (!parentLayout.getItems().remove(this)) {
                            System.out.println("编辑器堆叠 自清除失败");
                        }
                    }*/
                } else if (c.wasAdded()) {
                    List<? extends Tab> addedList = c.getAddedSubList();
                    for (Tab tab : addedList) {
                        rootArea.editorHashMap.put(tab.getId(), (BaseEditor) tab);
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


       /* parentProperty().addListener((observable, oldValue, newValue) -> {
            for (EditorLayout layout : rootArea.parentLayout) {
                if (layout.getItems().contains(this)) {
                    parentLayout = layout;
                }
            }
        });*/

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
            for (LayoutPosition layoutPosition : LayoutPosition.values()) {
                contentArea.getStyleClass().remove("layout-" + layoutPosition.getId());
            }
            if (rootArea.dragTab != null) {
                LayoutPosition layoutPosition = getLayoutPosition(contentArea, event);
                contentArea.getStyleClass().add("layout-" + layoutPosition.getId());
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        contentArea.setOnDragExited((event) -> {
            if (rootArea.dragTab != null) {
                for (LayoutPosition layoutPosition : LayoutPosition.values()) {
                    contentArea.getStyleClass().remove("layout-" + layoutPosition.getId());
                }
            }
            event.consume();
        });


        contentArea.setOnDragDropped((event) -> {

            try {// 从 Dragboard 中获取 Tab 的数据
                if (rootArea.dragTab != null) {

                    for (LayoutPosition layoutPosition : LayoutPosition.values()) {
                        contentArea.getStyleClass().remove("layout-" + layoutPosition.getId());
                    }

                    rootArea.dragTab.removeParentThis();//清除源Tab
                    LayoutPosition layoutPosition = getLayoutPosition(contentArea, event);

                    if (layoutPosition == LayoutPosition.CENTER) {//中间
                        getTabs().add(rootArea.dragTab);
                        getSelectionModel().select(rootArea.dragTab);
                        requestFocus();

                    } else {//进行布局生成
                        // 进行布局生成
                        Orientation targetOrientation = layoutPosition.getOrientation();
                        Orientation sourceOrientation = parentLayout.getOrientation();

                        if ((parentLayout.getItems().size() == 1) && targetOrientation != sourceOrientation) {
                            parentLayout.setOrientation(targetOrientation);
                            sourceOrientation = targetOrientation;
                        }


                        System.out.println("源: " + sourceOrientation);
                        System.out.println("到: " + targetOrientation);


                        if (targetOrientation == sourceOrientation) {
                            int index = parentLayout.getItems().indexOf(this);

                            EditorTabPane newEditorTabPane = new EditorTabPane(rootArea, parentLayout);
                            newEditorTabPane.getTabs().add(rootArea.dragTab);

                            parentLayout.getItems().add((layoutPosition == LayoutPosition.LEFT || layoutPosition == LayoutPosition.TOP) ? index : index + 1, newEditorTabPane);

                            System.out.println("分隔" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
                        } else {
                            //父布局水平 创建一个垂直

                            //找到此TabPane的在父布局索引
                            int index = parentLayout.getItems().indexOf(this);

                            //创建新的垂直布局
                            EditorLayout newEditorLayout = new EditorLayout(parentLayout);
                            newEditorLayout.setOrientation(targetOrientation);

                            //为拖拽Tab创建新的TabPane
                            EditorTabPane newEditorTabPane = new EditorTabPane(rootArea, newEditorLayout);

                            parentLayout.getItems().add(Math.max(index, 0), newEditorLayout);
                            newEditorTabPane.getTabs().add(rootArea.dragTab);

                            this.removeParentThis();//删除父节点的自己
                            this.parentLayout = newEditorLayout;

                            if ((layoutPosition == LayoutPosition.LEFT || layoutPosition == LayoutPosition.TOP)) {
                                newEditorLayout.getItems().addAll(newEditorTabPane, this);
                            } else {
                                newEditorLayout.getItems().addAll(this, newEditorTabPane);
                            }


                            System.out.println("增加到" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
                        }


                    }

                    if (rootArea.dragTabPane.getTabs().size() == 0) {
                        rootArea.dragTabPane.removeParentThis();
                    }


                    System.out.println(rootArea);

                    rootArea.dragTab = null;
                    rootArea.dragTabPane = null;
                    event.setDropCompleted(true);
                }
                event.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeParentThis() {
        if (!parentLayout.getItems().remove(this)) {
            System.out.println("没删成功");
        }
    }


    /**
     * 计算布局位置
     */
    private LayoutPosition getLayoutPosition(Pane contentArea, DragEvent event) {
        if (event.getX() < 32) {
            return LayoutPosition.LEFT;
        } else if (contentArea.getWidth() - event.getX() < 32) {
            return LayoutPosition.RIGHT;
        } else if (event.getY() < 32) {
            return LayoutPosition.TOP;
        } else if (contentArea.getHeight() - event.getY() < 32) {
            return LayoutPosition.BOTTOM;
        }
        return LayoutPosition.CENTER;
    }

    private void handleHeaderArea(Pane headerArea) {
        headerArea.setOnDragOver(event -> {
            if (rootArea.dragTab != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        headerArea.setOnDragExited((event) -> {
            if (rootArea.dragTab != null) {
            }
            event.consume();
        });

        headerArea.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            if (rootArea.dragTab != null) {
                ObservableList<Tab> tabs = getTabs();

                rootArea.dragTabPane.getTabs().remove(rootArea.dragTab);

                tabs.add(rootArea.dragTab);
                getSelectionModel().select(rootArea.dragTab);
                requestFocus();
                event.setDropCompleted(true);
                rootArea.dragTab = null;
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
                BaseEditor baseEditor = rootArea.editorHashMap.get(tab.getId());
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
            BaseEditor editor = (BaseEditor) editorClass.getDeclaredConstructor(EditorArea.class).newInstance(rootArea);
            getTabs().add(editor);
            getSelectionModel().select(editor);
            return editor;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


}
