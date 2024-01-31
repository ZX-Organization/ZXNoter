package team.zxorg.zxnoter.uiframe.component;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.uiframe.ZXNoterManager;
import team.zxorg.zxnoter.uiframe.factory.IconFactory;
import team.zxorg.zxnoter.uiframe.factory.MenuFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ActivityBar extends VBox {
    private static final HashMap<String, Class<? extends ActivityItem>> activityItemClassMap = new HashMap<>();
    private static final HashMap<Class<? extends ActivityItem>, Class<? extends SideBar>> sideBarClassMap = new HashMap<>();
    private static final ActivityConfig activityConfig;
    private static final String LANG = "zxnoterUiFrame.projectView.activityBar.";
    /**
     * 活动项配置菜单
     */
    private static final Menu activityItemConfigMenu = MenuFactory.getLangMenu(LANG + "menu.itemConfig");

    private HashMap<Class<? extends ActivityItem>, SideBar> sideBarMap = new HashMap<>();
    private ObjectProperty<SideBar> sideBar = new SimpleObjectProperty<>();

    static {
        activityConfig = ZXNoterManager.config.get(ActivityConfig.class);
        //更新活动项配置菜单
        ZXNoterManager.config.addEventListener(ActivityConfig.class, new ConfigEventListener() {
            @Override
            public void ConfigSaved() {
                updateConfigMenu();
            }
        });
        Platform.runLater(ActivityBar::updateConfigMenu);

    }

    private final VBox top = new VBox() {
        {
            setAlignment(Pos.TOP_CENTER);
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    };
    private final VBox bottom = new VBox() {
        {
            setAlignment(Pos.BOTTOM_CENTER);
        }
    };
    /**
     * 更新活动项的实例
     */
    private final ConfigEventListener updateConfig = new ConfigEventListener() {
        @Override
        public void ConfigSaved() {
            clearChildren(top);
            clearChildren(bottom);

            loadActivityItems(activityConfig.topItems, top);
            loadActivityItems(activityConfig.bottomItems, bottom);
        }

        private static void clearChildren(Pane pane) {
            pane.getChildren().clear();
        }

        private void loadActivityItems(List<String> items, Pane pane) {
            for (String id : items) {
                Class<? extends ActivityItem> clazz = activityItemClassMap.get(id);
                if (clazz != null) {
                    ActivityItem item = newInstance(clazz);
                    item.setOnAction(e -> {
                        if (e.getSource() instanceof ActivityItem activityItem) {
                            if (activityItem.isSelected()) {
                                sideBar.set(sideBarMap.get(activityItem.getClass()));
                            } else {
                                sideBar.set(null);
                            }
                        }
                    });
                    pane.getChildren().add(item);
                }
            }
        }
    };
    /**
     * 活动栏的上下文菜单
     */
    private final ContextMenu barContextMenu = new ContextMenu(activityItemConfigMenu);

    {
        //初始化
        getChildren().addAll(top, bottom);
        getStyleClass().addAll("activity-bar");


        //实例化所有侧边栏
        for (Map.Entry<Class<? extends ActivityItem>, Class<? extends SideBar>> entry : sideBarClassMap.entrySet()) {
            sideBarMap.put(entry.getKey(), newInstance(entry.getValue()));
        }

        //监听并初始化活动项
        ZXNoterManager.config.addEventListener(ActivityConfig.class, updateConfig);
        updateConfig.ConfigSaved();

        //设置右键活动栏的配置菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                barContextMenu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
        sideBar.addListener((observable, oldValue, newValue) -> {
            hideSideBar();
            if (newValue == null) {

            } else {
                showSideBar(sideBar.get());
            }
        });
    }

    /**
     * 实例化一个ActivityItem
     *
     * @param clazz 活动项类
     * @return 活动项实例
     */
    private static <T> T newInstance(Class<? extends T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为活动栏注册一个活动项 (请在javafx线程内调用)
     *
     * @param activityItemClass 活动项
     */
    public static void registerItem(Class<? extends ActivityItem> activityItemClass, Class<? extends SideBar> sideBarClass) {
        ActivityItem newActivityItem = newInstance(activityItemClass);
        if (activityItemClassMap.containsKey(newActivityItem.getItemId())) {
            Logger.warning("ActivityItem " + newActivityItem.getItemId() + " has been registered");
            return;
        }
        activityItemClassMap.put(newActivityItem.getItemId(), activityItemClass);
        sideBarClassMap.put(activityItemClass, sideBarClass);
        LangCheckMenuItem item = getLangCheckMenuItem(newActivityItem);
        activityItemConfigMenu.getItems().add(item);
    }

    /**
     * 活动项的上下文菜单
     */
    public static final ContextMenu activityItemContextMenu = new ContextMenu() {
        {
            LangMenuItem hideMenuItem = MenuFactory.getLangMenuItem(LANG + "menu.hideItem");
            hideMenuItem.setOnAction(event -> {
                String id = activityItemContextMenu.getId();
                activityConfig.topItems.remove(id);
                activityConfig.bottomItems.remove(id);
                ZXNoterManager.config.save(ActivityConfig.class);
            });
            getItems().addAll(hideMenuItem, activityItemConfigMenu);
        }
    };

    private static LangCheckMenuItem getLangCheckMenuItem(ActivityItem newActivityItem) {
        LangCheckMenuItem item = new LangCheckMenuItem(IconFactory.getMenuIcon(newActivityItem.getIconKey()), newActivityItem.getNameKey());
        item.setId(newActivityItem.getItemId());
        item.setOnAction(event -> {
            String id = item.getId();
            if (item.isSelected()) {
                activityConfig.topItems.add(id);
            } else {
                activityConfig.topItems.remove(id);
                activityConfig.bottomItems.remove(id);
            }

            ZXNoterManager.config.save(ActivityConfig.class);
        });
        return item;
    }

    /**
     * 更新配置菜单
     */
    private static void updateConfigMenu() {
        for (MenuItem menu : activityItemConfigMenu.getItems()) {
            if (menu instanceof CheckMenuItem checkMenuItem) {
                String id = checkMenuItem.getId();
                checkMenuItem.setSelected(activityConfig.topItems.contains(id) || activityConfig.bottomItems.contains(id));
            }
        }
    }

    public static class ActivityConfig {
        List<String> topItems;
        List<String> bottomItems;
        String openedItem;

        public ActivityConfig() {
            topItems = new ArrayList<>();
            bottomItems = new ArrayList<>();
        }
    }

    protected abstract void showSideBar(SideBar sideBar);

    protected abstract void hideSideBar();
}
