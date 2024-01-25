package team.zxorg.fxcl.component.menu;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.RadioMenuItem;
import team.zxorg.fxcl.property.LangProperty;

public class LangRadioMenuItem extends RadioMenuItem {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangRadioMenuItem() {
        textProperty().bind(langTextProperty);
    }

    public LangRadioMenuItem(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }

    public LangRadioMenuItem(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }
}
