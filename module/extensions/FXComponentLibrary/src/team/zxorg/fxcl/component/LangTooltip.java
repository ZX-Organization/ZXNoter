package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.control.Tooltip;
import team.zxorg.fxcl.property.LangProperty;

public class LangTooltip extends Tooltip {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }


    public LangTooltip() {
        textProperty().bind(langTextProperty);
    }

    public LangTooltip(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }
}
