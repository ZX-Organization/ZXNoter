package team.zxorg.fxcl.component;

import javafx.beans.property.Property;
import javafx.scene.control.Alert;
import team.zxorg.fxcl.property.LangProperty;

public class LangAlert extends Alert {
    LangProperty langTitleProperty = new LangProperty();
    LangProperty langContentProperty = new LangProperty();
    LangProperty langHeaderProperty = new LangProperty();

    public LangAlert(AlertType alertType) {
        super(alertType);
        titleProperty().bind(langTitleProperty);
        contentTextProperty().bind(langContentProperty);
        headerTextProperty().bind(langHeaderProperty);
    }


    public void setTitleLang(Object key, Property<?>... args) {
        langTitleProperty.setLang(key, args);
    }

    public void setContentLang(Object key, Property<?>... args) {
        langContentProperty.setLang(key, args);
    }
    public void setHeaderLang(Object key, Property<?>... args) {
        langHeaderProperty.setLang(key, args);
    }

    public void setIcon(Icon icon) {
        setGraphic(icon);
    }
}
