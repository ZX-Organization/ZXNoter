package team.zxorg.zxnoter.uiframe.activitypane;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.uiframe.ProjectView;
import team.zxorg.zxnoter.uiframe.ZXNoter;
import team.zxorg.zxnoter.uiframe.factory.MenuFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static team.zxorg.zxnoter.uiframe.activitypane.ActivityItem.ACTIVITY_ITEM_DATA_FORMAT;

public class ActivityBarPane extends HBox {

    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.";
    protected ActivityItem dragedActivityItem;
    /**
     * 当前所有被注册的侧边栏类
     */
    protected static final HashMap<String, Class<? extends ActivityPane>> activityPaneClassMap = new HashMap<>();
    private static final HashMap<String, LangCheckMenuItem> activityItemMenuMap = new HashMap<>();
    private static final ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);

    public static void register(Class<? extends ActivityPane> activityPaneClass) {
        ActivityPane activityPane;
        try {
            activityPane = activityPaneClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            Logger.warning("注册失败: " + activityPaneClass.getSimpleName());
            return;
        }

        activityPaneClassMap.put(activityPane.getId(), activityPaneClass);
        LangCheckMenuItem menuItem = MenuFactory.getCheckMenuItem(LANG + "item." + activityPane.getId());
        menuItem.setId(activityPane.getId());
        activityItemMenu.getItems().add(menuItem);
        activityItemMenuMap.put(activityPane.getId(), menuItem);
        menuItem.setSelected(config.containsItem(menuItem.getId()));
        menuItem.setOnAction(activityItemMenuHandler);
    }

    private static final EventHandler<ActionEvent> activityItemMenuHandler = event -> {
        if (event.getSource() instanceof LangCheckMenuItem menuItem) {
            if (menuItem.isSelected()) {
                config.mainTopBarItems.addLast(menuItem.getId());
            } else {
                config.removeItem(menuItem.getId());
            }
            config.save();
        }
    };

    static {
        //刷新活动项菜单
        config.addEventListener(new ConfigEventListener() {
            @Override
            public void ConfigSaved() {
                for (CheckMenuItem menuItem : activityItemMenuMap.values()) {
                    menuItem.setSelected(config.containsItem(menuItem.getId()));
                }
            }
        });
    }


    protected final HashMap<String, ActivityPane> activityPaneMap = new HashMap<>();


    final ProjectView projectView;
    BorderPane borderPane = new BorderPane() {{
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
        Platform.runLater(this::refresh);
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

    /**
     * 控制所有活动项菜单
     */
    private static final LangMenu activityItemMenu = MenuFactory.getMenu(LANG + "menu.itemConfig");


    private static final LangMenuItem hideBarMenuItem = MenuFactory.getMenuItem(LANG + "menu.hideBar");
    private static final LangMenuItem moveToLeftMenuItem = MenuFactory.getMenuItem(LANG + "menu.moveToLeft");
    private static final LangMenuItem moveToRightMenuItem = MenuFactory.getMenuItem(LANG + "menu.moveToRight");

    static {
        moveToLeftMenuItem.setOnAction(event -> {
            config.mainBarIsLeft = !config.mainBarIsLeft;
            config.save();
        });
        moveToRightMenuItem.setOnAction(moveToLeftMenuItem.getOnAction());
        hideBarMenuItem.setOnAction(event -> {
            if (hideBarMenuItem.getUserData() instanceof MainActivityBar) {
                config.hideMainBar = true;
            } else {
                config.hideSecondBar = true;
            }
            config.save();
        });
    }


    /**
     * 活动栏的右键菜单
     */
    static final ContextMenu activityBarContextMenu = new ContextMenu(activityItemMenu, hideBarMenuItem);
    /**
     * 活动栏的项的右键菜单
     */
    static final ContextMenu activityItemContextMenu = new ContextMenu();


    private static abstract class ActivityBar extends VBox {
        boolean isLeft;
        boolean isHidden;
        ActivityBarPane parent;

        public ActivityBar(ActivityBarPane parent) {
            this.parent = parent;
            getStyleClass().add("activity-bar");

            //设置右键活动项的菜单
            setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    hideBarMenuItem.setUserData(this);
                    activityBarContextMenu.getItems().removeAll(moveToLeftMenuItem, moveToRightMenuItem);
                    activityBarContextMenu.getItems().addAll((isLeft ? moveToRightMenuItem : moveToLeftMenuItem));
                    activityBarContextMenu.show(getScene().getWindow(), event.getScreenX(), event.getScreenY());
                }
            });
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
                    config.activeMainBarTopItemIndex = top.activityItems.indexOf(activityItem);
                } else {
                    topActivityPane.setShow(false);
                    config.activeMainBarTopItemIndex = -1;
                }
                config.needSave();
            });
            bottomGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItem activityItem) {
                    bottomActivityPane.showActivityPane(activityItem.getActivityPane());
                    bottomActivityPane.setShow(true);
                    config.activeMainBarBottomItemIndex = bottom.activityItems.indexOf(activityItem);
                } else {
                    bottomActivityPane.setShow(false);
                    config.activeMainBarBottomItemIndex = -1;
                }
                config.needSave();
            });
        }

        @Override
        void refresh() {
            topActivityPane = (config.mainBarIsLeft ? parent.leftActivityPane : parent.rightActivityPane);
            bottomActivityPane = parent.bottomActivityPane;


            topActivityPane.setShow(false);
            bottomActivityPane.setShow(false);


            top.setShowActivityPane(topActivityPane);
            bottom.setShowActivityPane(bottomActivityPane);
            top.getChildren().clear();
            for (String id : config.mainTopBarItems) {
                ActivityItem activityItem = parent.activityPaneMap.get(id).getActivityItem();
                activityItem.setToggleGroup(topGroup);
                activityItem.bind(parent, topActivityPane);
                top.getChildren().add(activityItem);
            }
            bottom.getChildren().clear();
            for (String id : config.mainBottomBarItems) {
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



            {
                List<ActivityItem> activityItems = top.activityItems;
                if (config.activeMainBarTopItemIndex > -1 && config.activeMainBarTopItemIndex < activityItems.size()) {
                    ActivityItem activityItem = activityItems.get(config.activeMainBarTopItemIndex);
                    activityItem.setSelected(true);
                    topActivityPane.showActivityPane(activityItem.getActivityPane());
                    topActivityPane.setShow(true);
                }
            }
            {
                List<ActivityItem> activityItems = bottom.activityItems;
                if (config.activeMainBarBottomItemIndex > -1 && config.activeMainBarBottomItemIndex < activityItems.size()) {
                    ActivityItem activityItem = activityItems.get(config.activeMainBarBottomItemIndex);
                    activityItem.setSelected(true);
                    bottomActivityPane.showActivityPane(activityItem.getActivityPane());
                    bottomActivityPane.setShow(true);
                }
            }


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
                    config.activeSecondBarItemIndex = top.activityItems.indexOf(activityItem);
                } else {
                    activityPane.setShow(false);
                    config.activeSecondBarItemIndex = -1;
                }
            });
            getChildren().add(top);
        }

        @Override
        void refresh() {
            activityPane = (!config.mainBarIsLeft ? parent.leftActivityPane : parent.rightActivityPane);

            activityPane.setShow(false);



            top.setShowActivityPane(activityPane);
            top.getChildren().clear();
            for (String id : config.secondBarItems) {
                ActivityItem activityItem = parent.activityPaneMap.get(id).getActivityItem();
                activityItem.setToggleGroup(group);
                activityItem.bind(parent, activityPane);
                top.getChildren().add(activityItem);
            }

            List<ActivityItem> activityItems = top.activityItems;
            if (config.activeSecondBarItemIndex > -1 && config.activeSecondBarItemIndex < activityItems.size()) {
                ActivityItem activityItem = activityItems.get(config.activeSecondBarItemIndex);
                activityItem.setSelected(true);
                activityPane.showActivityPane(activityItem.getActivityPane());
                activityPane.setShow(true);
            }
        }
    }

    private static class ShowActivityItemPane extends VBox {
        ActivityBar activityBar;
        ActivityBarPane activityBarPane;
        ShowActivityPane showActivityPane;

        List<ActivityItem> activityItems = new ArrayList<>();

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

            getChildren().addListener((ListChangeListener<Node>) c -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (Node node : c.getAddedSubList()) {
                            if (node instanceof ActivityItem activityItem) {
                                activityItems.add(activityItem);
                            }
                        }
                    } else if (c.wasRemoved()) {
                        for (Node node : c.getRemoved()) {
                            if (node instanceof ActivityItem activityItem) {
                                activityItems.remove(activityItem);
                            }
                        }
                    }
                }
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
                config.mainTopBarItems.remove(activityItem.getId());
                config.secondBarItems.remove(activityItem.getId());
                config.mainBottomBarItems.remove(activityItem.getId());

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
}
