package team.zxorg.zxnoter.uiframe.component;

import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.fxcl.component.menu.LangRadioMenuItem;
import team.zxorg.zxnoter.uiframe.ZXNoterManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityBar extends VBox {
    private static final ToggleGroup GROUP = new ToggleGroup();
    private static final HashMap<String, Class<? extends ActivityItem>> activityItems = new HashMap<>();

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
        if (activityItems.containsKey(newActivityItem.getItemId())) {
            Logger.warning("ActivityItem " + newActivityItem.getItemId() + " has been registered");
            return;
        }
        activityItems.put(newActivityItem.getItemId(), activityItemClass);
        LangCheckMenuItem item=new LangCheckMenuItem(new Icon(newActivityItem.icon.getIconKey(),16),LANG+"item."+newActivityItem.getItemId());
        ActivityItemMenu.getItems().add(item);

    }

    ActivityConfig activityConfig;

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

    {
        //初始化
        getChildren().addAll(top, bottom);
        getStyleClass().addAll("activity-bar");
        activityConfig = ZXNoterManager.config.get(ActivityConfig.class);

        for (Class<? extends ActivityItem> activityItem : activityItems.values()) {
            ActivityItem item = newInstanceActivityItem(activityItem);
            top.getChildren().add(item);
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
    private static final String LANG = "zxnoterUiFrame.projectView.activityBar.";

    public static Menu ActivityItemMenu = new LangMenu(LANG + "menu.showItems");
    public static ContextMenu ActivityItemContextMenu = new ContextMenu() {
        {
            getItems().addAll(new LangMenuItem(LANG + "menu.hideItem"), ActivityItemMenu);
        }
    };
}
