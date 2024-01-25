package team.zxorg.fxcl.component.menu;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import team.zxorg.fxcl.property.LangProperty;

public class LangMenu extends Menu {
    LangProperty langTextProperty = new LangProperty();

    public LangProperty langTextProperty() {
        return langTextProperty;
    }

    public LangMenu() {
        textProperty().bind(langTextProperty);
    }

    public LangMenu(String key, Property<?>... args) {
        this();
        langTextProperty.setLang(key, args);
    }

    public LangMenu(Node graphic, String key, Property<?>... args) {
        this(key, args);
        setGraphic(graphic);
    }

    public LangMenu(Node graphic, String key, MenuItem... items) {
        this();
        langTextProperty.setLang(key);
        setGraphic(graphic);
        getItems().addAll(items);
    }
}
