package team.zxorg.zxnoter.ui.main.one.two.three.four;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
    public EditorLayout parentLayout;//上一层布局

    public EditorArea getRootArea() {
        return rootArea;
    }

    public String getName() {
        return uuid.toString().substring(19);
    }

    @Override
    public String toString() {
        return "堆叠容器 父布局{" + parentLayout.getName() + "} 此{" + getName() + "} " + getTabs() + " ";
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

        });


        setOnMouseClicked(event -> {
            //".tab-container > .tab-label >.text"
        });

        //消耗拖拽
        setOnDragDropped(Event::consume);


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
                                System.out.println("更新内容区域{" + uuid.toString().substring(19) + "}");
                                handleContentArea(addedTabPanePane);
                            } else if (addedTabPaneStyleClass.contains("tab-header-area")) {
                                // 处理添加的头部区域
                                System.out.println("更新头部区域{" + uuid.toString().substring(19) + "}");
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

        //TabPane内容容器拖拽到事件监听
        contentArea.setOnDragDropped((event) -> {
            try {
                //检查是否是拖拽的Tab
                if (rootArea.dragTab != null) {

                    //清除所有布局高亮样式
                    for (LayoutPosition layoutPosition : LayoutPosition.values()) {
                        contentArea.getStyleClass().remove("layout-" + layoutPosition.getId());
                    }

                    //清除源Tab
                    rootArea.dragTab.removeParentThis();

                    //通过计算获得目标布局方向位置
                    LayoutPosition layoutPosition = getLayoutPosition(contentArea, event);

                    if (layoutPosition == LayoutPosition.CENTER) {//拖拽到中间
                        getTabs().add(rootArea.dragTab);
                    } else {//进行布局生成
                        // 进行布局生成
                        Orientation targetOrientation = layoutPosition.getOrientation();
                        Orientation sourceOrientation = parentLayout.getOrientation();

                        //如果源布局内容只有一个并且和目标布局方向不相同 可以直接更改布局方向省去创建新布局
                        if ((parentLayout.getItems().size() == 1) && targetOrientation != sourceOrientation) {
                            parentLayout.setOrientation(targetOrientation);
                            sourceOrientation = targetOrientation;
                        }

                        System.out.println("源布局: " + sourceOrientation);
                        System.out.println("目标布局: " + targetOrientation);

                        //找到此TabPane的在父布局索引
                        int index = parentLayout.getItems().indexOf(this);

                        //为拖拽Tab创建新的TabPane
                        EditorTabPane newEditorTabPane = new EditorTabPane(rootArea, parentLayout);
                        newEditorTabPane.getTabs().add(rootArea.dragTab);

                        //布局相同 直接加入布局
                        if (targetOrientation == sourceOrientation) {
                            //根据索引将新的tabPane直接加入到父布局里
                            parentLayout.getItems().add(layoutPosition.isPriority() ? index : index + 1, newEditorTabPane);

                            System.out.println("分隔" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
                        } else {//布局不相同 创建一个布局

                            //创建新的垂直布局
                            EditorLayout newEditorLayout = new EditorLayout(parentLayout);
                            newEditorLayout.setOrientation(targetOrientation);//设置到目标布局方向
                            //将新的布局根据索引位置添加新的布局
                            parentLayout.getItems().add(Math.max(index, 0), newEditorLayout);

                            this.removeParentThis();//删除父节点的自己
                            this.parentLayout = newEditorLayout;//更改父布局为新的布局
                            newEditorTabPane.parentLayout = newEditorLayout;//新的TabPane的父布局修改为新的布局

                            //根据顺序将此Pane和新Pane加入到新布局里
                            if (layoutPosition.isPriority()) {
                                newEditorLayout.getItems().addAll(newEditorTabPane, this);
                            } else {
                                newEditorLayout.getItems().addAll(this, newEditorTabPane);
                            }
                            newEditorLayout.autoLayout();

                            System.out.println("增加到" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
                        }

                    }

                    //将焦点和选中交给拖拽tab
                    rootArea.dragTab.getTabPane().getSelectionModel().select(rootArea.dragTab);
                    rootArea.dragTab.getTabPane().requestFocus();

                    //检查清除拖拽Tab之前的TabPane
                    if (rootArea.dragTabPane == null) {
                        throw new RuntimeException("触发未知异常 拖拽TabPane为null (你怎么能触发到的？？)");
                    }
                    if (rootArea.dragTabPane.getTabs().isEmpty()) {
                        rootArea.dragTabPane.removeParentThis();
                    }
                    rootArea.dragTabPane.parentLayout.checkItems();
                    rootArea.dragTabPane.parentLayout.autoLayout();
                    parentLayout.autoLayout();

                    EditorLayout.printLayout(rootArea, "|");

                    rootArea.dragTab = null;
                    event.setDropCompleted(true);
                }
                //消耗掉事件
                event.consume();
            } catch (Exception e) {
                //为了防止javafx输出异常 (会乱码) 直接打印堆栈跟踪
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

                if (rootArea.dragTabPane.getTabs().size() == 0) {
                    rootArea.dragTabPane.removeParentThis();
                }

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


    public void createEditor(BaseEditor editor) {
        getTabs().add(editor);
        getSelectionModel().select(editor);
    }


}
