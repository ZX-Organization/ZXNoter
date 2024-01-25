package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.fxcl.property.LangProperty;

public class LangToggleButton extends ToggleButton {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langEllipsisStringProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }


    public LangProperty langEllipsisStringProperty() {
        return langEllipsisStringProperty;
    }

    public LangToggleButton() {
        textProperty().bind(langTextProperty);
        ellipsisStringProperty().bind(langEllipsisStringProperty);
        langEllipsisStringProperty.setLang(LanguageKey.COMMON_ELLIPSIS);
    }

    public LangToggleButton(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }
    public LangToggleButton(Node graphic) {
        this();
        setGraphic(graphic);
    }

    public LangToggleButton(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }
}
