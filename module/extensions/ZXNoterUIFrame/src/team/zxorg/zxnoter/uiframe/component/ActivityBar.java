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
import team.zxorg.extensionloader.core.Language;
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
    /**
     * 当前所有被注册的侧边栏类
     */
    private static final HashMap<String, Class<? extends SideBar>> sideBarClassMap = new HashMap<>();
    /**
     * 当前活动栏配置
     */
    private static final ActivityBarConfig ACTIVITY_BAR_CONFIG;
    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.";
    /**
     * 活动项配置菜单
     */
    private static final Menu activityItemConfigMenu = MenuFactory.getLangMenu(LANG + "menu.itemConfig");
    /**
     * 活动栏当前侧边栏表
     */
    private final HashMap<String, SideBar> sideBarMap = new HashMap<>();
    /**
     * 当前显示的侧边栏
     */
    private final ObjectProperty<SideBar> showedSideBar = new SimpleObjectProperty<>();

    static {
        ACTIVITY_BAR_CONFIG = ZXNoterManager.config.get(ActivityBarConfig.class);
        //更新活动项配置菜单
        ZXNoterManager.config.addEventListener(ActivityBarConfig.class, new ConfigEventListener() {
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

            loadActivityItems(ACTIVITY_BAR_CONFIG.topItems, top);
            loadActivityItems(ACTIVITY_BAR_CONFIG.bottomItems, bottom);
        }

        private static void clearChildren(Pane pane) {
            pane.getChildren().clear();
        }

        private void loadActivityItems(List<String> items, Pane pane) {
            for (String id : items) {
                SideBar sideBar = sideBarMap.get(id);
                if (sideBar != null) {
                    pane.getChildren().add(sideBar.activityItem);
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
        for (Map.Entry<String, Class<? extends SideBar>> entry : sideBarClassMap.entrySet()) {
            SideBar sideBar = newInstance(entry.getValue());
            sideBarMap.put(entry.getKey(), sideBar);

            ActivityItem item = sideBar.activityItem;
            item.setOnAction(e -> {
                if (e.getSource() instanceof ActivityItem activityItem) {
                    if (activityItem.isSelected()) {
                        showedSideBar.set(sideBarMap.get(activityItem.getId()));
                    } else {
                        showedSideBar.set(null);
                    }
                }
            });

        }

        //监听并初始化活动项
        ZXNoterManager.config.addEventListener(ActivityBarConfig.class, updateConfig);
        updateConfig.ConfigSaved();

        //设置右键活动栏的配置菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                barContextMenu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
        showedSideBar.addListener((observable, oldValue, newValue) -> {
            hideSideBar();
            if (newValue == null) {

            } else {
                showSideBar(showedSideBar.get());
            }
        });
    }

    /**
     * 实例化类
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
     * 为活动栏注册一个活动项 (在javafx线程内调用)
     *
     * @param id           侧边栏id
     * @param sideBarClass 被注册的侧边栏
     */
    public static void registerItem(String id, Class<? extends SideBar> sideBarClass) {
        if (sideBarClassMap.containsKey(id)) {
            Logger.warning("SideBar " + id + " has been registered");
            return;
        }
        sideBarClassMap.put(id, sideBarClass);
        LangCheckMenuItem item = getLangCheckMenuItem(id);
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
                ACTIVITY_BAR_CONFIG.topItems.remove(id);
                ACTIVITY_BAR_CONFIG.bottomItems.remove(id);
                ZXNoterManager.config.save(ActivityBarConfig.class);
            });
            getItems().addAll(hideMenuItem, activityItemConfigMenu);
        }
    };

    private static LangCheckMenuItem getLangCheckMenuItem(String id) {
        LangCheckMenuItem item = new LangCheckMenuItem(IconFactory.getMenuIcon(Language.get(LANG + "item." + id + ".icon")), LANG + "item."+ id + ".name");
        item.setId(id);
        item.setOnAction(event -> {
            if (item.isSelected()) {
                ACTIVITY_BAR_CONFIG.topItems.add(id);
            } else {
                ACTIVITY_BAR_CONFIG.topItems.remove(id);
                ACTIVITY_BAR_CONFIG.bottomItems.remove(id);
            }
            ZXNoterManager.config.save(ActivityBarConfig.class);
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
                checkMenuItem.setSelected(ACTIVITY_BAR_CONFIG.topItems.contains(id) || ACTIVITY_BAR_CONFIG.bottomItems.contains(id));
            }
        }
    }

    public static class ActivityBarConfig {
        List<String> topItems;
        List<String> bottomItems;
        String openedItem;

        public ActivityBarConfig() {
            topItems = new ArrayList<>();
            bottomItems = new ArrayList<>();
        }
    }

    protected abstract void showSideBar(SideBar sideBar);

    protected abstract void hideSideBar();
}
