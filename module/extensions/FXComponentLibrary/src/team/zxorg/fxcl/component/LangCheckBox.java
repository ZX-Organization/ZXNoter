package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.fxcl.property.LangProperty;

public class LangCheckBox extends CheckBox {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langEllipsisStringProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }


    public LangProperty langEllipsisStringProperty() {
        return langEllipsisStringProperty;
    }

    public LangCheckBox() {
        textProperty().bind(langTextProperty);
        ellipsisStringProperty().bind(langEllipsisStringProperty);
        langEllipsisStringProperty.setLang(LanguageKey.COMMON_ELLIPSIS);
    }

    public LangCheckBox(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }
}
