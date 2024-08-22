package team.zxorg.zxnoter.ui.component.activitypane;

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
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.ZXNoter;
import team.zxorg.zxnoter.ui.factory.MenuFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static team.zxorg.zxnoter.ui.component.activitypane.ActivityItemSkin.ACTIVITY_ITEM_DATA_FORMAT;

public class ActivityPane extends HBox {

    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.";
    /**
     * 当前所有被注册的侧边栏类
     */
    protected static final HashMap<String, Class<? extends ActivityPaneSkin>> activityPaneClassMap = new HashMap<>();
    /**
     * 活动栏的项的右键菜单
     */
    static final ContextMenu activityItemContextMenu = new ContextMenu();
    private static final HashMap<String, LangCheckMenuItem> activityItemMenuMap = new HashMap<>();
    private static final ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);
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
    /**
     * 控制所有活动项菜单
     */
    private static final LangMenu activityItemMenu = MenuFactory.getMenu(LANG + "menu.itemConfig");
    private static final LangMenuItem hideBarMenuItem = MenuFactory.getMenuItem(LANG + "menu.hideBar");
    /**
     * 活动栏的右键菜单
     */
    static final ContextMenu activityBarContextMenu = new ContextMenu(activityItemMenu, hideBarMenuItem);
    private static final LangMenuItem moveToLeftMenuItem = MenuFactory.getMenuItem(LANG + "menu.moveToLeft");
    private static final LangMenuItem moveToRightMenuItem = MenuFactory.getMenuItem(LANG + "menu.moveToRight");

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

    protected final HashMap<String, ActivityPaneSkin> activityPaneMap = new HashMap<>();
    final ProjectView projectView;
    protected ActivityItemSkin dragedActivityItemSkin;
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
    ActivityBarPaneSkin leftActivityPane;
    ActivityBarPaneSkin rightActivityPane;
    ActivityBarPaneSkin bottomActivityPane;

    public ActivityPane(ProjectView projectView, Node contentNode) {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("activity-bar-pane");
        this.projectView = projectView;


        leftActivityPane = new ActivityBarPaneSkin(this, ActivityBarPaneSkin.ActivityPanePositionSkin.left);
        rightActivityPane = new ActivityBarPaneSkin(this, ActivityBarPaneSkin.ActivityPanePositionSkin.right);
        bottomActivityPane = new ActivityBarPaneSkin(this, ActivityBarPaneSkin.ActivityPanePositionSkin.bottom);

        borderPane.setCenter(contentNode);

        HBox.setHgrow(borderPane, Priority.ALWAYS);

        getChildren().add(borderPane);

        //实例化所有侧边栏
        for (Map.Entry<String, Class<? extends ActivityPaneSkin>> entry : activityPaneClassMap.entrySet()) {
            ActivityPaneSkin activityPaneSkin = null;
            try {
                activityPaneSkin = entry.getValue().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            activityPaneSkin.bind(projectView, this);
            activityPaneMap.put(activityPaneSkin.getId(), activityPaneSkin);
        }
        Platform.runLater(this::refresh);
        config.addEventListener(new ConfigEventListener() {
            @Override
            public void ConfigSaved() {
                refresh();
            }
        });

    }

    public static void register(Class<? extends ActivityPaneSkin> activityPaneClass) {
        ActivityPaneSkin activityPaneSkin;
        try {
            activityPaneSkin = activityPaneClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            Logger.warning("注册失败: " + activityPaneClass.getSimpleName() + " " + e.getMessage());
            return;
        }

        activityPaneClassMap.put(activityPaneSkin.getId(), activityPaneClass);
        LangCheckMenuItem menuItem = MenuFactory.getCheckMenuItem(LANG + "item." + activityPaneSkin.getId());
        menuItem.setId(activityPaneSkin.getId());
        activityItemMenu.getItems().add(menuItem);
        activityItemMenuMap.put(activityPaneSkin.getId(), menuItem);
        menuItem.setSelected(config.containsItem(menuItem.getId()));
        menuItem.setOnAction(activityItemMenuHandler);
        config.save();
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
        ActivityPane parent;

        public ActivityBar(ActivityPane parent) {
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

        ActivityBarPaneSkin topActivityPane;
        ActivityBarPaneSkin bottomActivityPane;

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

        public MainActivityBar(ActivityPane parent) {
            super(parent);

            getChildren().addAll(top, bottom);

            topGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItemSkin activityItemSkin) {
                    topActivityPane.showActivityPane(activityItemSkin.getActivityPane());
                    topActivityPane.setShow(true);
                    config.activeMainBarTopItemIndex = top.activityItemSkins.indexOf(activityItemSkin);
                } else {
                    topActivityPane.setShow(false);
                    config.activeMainBarTopItemIndex = -1;
                }
                config.needSave();
            });
            bottomGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItemSkin activityItemSkin) {
                    bottomActivityPane.showActivityPane(activityItemSkin.getActivityPane());
                    bottomActivityPane.setShow(true);
                    config.activeMainBarBottomItemIndex = bottom.activityItemSkins.indexOf(activityItemSkin);
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
                ActivityItemSkin activityItemSkin = parent.activityPaneMap.get(id).getActivityItem();
                activityItemSkin.setToggleGroup(topGroup);
                activityItemSkin.bind(parent, topActivityPane);
                top.getChildren().add(activityItemSkin);
            }
            bottom.getChildren().clear();
            for (String id : config.mainBottomBarItems) {
                ActivityPaneSkin activityPaneSkin = parent.activityPaneMap.get(id);
                if (activityPaneSkin == null) {
                    Logger.warning("ActivityPane not found: " + id);
                    continue;
                }
                ActivityItemSkin activityItemSkin = activityPaneSkin.getActivityItem();
                activityItemSkin.setToggleGroup(bottomGroup);
                activityItemSkin.bind(parent, bottomActivityPane);
                bottom.getChildren().add(activityItemSkin);
            }


            {
                List<ActivityItemSkin> activityItemSkins = top.activityItemSkins;
                if (config.activeMainBarTopItemIndex > -1 && config.activeMainBarTopItemIndex < activityItemSkins.size()) {
                    ActivityItemSkin activityItemSkin = activityItemSkins.get(config.activeMainBarTopItemIndex);
                    activityItemSkin.setSelected(true);
                    topActivityPane.showActivityPane(activityItemSkin.getActivityPane());
                    topActivityPane.setShow(true);
                }
            }
            {
                List<ActivityItemSkin> activityItemSkins = bottom.activityItemSkins;
                if (config.activeMainBarBottomItemIndex > -1 && config.activeMainBarBottomItemIndex < activityItemSkins.size()) {
                    ActivityItemSkin activityItemSkin = activityItemSkins.get(config.activeMainBarBottomItemIndex);
                    activityItemSkin.setSelected(true);
                    bottomActivityPane.showActivityPane(activityItemSkin.getActivityPane());
                    bottomActivityPane.setShow(true);
                }
            }


        }
    }

    private static class SecondActivityBar extends ActivityBar {
        private final ToggleGroup group = new ToggleGroup();
        ActivityBarPaneSkin activityPane;

        ShowActivityItemPane top = new ShowActivityItemPane(this, true) {
            {
                getStyleClass().add("top");
                setAlignment(Pos.TOP_CENTER);
                VBox.setVgrow(this, Priority.ALWAYS);
            }
        };

        public SecondActivityBar(ActivityPane parent) {
            super(parent);
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue instanceof ActivityItemSkin activityItemSkin) {
                    activityPane.showActivityPane(activityItemSkin.getActivityPane());
                    activityPane.setShow(true);
                    config.activeSecondBarItemIndex = top.activityItemSkins.indexOf(activityItemSkin);
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
                ActivityItemSkin activityItemSkin = parent.activityPaneMap.get(id).getActivityItem();
                activityItemSkin.setToggleGroup(group);
                activityItemSkin.bind(parent, activityPane);
                top.getChildren().add(activityItemSkin);
            }

            List<ActivityItemSkin> activityItemSkins = top.activityItemSkins;
            if (config.activeSecondBarItemIndex > -1 && config.activeSecondBarItemIndex < activityItemSkins.size()) {
                ActivityItemSkin activityItemSkin = activityItemSkins.get(config.activeSecondBarItemIndex);
                activityItemSkin.setSelected(true);
                activityPane.showActivityPane(activityItemSkin.getActivityPane());
                activityPane.setShow(true);
            }
        }
    }

    private static class ShowActivityItemPane extends VBox {
        ActivityBar activityBar;
        ActivityPane activityPane;
        ActivityBarPaneSkin activityBarPaneSkin;

        List<ActivityItemSkin> activityItemSkins = new ArrayList<>();
        boolean isTop;
        BooleanProperty style = new SimpleBooleanProperty(false);
        public ShowActivityItemPane(ActivityBar activityBar, boolean isTop) {
            this.activityBar = activityBar;
            activityPane = activityBar.parent;
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
                            if (node instanceof ActivityItemSkin activityItemSkin) {
                                activityItemSkins.add(activityItemSkin);
                            }
                        }
                    } else if (c.wasRemoved()) {
                        for (Node node : c.getRemoved()) {
                            if (node instanceof ActivityItemSkin activityItemSkin) {
                                activityItemSkins.remove(activityItemSkin);
                            }
                        }
                    }
                }
            });

            setOnDragOver(event -> {
                if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT))
                    return;
                if (activityPane.dragedActivityItemSkin == null)
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
                if (activityPane.dragedActivityItemSkin == null)
                    return;
                style.set(false);
                ActivityItemSkin activityItemSkin = activityPane.dragedActivityItemSkin;
                config.mainTopBarItems.remove(activityItemSkin.getId());
                config.secondBarItems.remove(activityItemSkin.getId());
                config.mainBottomBarItems.remove(activityItemSkin.getId());

                LinkedHashSet<String> items = activityBarPaneSkin.getItems();

                List<String> list = new ArrayList<>(items);
                list.add(activityItemSkin.getId());
                items.clear();
                items.addAll(list);

                activityPane.dragedActivityItemSkin = null;

                //立即更新
                config.save();

                event.setDropCompleted(true);
                event.consume();
            });
        }

        void setShowActivityPane(ActivityBarPaneSkin activityBarPaneSkin) {
            this.activityBarPaneSkin = activityBarPaneSkin;
        }
    }
}
