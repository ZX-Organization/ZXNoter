package team.zxorg.zxnoter.uiframe.component;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.event.ConfigEventListener;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.uiframe.ZXNoterManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityBar extends VBox {
    private static final HashMap<String, Class<? extends ActivityItem>> activityItemClassMap = new HashMap<>();
    private static final ActivityConfig activityConfig;
    private static final String LANG = "zxnoterUiFrame.projectView.activityBar.";
    /**
     * 活动项配置菜单
     */
    private static final Menu activityItemConfigMenu = new LangMenu(new Icon(Language.get(LANG + "menu.itemConfigIcon"), 16),LANG + "menu.itemConfig");

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
            top.getChildren().clear();
            bottom.getChildren().clear();


            //读取并载入顶部
            for (String id : activityConfig.topItems) {
                Class<? extends ActivityItem> clazz = activityItemClassMap.get(id);
                if (clazz != null) {
                    ActivityItem item = newInstanceActivityItem(clazz);
                    top.getChildren().add(item);
                }
            }

            for (String id : activityConfig.bottomItems) {
                Class<? extends ActivityItem> clazz = activityItemClassMap.get(id);
                if (clazz != null) {
                    ActivityItem item = newInstanceActivityItem(clazz);
                    bottom.getChildren().add(item);
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

        //监听并初始化活动项
        ZXNoterManager.config.addEventListener(ActivityConfig.class, updateConfig);
        updateConfig.ConfigSaved();

        //设置右键活动栏的配置菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                barContextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });
    }

    /**
     * 实例化一个ActivityItem
     *
     * @param activityItemClass 活动项类
     * @return 活动项实例
     */
    private static ActivityItem newInstanceActivityItem(Class<? extends ActivityItem> activityItemClass) {
        try {
            return activityItemClass.getDeclaredConstructor().newInstance();
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
    public static void registerItem(Class<? extends ActivityItem> activityItemClass) {
        ActivityItem newActivityItem = newInstanceActivityItem(activityItemClass);
        if (activityItemClassMap.containsKey(newActivityItem.getItemId())) {
            Logger.warning("ActivityItem " + newActivityItem.getItemId() + " has been registered");
            return;
        }
        activityItemClassMap.put(newActivityItem.getItemId(), activityItemClass);
        LangCheckMenuItem item = getLangCheckMenuItem(newActivityItem);
        activityItemConfigMenu.getItems().add(item);
    }

    /**
     * 活动项的上下文菜单
     */
    public static final ContextMenu activityItemContextMenu = new ContextMenu() {
        {
            LangMenuItem hideMenuItem = new LangMenuItem(new Icon(Language.get(LANG + "menu.hideItemIcon"), 16), LANG + "menu.hideItem");
            hideMenuItem.setOnAction(event -> {
                String id = activityItemContextMenu.getId();
                activityConfig.topItems.remove(id);
                activityConfig.bottomItems.remove(id);
                ZXNoterManager.config.save(ActivityConfig.class);
            });
            getItems().addAll(hideMenuItem, activityItemConfigMenu);
        }
    };

    @NotNull
    private static LangCheckMenuItem getLangCheckMenuItem(ActivityItem newActivityItem) {
        LangCheckMenuItem item = new LangCheckMenuItem(new Icon(newActivityItem.icon.getIconKey(), 16), LANG + "item." + newActivityItem.getItemId());
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


}
