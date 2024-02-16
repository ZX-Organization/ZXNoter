package team.zxorg.zxnoter.uiframe.activitypane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.zxnoter.uiframe.ProjectView;
import team.zxorg.zxnoter.uiframe.ZXNoter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static team.zxorg.zxnoter.uiframe.activitypane.ActivityItem.ACTIVITY_ITEM_DATA_FORMAT;

public class ActivityBarPane extends HBox {
    protected ActivityItem dragedActivityItem;
    /**
     * 当前所有被注册的侧边栏类
     */
    protected static final HashMap<String, Class<? extends ActivityPane>> activityPaneClassMap = new HashMap<>();

    public static void register(Class<? extends ActivityPane> activityPaneClass) {
        activityPaneClassMap.put(activityPaneClass.getSimpleName(), activityPaneClass);
    }

    protected final HashMap<String, ActivityPane> activityPaneMap = new HashMap<>();


    private static final ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);
    final ProjectView projectView;
    BorderPane borderPane = new BorderPane(){{
        getStyleClass().add("border-pane");
    }};
    /**
     * 主活动栏
     */
    MainActivityBar mainActivityBar = new MainActivityBar(this);

    /**
     * 次活动栏
     */
    SecondActivityBar secondActivityBar = new SecondActivityBar(this);

    ShowActivityPane leftActivityPane;
    ShowActivityPane rightActivityPane;
    ShowActivityPane bottomActivityPane;

    public ActivityBarPane(ProjectView projectView, Node contentNode) {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("activity-bar-pane");
        this.projectView = projectView;


        leftActivityPane = new ShowActivityPane(this, ShowActivityPanePosition.left);
        rightActivityPane = new ShowActivityPane(this, ShowActivityPanePosition.right);
        bottomActivityPane = new ShowActivityPane(this, ShowActivityPanePosition.bottom);

        borderPane.setCenter(contentNode);

        HBox.setHgrow(borderPane, Priority.ALWAYS);

        getChildren().add(borderPane);

        //实例化所有侧边栏
        for (Map.Entry<String, Class<? extends ActivityPane>> entry : activityPaneClassMap.entrySet()) {
            ActivityPane activityPane = null;
            try {
                activityPane = entry.getValue().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            activityPane.bind(projectView, this);
            activityPaneMap.put(activityPane.getId(), activityPane);
            System.out.println("实例化 " + activityPane);
        }
        refresh();
        config.addEventListener(new ConfigEventListener() {
            @Override
            public void ConfigSaved() {
                refresh();
            }
        });

    }

    public void refresh() {


        mainActivityBar.setHidden(config.hideMainBar);
        secondActivityBar.setHidden(config.hideSecondBar);
        mainActivityBar.setPosition(config.mainBarIsLeft);
        secondActivityBar.setPosition(!config.mainBarIsLeft);
        mainActivityBar.refresh();
        secondActivityBar.refresh();

    }

    private static abstract class ActivityBar extends VBox {
        boolean isLeft;
        boolean isHidden;
        ActivityBarPane parent;

        public ActivityBar(ActivityBarPane parent) {
            this.parent = parent;
            getStyleClass().add("activity-bar");
        }

        /**
         * 设置是否隐藏
         *
         * @param hidden 是否隐藏
         */
        public void setHidden(boolean hidden) {
            isHidden = hidden;
            if (hidden) {
                parent.getChildren().remove(this);
            } else {
                setPosition(isLeft);
            }
        }

        /**
         * 设置活动栏的位置
         *
         * @param isLeft 是否在左边
         */
        public void setPosition(boolean isLeft) {
            this.isLeft = isLeft;
            if (isHidden) return;
            ObservableList<String> styleClass = getStyleClass();
            styleClass.removeAll("left", "right");
            styleClass.add(isLeft ? "left" : "right");

            ObservableList<Node> nodes = parent.getChildren();
            nodes.remove(this);
            nodes.add(isLeft ? 0 : nodes.size(), this);
        }

        abstract void refresh();
    }

    private static class MainActivityBar extends ActivityBar {

        private final ToggleGroup topGroup = new ToggleGroup();
        private final ToggleGroup bottomGroup = new ToggleGroup();

        ShowActivityPane topActivityPane;
        ShowActivityPane bottomActivityPane;

        /**
         * 主活动栏
         */
        ShowActivityItemPane top = new ShowActivityItemPane(this, true) {
            {
                getStyleClass().add("top");
                setAlignment(Pos.TOP_CENTER);
            }
        };
        /**
         * 底部活动栏
         */
        ShowActivityItemPane bottom = new ShowActivityItemPane(this, false) {
            {
                getStyleClass().add("bottom");
                setAlignment(Pos.BOTTOM_CENTER);
                VBox.setVgrow(this, Priority.ALWAYS);
            }
        };

        public MainActivityBar(ActivityBarPane parent) {
            super(parent);

            getChildren().addAll(top, bottom);

            topGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItem activityItem) {
                    topActivityPane.showActivityPane(activityItem.getActivityPane());
                    topActivityPane.setShow(true);
                } else {
                    topActivityPane.setShow(false);
                }
            });
            bottomGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItem activityItem) {
                    bottomActivityPane.showActivityPane(activityItem.getActivityPane());
                    bottomActivityPane.setShow(true);
                } else {
                    bottomActivityPane.setShow(false);
                }
            });
        }

        @Override
        void refresh() {
            topActivityPane = (config.mainBarIsLeft ? parent.leftActivityPane : parent.rightActivityPane);
            bottomActivityPane = parent.bottomActivityPane;
            top.setShowActivityPane(topActivityPane);
            bottom.setShowActivityPane(bottomActivityPane);
            top.getChildren().clear();
            for (String id : config.mainBarItems) {
                ActivityItem activityItem = parent.activityPaneMap.get(id).getActivityItem();
                activityItem.setToggleGroup(topGroup);
                activityItem.bind(parent, topActivityPane);
                top.getChildren().add(activityItem);
            }
            bottom.getChildren().clear();
            for (String id : config.bottomBarItems) {
                ActivityPane activityPane = parent.activityPaneMap.get(id);
                if (activityPane == null) {
                    Logger.warning("ActivityPane not found: " + id);
                    continue;
                }
                ActivityItem activityItem = activityPane.getActivityItem();
                activityItem.setToggleGroup(bottomGroup);
                activityItem.bind(parent, bottomActivityPane);
                bottom.getChildren().add(activityItem);
            }

        }
    }

    private static class ShowActivityItemPane extends VBox {
        ActivityBar activityBar;
        ActivityBarPane activityBarPane;
        ShowActivityPane showActivityPane;

        void setShowActivityPane(ShowActivityPane showActivityPane) {
            this.showActivityPane = showActivityPane;
        }

        boolean isTop;
        BooleanProperty style = new SimpleBooleanProperty(false);

        public ShowActivityItemPane(ActivityBar activityBar, boolean isTop) {
            this.activityBar = activityBar;
            activityBarPane = activityBar.parent;
            this.isTop = isTop;
            getStyleClass().add("activity-item-pane");

            style.addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    getStyleClass().add((isTop ? "drag-top" : "drag-bottom"));
                else
                    getStyleClass().remove((isTop ? "drag-top" : "drag-bottom"));
            });

            setOnDragOver(event -> {
                if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT))
                    return;
                if (activityBarPane.dragedActivityItem == null)
                    return;
                style.set(true);
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });
            setOnDragExited(event -> {
                style.set(false);
            });

            setOnDragDropped(event -> {
                if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT))
                    return;
                if (activityBarPane.dragedActivityItem == null)
                    return;
                style.set(false);
                ActivityItem activityItem = activityBarPane.dragedActivityItem;
                config.mainBarItems.remove(activityItem.getId());
                config.secondBarItems.remove(activityItem.getId());
                config.bottomBarItems.remove(activityItem.getId());

                LinkedHashSet<String> items = showActivityPane.getItems();

                List<String> list = new ArrayList<>(items);
                list.add(activityItem.getId());
                items.clear();
                items.addAll(list);

                activityBarPane.dragedActivityItem = null;

                //立即更新
                config.save();

                event.setDropCompleted(true);
                event.consume();
            });
        }
    }


    private static class SecondActivityBar extends ActivityBar {
        private final ToggleGroup group = new ToggleGroup();
        ShowActivityPane activityPane;

        ShowActivityItemPane top = new ShowActivityItemPane(this, true) {
            {
                getStyleClass().add("top");
                setAlignment(Pos.TOP_CENTER);
                VBox.setVgrow(this, Priority.ALWAYS);
            }
        };

        public SecondActivityBar(ActivityBarPane parent) {
            super(parent);
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItem activityItem) {
                    activityPane.showActivityPane(activityItem.getActivityPane());
                    activityPane.setShow(true);
                } else {
                    activityPane.setShow(false);
                }
            });
            getChildren().add(top);
        }

        @Override
        void refresh() {
            activityPane = (!config.mainBarIsLeft ? parent.leftActivityPane : parent.rightActivityPane);
            top.setShowActivityPane(activityPane);
            top.getChildren().clear();
            for (String id : config.secondBarItems) {
                ActivityItem activityItem = parent.activityPaneMap.get(id).getActivityItem();
                activityItem.setToggleGroup(group);
                activityItem.bind(parent, activityPane);
                top.getChildren().add(activityItem);
            }
        }
    }
}
