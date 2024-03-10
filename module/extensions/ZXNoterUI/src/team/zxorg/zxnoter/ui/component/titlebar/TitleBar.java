package team.zxorg.zxnoter.ui.component.titlebar;

import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.LangLabel;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.ZXNoter;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityBarConfig;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPaneSkin;
import team.zxorg.zxnoter.ui.factory.MenuFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 标题栏定义
 */
public final class TitleBar extends HBox {

    private static final LinkedHashMap<String, Class<? extends LangMenuItem>> activityMenuClassMap = new LinkedHashMap<>();
    private static final TitleBarConfig config = ZXNoter.config.get(TitleBarConfig.class);

    public static void register(Class<? extends LangMenuItem> activityMenuClass) {
        LangMenuItem activityPaneSkin;
        try {
            activityPaneSkin = activityMenuClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            Logger.warning("注册失败: " + activityMenuClass.getSimpleName());
            return;
        }

        activityMenuClassMap.put(activityPaneSkin.getId(), activityMenuClass);
        config.save();
    }

    /**
     * 注册一个菜单
     *
     * @param menuId 菜单id
     * @param sort   菜单排序 越小越靠前
     */
    public static void registerMenu(String path, int sort, String group) {

    }

    /**
     * 注册一个菜单项目
     *
     * @param parentId 菜单id
     * @param itemId   菜单id
     * @param sort     菜单排序 越小越靠前
     * @param group    菜单组名
     */
    public static void registerMenuItem(String path, int sort, String group) {

    }

    private final ProjectView projectView;

    public TitleBar(ProjectView projectView) {
        this.projectView = projectView;
    }

    private static final String LANG = "zxnoterUiFrame.projectView.titleBar.";
    private final Icon titleIcon = new Icon(Language.get(LANG + "icon"), 22) {
        {
            getStyleClass().add("title-icon");
        }
    };
    private final MenuBar menuBar = new MenuBar();
    private final LangLabel titleLabel = new LangLabel(LANG + "title") {
        {
            HBox.setHgrow(this, Priority.ALWAYS);
            setMaxWidth(Double.MAX_VALUE);
            setAlignment(Pos.CENTER);
        }
    };

    {
        Menu menu = new Menu();
        menu.getItems().addAll(new Menu());

        getChildren().addAll(titleIcon, menuBar, titleLabel);
        getStyleClass().addAll("title-bar");
    }


}