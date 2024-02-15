package team.zxorg.zxnoter.uiframe.factory;

import javafx.beans.property.Property;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangMenu;
import team.zxorg.fxcl.component.menu.LangMenuItem;

public class MenuFactory {
    public static LangMenu getLangMenu(String key, Property<?>... args) {
        String langKey = (Language.getOrNull(key) == null ? key + ".title" : key);
        LangMenu langMenu = new LangMenu(langKey, args);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }


    public static LangMenuItem getLangMenuItem(String key, Property<?>... args) {
        String langKey = (Language.getOrNull(key) == null ? key + ".title" : key);
        LangMenuItem langMenu = new LangMenuItem(langKey, args);
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey != null)
            langMenu.setGraphic(new Icon(iconKey));
        return langMenu;
    }


}
