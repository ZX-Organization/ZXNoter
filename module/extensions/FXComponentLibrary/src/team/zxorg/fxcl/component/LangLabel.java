package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Label;
import team.zxorg.fxcl.property.LangProperty;

public class LangLabel extends Label {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangLabel(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }

    public LangLabel(String key, Property<?>... args) {
        langTextProperty.setLang(key, args);
        textProperty().bind(langTextProperty);
    }
}
