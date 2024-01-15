package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.resource.ZXColor;

public class ZXTextFieldGroup extends ZXGroupComponent {
    public ZXTextField textField;
    public ZXIcon icon = new ZXIcon();

    public ZXTextFieldGroup(String promptTextKey, String iconKey) {
        textField = new ZXTextField();
        textField.setPromptTextKey(promptTextKey);
        HBox.setHgrow(textField, Priority.ALWAYS);
        icon.setColor(ZXColor.FONT_USUALLY);
        icon.setSize(22);
        icon.setIconKey(iconKey);
        ZXIconButton clearButton = new ZXIconButton();
        clearButton.setColor(ZXColor.FONT_USUALLY);
        clearButton.setIconKey("system.close");
        clearButton.setSize(22);
        clearButton.setOnAction((event) -> textField.clear());
        clearButton.setVisible(false);
        getChildren().addAll(icon, textField, clearButton);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            clearButton.setVisible(newValue.length() != 0);
        });
    }

    public String getText() {
        return textField.getText();
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }


}
