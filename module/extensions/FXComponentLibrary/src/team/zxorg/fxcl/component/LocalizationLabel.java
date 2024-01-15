package team.zxorg.fxcl.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Labeled;

public class LocalizationLabel extends Labeled {
    StringProperty label = new SimpleStringProperty();

    public LocalizationLabel(String id, Object... parameter) {

        //MessageFormat.format("", 10);
        getStyleClass().setAll("label");
        setAccessibleRole(AccessibleRole.TEXT);
    }
}
