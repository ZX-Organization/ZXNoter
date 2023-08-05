package team.zxorg.zxnoter.ui.component;

import javafx.scene.control.MenuItem;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;

public class ZXMenuItem extends MenuItem {
    public ZXMenuItem(String languageKey) {
        textProperty().bind(GlobalResources.getLanguageContent(languageKey));
    }
    public ZXMenuItem(String languageKey, String iconKey, ZXColor zxColor) {
        textProperty().bind(GlobalResources.getLanguageContent(languageKey));

    }
}
