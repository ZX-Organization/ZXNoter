package team.zxorg.zxnoter.ui.component;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import team.zxorg.zxnoter.resource.GlobalResources;

public class ComponentFactory {
    public static Tooltip getTooltip(Node bindNode, double offset) {
        Tooltip tooltip = new Tooltip();
        Tooltip.install(bindNode, tooltip);

        tooltip.setOnShown(event -> {
            Bounds bounds = bindNode.localToScreen(bindNode.getBoundsInLocal());
            tooltip.setX(bounds.getMaxX() + offset);
            tooltip.setY(bounds.getCenterY() - tooltip.getHeight() / 2);
        });

        return tooltip;
    }

    /*public static Tooltip getTooltip(String languageKey, Node bindNode, double offset) {
        Tooltip tooltip = getTooltip(bindNode, offset);
        tooltip.setText(ZXResources.getLanguageContent(languageKey));
        return tooltip;
    }*/

    public static Tooltip getRealTimeTooltip(Node bindNode, double offset) {
        Tooltip tooltip = getTooltip(bindNode, offset);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        return tooltip;
    }

    /*public static Tooltip getRealTimeTooltip(String languageKey, Node bindNode, double offset) {
        Tooltip tooltip = getTooltip(languageKey, bindNode, offset);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        return tooltip;
    }*/


    public static Button Button(String languageKey) {
        Button button = new Button();
        button.textProperty().bind(GlobalResources.getLanguageContent(languageKey));
        return button;
    }

    /*public static Button getIconButton(String iconKey, double size) {
        Button button = new Button();
        button.setShape(ZXResources.getIconPane(iconKey));
        button.getStyleClass().add("icon-button");
        button.setPrefSize(size, size);
        button.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        return button;
    }*/

    public static Menu menu(String languageKey) {
        Menu menu = new Menu();
        menu.textProperty().bind(GlobalResources.getLanguageContent(languageKey));
        return menu;
    }

    public static MenuItem menuItem(String languageKey) {
        MenuItem menuItem = new MenuItem();
        menuItem.textProperty().bind(GlobalResources.getLanguageContent(languageKey));
        return menuItem;
    }


}
