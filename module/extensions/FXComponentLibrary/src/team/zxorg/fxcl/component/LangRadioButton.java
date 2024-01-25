package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.fxcl.property.LangProperty;

public class LangRadioButton extends RadioButton {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langEllipsisStringProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangProperty langEllipsisStringProperty() {
        return langEllipsisStringProperty;
    }

    public LangRadioButton() {
        textProperty().bind(langTextProperty);
        ellipsisStringProperty().bind(langEllipsisStringProperty);
        langEllipsisStringProperty.setLang(LanguageKey.COMMON_ELLIPSIS);
    }

    public LangRadioButton(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }
    public LangRadioButton(Node graphic, String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
        setGraphic(graphic);
    }
}
