package team.zxorg.zxnoter.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow;
import team.zxorg.zxnoter.resource_old.ZXResources;

public class ComponentFactory {
    public static String languageCode = "zh_cn";

    public static Tooltip getTooltip(String tip) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
        return tooltip;
    }

    public static Tooltip getTooltip(String tip, String iconKey) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.setGraphic(ZXResources.getSvgPane(iconKey, 16, Color.web("#C8C8C8")));
        return tooltip;
    }

    public static Menu menu(String key) {
        return new Menu(getLanguageContent(key));
    }

    public static MenuItem menuItem(String key) {
        return new MenuItem(getLanguageContent(key));
    }

    public static String getLanguageContent(String key) {
        return ZXResources.getLanguageContent(key, languageCode);
    }
}
