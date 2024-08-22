package team.zxorg.zxnoter.ui.factory;

import javafx.beans.property.Property;
import javafx.scene.control.MenuItem;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;

import static team.zxorg.zxnoter.ui.factory.FactoryUtils.getIcon;
import static team.zxorg.zxnoter.ui.factory.FactoryUtils.getKey;

public class MenuFactory {
    private static final double DEFAULT_ICON_SIZE = 16;

    public static LangMenu getMenu(String key, Property<?>... args) {
        LangMenu langMenu = new LangMenu(getKey(key), args);
        Icon icon = getIcon(key, DEFAULT_ICON_SIZE);
        if (icon != null)
            langMenu.setGraphic(icon);
        return langMenu;
    }

    public static LangMenu getMenu(String key) {
        LangMenu langMenu = new LangMenu(getKey(key));
        Icon icon = getIcon(key, DEFAULT_ICON_SIZE);
        if (icon != null)
            langMenu.setGraphic(icon);
        return langMenu;
    }

    public static LangMenu getMenu(String key, MenuItem... items) {
        LangMenu menu = getMenu(key);
        menu.getItems().addAll(items);
        return menu;
    }


    public static LangMenuItem getMenuItem(String key, Property<?>... args) {
        LangMenuItem langMenu = new LangMenuItem(getKey(key), args);
        Icon icon = getIcon(key, DEFAULT_ICON_SIZE);
        if (icon != null)
            langMenu.setGraphic(icon);
        return langMenu;
    }


    public static LangCheckMenuItem getCheckMenuItem(String key, Property<?>... args) {
        LangCheckMenuItem langMenu = new LangCheckMenuItem(getKey(key), args);
        Icon icon = getIcon(key, DEFAULT_ICON_SIZE);
        if (icon != null)
            langMenu.setGraphic(icon);
        return langMenu;
    }


}
