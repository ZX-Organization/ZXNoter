package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;
import team.zxorg.fxcl.property.LangProperty;


public class LangTextField extends TextField {
    LangProperty langTextProperty = new LangProperty();
    LangProperty langPromptTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangProperty langPromptTextProperty() {
        return langPromptTextProperty;
    }

    public LangTextField() {
        textProperty().bind(langTextProperty);
        promptTextProperty().bind(langPromptTextProperty);
    }

    public LangTextField(String key, Property<?>... args) {
        this();
        langPromptTextProperty.setLang(key, args);
    }
}
