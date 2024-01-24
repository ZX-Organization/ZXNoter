package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.SplitMenuButton;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.fxcl.property.LangProperty;

public class LangSplitMenuButton extends SplitMenuButton {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langEllipsisStringProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }


    public LangProperty langEllipsisStringProperty() {
        return langEllipsisStringProperty;
    }

    public LangSplitMenuButton() {
        textProperty().bind(langTextProperty);
        ellipsisStringProperty().bind(langEllipsisStringProperty);
        langEllipsisStringProperty.setLang(LanguageKey.COMMON_ELLIPSIS);
    }

    public LangSplitMenuButton(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }

    public LangSplitMenuButton(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }
}
