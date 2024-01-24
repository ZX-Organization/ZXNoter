package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import team.zxorg.fxcl.property.LangProperty;

public class LangButton extends Button {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty getLangTextProperty() {
        return langTextProperty;
    }

    public LangButton(Icon icon) {
        this();
        setGraphic(icon);
    }

    public LangButton(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }

    public LangButton() {
        textProperty().bind(langTextProperty);
    }

    public LangButton(String key, Property<?>... args) {
        this();
        setLang(key, args);
    }

    public void setLang(String key, Property<?>... args) {
        langTextProperty.setLang(key, args);
    }
}
