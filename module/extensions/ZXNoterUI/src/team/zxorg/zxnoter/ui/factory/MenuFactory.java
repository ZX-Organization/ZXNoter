package team.zxorg.zxnoter.ui.factory;

import javafx.beans.property.Property;
import javafx.scene.control.MenuItem;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangCheckMenuItem;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;

public class MenuFactory {
    public static LangMenu getMenu(String key, Property<?>... args) {
        String langKey = (Language.getOrNull(key) == null ? key + ".name" : key);
        LangMenu langMenu = new LangMenu(langKey, args);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }
    public static LangMenu getMenu(String key) {
        String langKey = (Language.getOrNull(key) == null ? key + ".name" : key);
        LangMenu langMenu = new LangMenu(langKey);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }

    public static LangMenu getMenu(String key, MenuItem... items) {
        LangMenu menu = getMenu(key);
        menu.getItems().addAll(items);
        return menu;
    }


    public static LangMenuItem getMenuItem(String key, Property<?>... args) {
        String langKey = (Language.getOrNull(key) == null ? key + ".name" : key);
        LangMenuItem langMenu = new LangMenuItem(langKey, args);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }


    public static LangCheckMenuItem getCheckMenuItem(String key, Property<?>... args) {
        String langKey = (Language.getOrNull(key) == null ? key + ".name" : key);
        LangCheckMenuItem langMenu = new LangCheckMenuItem(langKey, args);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }


}
