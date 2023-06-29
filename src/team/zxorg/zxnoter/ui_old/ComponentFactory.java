package team.zxorg.zxnoter.ui_old;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow;
import javafx.util.Duration;
import team.zxorg.zxnoter.resource.ZXResources;

public class ComponentFactory {
    public static Tooltip getTooltip(String tip) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        return tooltip;
    }

    public static Tooltip getTooltip(String tip, String iconKey) {
        Tooltip tooltip = new Tooltip(tip);
        tooltip.setGraphic(ZXResources.getSvgPane(iconKey, 16, Color.web("#C8C8C8")));
        return tooltip;
    }
}
