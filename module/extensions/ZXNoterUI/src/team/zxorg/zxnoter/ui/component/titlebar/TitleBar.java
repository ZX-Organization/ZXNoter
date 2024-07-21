package team.zxorg.zxnoter.ui.component.titlebar;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.LangLabel;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.ProjectViewConfig;
import team.zxorg.zxnoter.ui.ZXNoter;
import team.zxorg.zxnoter.ui.factory.AlertFactory;
import team.zxorg.zxnoter.ui.factory.MenuFactory;

import java.nio.file.Path;
import java.util.LinkedHashMap;

/**
 * 标题栏定义
 */
public final class TitleBar extends HBox {

    private static final LinkedHashMap<String, Class<? extends LangMenuItem>> fileMenuClassMap = new LinkedHashMap<>();
    private static final String LANG = "zxnoterUiFrame.projectView.titleBar.";
    private static final ProjectViewConfig config = ZXNoter.config.get(ProjectViewConfig.class);

    private final LangMenu newMenu = MenuFactory.getMenu(LANG + "menu.file.new");
    private final LangMenuItem openProjectMenu = MenuFactory.getMenuItem(LANG + "menu.file.openProject");
    private final LangMenuItem openFileMenu = MenuFactory.getMenuItem(LANG + "menu.file.openFile");
    //最近菜单
    private final LangMenu recentMenu = MenuFactory.getMenu(LANG + "menu.file.recent");
    private final LangMenuItem clearRecentMenu = MenuFactory.getMenuItem(LANG + "menu.file.recent.clear");
    //文件菜单
    private final LangMenu fileMenu = MenuFactory.getMenu(LANG + "menu.file", newMenu, openProjectMenu, openFileMenu, recentMenu, new SeparatorMenuItem());

    //编辑菜单
    private final LangMenu editMenu = MenuFactory.getMenu(LANG + "menu.edit");


    private final ProjectView projectView;
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
        menuBar.getMenus().addAll(fileMenu, editMenu);

        getChildren().addAll(titleIcon, menuBar, titleLabel);
        getStyleClass().addAll("title-bar");
    }

    public TitleBar(ProjectView projectView) {
        this.projectView = projectView;

        //初始化一堆东西

        openFileMenu.setOnAction(e -> projectView.openFileChooser());
        clearRecentMenu.setOnAction(e -> clearRecent());
        updateRecent();
    }

    /**
     * 更新最近打开文件
     */
    public void updateRecent() {
        ObservableList<MenuItem> items = recentMenu.getItems();
        MenuItem item;
        items.clear();
        for (var s : config.recentlyOpenedProject) {
            item = new MenuItem(s);
            item.setOnAction(e -> {

            });
            items.add(item);
        }
        items.add(new SeparatorMenuItem());
        for (var s : config.recentlyOpenedFile) {
            item = new MenuItem(s);
            item.setOnAction(e -> {
                projectView.openFile(Path.of(s));
            });
            items.add(item);
        }
        items.add(new SeparatorMenuItem());
        items.add(clearRecentMenu);
    }

    /**
     * 清除最近文件
     */
    private void clearRecent() {
        Alert alert = AlertFactory.getAlert(Alert.AlertType.WARNING, LANG + "menu.file.recent.clear.alert", ButtonType.CANCEL);

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            config.recentlyOpenedFile.clear();
            config.recentlyOpenedProject.clear();
            config.needSave();
            updateRecent();
        }
    }

}