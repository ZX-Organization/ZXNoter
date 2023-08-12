package team.zxorg.zxnoter.ui.component;

import javafx.scene.control.Menu;
import team.zxorg.zxnoter.resource.GlobalResources;

public class ZXMenu extends Menu {
    public ZXMenu(String languageKey) {
        textProperty().bind(GlobalResources.getLanguageContent(languageKey));
    }

    public ZXMenu(String languageKey, ZXIcon zxIcon) {
        textProperty().bind(GlobalResources.getLanguageContent(languageKey));
        setGraphic(zxIcon);
    }

}
