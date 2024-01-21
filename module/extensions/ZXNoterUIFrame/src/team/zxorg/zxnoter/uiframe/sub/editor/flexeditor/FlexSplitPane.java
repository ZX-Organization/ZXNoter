package team.zxorg.zxnoter.uiframe.sub.editor.flexeditor;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import team.zxorg.extensionloader.core.Logger;

/**
 * 灵活编辑器 拆分窗格
 */
public class FlexSplitPane extends SplitPane {
    /**
     * 根区域
     */
    private final ObjectProperty<FlexArea> flexArea = new SimpleObjectProperty<>();

    /**
     * 父拆分窗格
     */
    private final ObjectProperty<FlexSplitPane> parentFlexSplitPane = new SimpleObjectProperty<>();

    {
        getStyleClass().addAll("flex-split-pane");
    }

    public FlexSplitPane() {
        /*parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof FlexEditorSplitPane editorSplitPane) {

            }
            System.out.println(newValue);
        });*/

        //监听项目列表变化
        getItems().addListener((ListChangeListener<Node>) c -> {
            while (c.next()) {
                //判断是被移除时 列表为空
                if (c.wasRemoved()) {
                    if (c.getList().isEmpty()) {
                        Logger.info("拆分窗格列表为空 删除自身");
                        if (getParent().getParent() instanceof FlexSplitPane editorSplitPane) {
                            editorSplitPane.getItems().remove(this);
                        }
                    } else if (c.getList().size() == 1) {
                        FlexSplitPane flexSplitPane = getParentFlexSplitPane();
                        //如果是根，则不精简
                        if (flexSplitPane != null) {
                            //精简
                            Logger.info("拆分窗格列表精简");
                            flexSplitPane.getItems().addAll(c.getList());
                            Platform.runLater(() -> {
                                getItems().clear();
                            });
                        }
                    }

                    //释放并解除
                    for (Node node : c.getRemoved()) {
                        if (node instanceof FlexTabPane tabPane) {
                            //tabPane.parentFlexSplitPane.set(null);
                        } else if (node instanceof FlexSplitPane splitPane) {
                            //splitPane.parentFlexSplitPane.set(null);
                        }
                    }
                    updateDividers();
                } else if (c.wasAdded()) {
                    //添加并绑定
                    for (Node node : c.getAddedSubList()) {
                        if (node instanceof FlexTabPane tabPane) {
                            tabPane.parentFlexSplitPane.set(this);



                        } else if (node instanceof FlexSplitPane splitPane) {
                            splitPane.parentFlexSplitPane.set(this);
                            splitPane.flexArea.bind(flexAreaProperty());
                            splitPane.setOrientation((getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL));


                        }
                    }
                    //重新计算每个的占比
                    updateDividers();

                }
            }
        });
    }

    private void updateDividers() {
        double v = 1. / (getItems().size());
        double v2=0;
        for (Divider divider : getDividers()) {
            v2+=v;
            divider.setPosition(v2);
        }
    }

    public ReadOnlyObjectProperty<FlexArea> flexAreaProperty() {
        return flexArea;
    }

    public ReadOnlyObjectProperty<FlexSplitPane> parentFlexSplitPaneProperty() {
        return parentFlexSplitPane;
    }

    public FlexArea getFlexArea() {
        return flexArea.get();
    }

    protected void setFlexArea(FlexArea flexArea) {
        this.flexArea.set(flexArea);
    }

    public FlexSplitPane getParentFlexSplitPane() {
        return parentFlexSplitPane.get();
    }

    public FlexTabPane createTabPane() {
        return createTabPane(getItems().size());
    }

    /**
     * 在指定索引创建一个选项卡窗格
     *
     * @param index 索引
     * @return 新的选项卡窗格
     */
    public FlexTabPane createTabPane(int index) {
        FlexTabPane tabPane = new FlexTabPane();
        getItems().add(index, tabPane);
        return tabPane;
    }

    public void addTabPane(FlexTabPane tabPane, int index) {
        tabPane.getParentSplitPane().removeSplitPane(tabPane);
        getItems().add(index, tabPane);
    }

    public FlexSplitPane createSplitPane() {
        return createSplitPane(getItems().size());
    }

    /**
     * 在指定索引创建一个拆分窗格
     *
     * @param index 索引
     * @return 新的拆分窗格
     */
    public FlexSplitPane createSplitPane(int index) {
        FlexSplitPane newSplitPane = new FlexSplitPane();
        getItems().add(index, newSplitPane);
        return newSplitPane;
    }

    public void removeSplitPane(int index) {
        ObservableList<Node> items = getItems();
        items.remove(index);
    }

    public void removeSplitPane(FlexTabPane tabPane) {
        ObservableList<Node> items = getItems();
        items.remove(tabPane);
    }
    /**
     * 关闭此拆分窗格
     */
    public void close() {
        //移除所有子物品
        for (Node item : getItems()) {
            if (item instanceof FlexSplitPane flexSplitPane) {
                //可能有子拆分窗格

            } else if (item instanceof FlexTabPane flexEditorTabPane) {
                //可能有子选项卡窗格

            }
        }
    }


}
