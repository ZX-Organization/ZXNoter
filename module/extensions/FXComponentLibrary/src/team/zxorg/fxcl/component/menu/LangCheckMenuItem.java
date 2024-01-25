package team.zxorg.fxcl.component.menu;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import team.zxorg.fxcl.property.LangProperty;

public class LangCheckMenuItem extends CheckMenuItem {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangCheckMenuItem() {
        textProperty().bind(langTextProperty);
    }

    public LangCheckMenuItem(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }

    public LangCheckMenuItem(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }
}
