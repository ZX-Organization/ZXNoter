package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;

public class ZXLabel extends Label {
    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    public StringProperty langKey = new SimpleStringProperty();

    {
        langKey.addListener((observable, oldValue, newValue) -> {
            textProperty().bind(GlobalResources.getLanguageContent(newValue));
        });
        color.addListener((observable, oldValue, newValue) -> {
            getStyleClass().filtered(s -> s.contains("text-")).clear();
            getStyleClass().add("text-" + newValue);
        });
    }

    public ZXLabel(String langKey, ZXColor color) {
        setColor(color);
        setLangKey(langKey);
    }

    public ZXLabel(String langKey) {
        setColor(ZXColor.FONT_USUALLY);
        setLangKey(langKey);
    }

    public void setColor(ZXColor zxColor) {
        color.set(zxColor);
    }

    public void setLangKey(String langKey) {
        this.langKey.set(langKey);
    }
}
