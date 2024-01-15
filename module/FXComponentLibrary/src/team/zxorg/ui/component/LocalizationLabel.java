package team.zxorg.ui.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

import java.text.MessageFormat;

public class LocalizationLabel extends Labeled {
    StringProperty label = new SimpleStringProperty();

    public LocalizationLabel(String id, Object... parameter) {

        //MessageFormat.format("", 10);
        getStyleClass().setAll("label");
        setAccessibleRole(AccessibleRole.TEXT);
    }
}
