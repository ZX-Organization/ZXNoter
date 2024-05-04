package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;


public class ZXIconButton extends Button {
    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();

    public ZXIconButton() {
    }

    public ZXIconButton(String iconKey, double size) {
        setIconKey(iconKey);
        setSize(size);
        setColor(ZXColor.FONT_USUALLY);
    }

    public ZXIconButton(String iconKey, double size, String tipLangKey) {
        setIconKey(iconKey);
        setSize(size);
        setColor(ZXColor.FONT_USUALLY);
        new ZXTooltip(this, tipLangKey);
    }


    public StringProperty iconKey = new SimpleStringProperty();

    {
        setFocused(false);
        getStyleClass().add("icon-button");
        iconKey.addListener((observable, oldValue, newValue) -> {
            shapeProperty().bind(GlobalResources.getIcon(newValue));
        });
        color.addListener((observable, oldValue, newValue) -> {
            getStyleClass().removeAll(getStyleClass().filtered(s -> s.contains("icon-button-")));
            getStyleClass().add("icon-button-" + newValue);
        });
    }

    public void setSize(double size) {
        setPrefSize(size, size);
        setMaxSize(size, size);
        setMinSize(size, size);
    }

    public void setIconKey(String key) {
        iconKey.set(key);
    }

    public void setColor(ZXColor zxColor) {
        color.set(zxColor);
    }
}