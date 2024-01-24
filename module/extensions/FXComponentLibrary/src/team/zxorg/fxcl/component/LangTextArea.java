package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.control.TextArea;
import team.zxorg.fxcl.property.LangProperty;

public class LangTextArea extends TextArea {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langPromptTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangProperty langPromptTextProperty() {
        return langPromptTextProperty;
    }

    public LangTextArea() {
        textProperty().bind(langTextProperty);
        promptTextProperty().bind(langPromptTextProperty);
    }

    public LangTextArea(String key, Property<?>... args) {
        this();
        langPromptTextProperty.setLang(key, args);
    }
}
