package team.zxorg.fxcl.component.menu;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import team.zxorg.fxcl.property.LangProperty;

public class LangMenuItem extends MenuItem {
    LangProperty langTextProperty = new LangProperty();
    public void setLang(String key, Property<?>... args){
        langTextProperty.setLang(key, args);
    }
    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangMenuItem() {
        textProperty().bind(langTextProperty);
    }

    public LangMenuItem(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }

    public LangMenuItem(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }
}
