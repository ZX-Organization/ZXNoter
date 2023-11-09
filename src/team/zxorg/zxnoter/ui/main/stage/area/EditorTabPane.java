package team.zxorg.zxnoter.ui.main.stage.area;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseTab;

import java.nio.file.Path;
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

    private final ObjectProperty<LayoutPosition> layoutPositionObjectProperty = new SimpleObjectProperty<>();

    public EditorTabPane(EditorArea rootArea, EditorLayout parentLayout) {
        this.parentLayout = parentLayout;
        this.rootArea = rootArea;
        setId(uuid.toString());
        //setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
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
                        rootArea.editorHashMap.put(tab.getId(), (BaseTab) tab);
                    }
                }
            }
        });


        setOnMouseClicked(event -> {
            //".tab-container > .tab-label >.text"
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

        setOnDragExited((event) -> {
            if (EditorArea.dragTab != null) {

            }
            event.consume();
        });


        //消耗拖拽
        setOnDragDropped((event -> {
            //检查是否是拖拽的Tab
            if (EditorArea.dragTab != null) {
                System.out.println("辅助拖拽");
                //清除所有布局高亮样式
                layoutPositionObjectProperty.set(null);

                //处理拖拽
                setLayout(LayoutPosition.CENTER, EditorArea.dragTab);

                handleDragDropped();
                event.setDropCompleted(true);
            }
            //消耗掉事件
            event.consume();

        }));

        setOnDragOver(event -> {
            if (EditorArea.dragTab != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });


      /*  focusedBooleanProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                getStyleClass().add("tab-focused");
            else
                getStyleClass().remove("tab-focused");
        });*/

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rootArea.focusEditorTabPane.set(this);
            }
        });


    }


    private void handleContentArea(Pane contentArea) {
        layoutPositionObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                contentArea.getStyleClass().remove("layout-" + oldValue.getId());
            if (newValue != null) {
                contentArea.getStyleClass().add("layout-" + newValue.getId());
            }
        });

        contentArea.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            rootArea.focusEditorTabPane.set(this);
        });

        contentArea.setOnDragOver(event -> {
            if (EditorArea.dragTab != null) {
                LayoutPosition layoutPosition = getLayoutPosition(contentArea, event);
                layoutPositionObjectProperty.set(layoutPosition);
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        contentArea.setOnDragExited((event) -> {
            if (EditorArea.dragTab != null) {
                layoutPositionObjectProperty.set(null);
            }
            event.consume();
        });

        //TabPane内容容器拖拽到事件监听
        contentArea.setOnDragDropped((event) -> {
            //检查是否是拖拽的Tab
            if (EditorArea.dragTab != null) {

                //清除所有布局高亮样式
                layoutPositionObjectProperty.set(null);

                //通过计算获得目标布局方向位置
                LayoutPosition layoutPosition = getLayoutPosition(contentArea, event);
                //处理拖拽
                setLayout(layoutPosition, EditorArea.dragTab);

                handleDragDropped();
                event.setDropCompleted(true);
            }
            //消耗掉事件
            event.consume();

        });


    }

    public void setLayout(LayoutPosition layoutPosition, BaseTab sourceEditor) {
        //清除源Tab
        sourceEditor.removeParentThis();

        if (layoutPosition == LayoutPosition.CENTER) {//拖拽到中间
            getTabs().add(EditorArea.dragTab);
        } else {//进行布局生成
            // 进行布局生成
            Orientation targetOrientation = layoutPosition.getOrientation();
            Orientation sourceOrientation = parentLayout.getOrientation();

            //如果源布局内容只有一个并且和目标布局方向不相同 可以直接更改布局方向省去创建新布局
            if ((parentLayout.getItems().size() == 1) && targetOrientation != sourceOrientation) {
                parentLayout.setOrientation(targetOrientation);
                sourceOrientation = targetOrientation;
            }

            //找到此TabPane的在父布局索引
            int index = parentLayout.getItems().indexOf(this);

            //为拖拽Tab创建新的TabPane
            EditorTabPane newEditorTabPane = new EditorTabPane(rootArea, parentLayout);
            newEditorTabPane.getTabs().add(sourceEditor);

            //布局相同 直接加入布局
            if (targetOrientation == sourceOrientation) {
                //根据索引将新的tabPane直接加入到父布局里
                parentLayout.getItems().add(layoutPosition.isPriority() ? index : index + 1, newEditorTabPane);

                ZXLogger.info("布局分隔" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
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

                ZXLogger.info("布局增加新" + (targetOrientation == Orientation.HORIZONTAL ? "水平" : "垂直"));
            }

        }
    }

    public void removeParentThis() {
        if (!parentLayout.getItems().remove(this)) {
            ZXLogger.warning("没删成功");
        }
    }


    /**
     * 计算布局位置
     */
    private static LayoutPosition getLayoutPosition(Pane contentArea, DragEvent event) {
        if (event.getX() < 64) {
            return LayoutPosition.LEFT;
        } else if (contentArea.getWidth() - event.getX() < 64) {
            return LayoutPosition.RIGHT;
        } else if (event.getY() < 64) {
            return LayoutPosition.TOP;
        } else if (contentArea.getHeight() - event.getY() < 64) {
            return LayoutPosition.BOTTOM;
        }
        return LayoutPosition.CENTER;
    }


    private void handleHeaderArea(Pane headerArea) {

        headerArea.setOnDragOver(event -> {
            if (EditorArea.dragTab != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        headerArea.setOnDragExited((event) -> {

        });

        headerArea.setOnDragDropped((event) -> {
            // 从 Dragboard 中获取 Tab 的数据
            if (EditorArea.dragTab != null) {
                EditorArea.dragTab.removeParentThis();
                getTabs().add(EditorArea.dragTab);
                handleDragDropped();
                event.setDropCompleted(true);
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

    /**
     * 处理拖拽完成后事
     */
    public void handleDragDropped() {
        EditorTabPane dragTabPane = EditorArea.dragTabPane;
        BaseTab dragEditor = EditorArea.dragTab;

        if (dragEditor != null) {
            if (dragEditor.getTabPane() != null) {
                //将焦点和选中交给拖拽tab
                dragEditor.getTabPane().getSelectionModel().select(dragEditor);
                dragEditor.getTabPane().requestFocus();
            }
        }

        //检查清除拖拽Tab之前的TabPane
        if (dragTabPane == null) {
            ZXLogger.warning("触发未知异常 拖拽TabPane为null (你怎么能触发到的？？)");
            return;
        }

        //检查源堆叠数量 为空移除
        if (dragTabPane.getTabs().isEmpty()) {
            if (dragTabPane.parentLayout instanceof EditorArea editorArea) {
                // 如果父布局是 EditorArea(Root)，并且其包含的子布局数量不等于1，则移除父布局
                if (editorArea.getItems().size() != 1) {
                    dragTabPane.removeParentThis();
                }
            } else {
                // 如果父布局不是 EditorArea，则直接移除
                dragTabPane.removeParentThis();
            }
        }

        //检查源堆叠的父布局物品数 并计算布局
        dragTabPane.parentLayout.checkItems();
        dragTabPane.parentLayout.autoLayout();
        //检查此父布局的布局
        parentLayout.autoLayout();

        EditorArea.dragTab = null;
    }

    private void handleTabs(List<Node> tabs) {
        for (Node tab : tabs) {
            if (tab instanceof Pane pane) {
                BaseTab baseTab = rootArea.editorHashMap.get(tab.getId());
                baseTab.updateDrag(pane, (Region) pane.getChildren().get(0));
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

    }


    public void createEditor(BaseTab editor) {
        getTabs().add(editor);
        //getSelectionModel().select(editor);
    }





    /*public void openFile(Path path) {
        BaseEditor baseFileEditor = new EditorTabPane();
        getTabs().add(baseFileEditor);
        getSelectionModel().select(baseFileEditor);
        BaseFileEditor now = (BaseFileEditor) zxProject.editorMap.get(baseFileEditor.getFileItem().path);
    }*/

   /* public void createFileEditor(Path filePath) {
        BaseEditor baseFileEditor;

        getTabs().add(baseFileEditor);
        getSelectionModel().select(baseFileEditor);
        BaseFileEditor now = (BaseFileEditor) zxProject.editorMap.get(baseFileEditor.getFileItem().path);
    }*/

}
