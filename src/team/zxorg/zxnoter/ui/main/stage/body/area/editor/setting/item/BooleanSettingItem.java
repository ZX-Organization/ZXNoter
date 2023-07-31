package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class BooleanSettingItem extends BaseSettingItem {
    CheckBox checkBox;

    public BooleanSettingItem(String titleKey, SimpleBooleanProperty booleanProperty) {
        super(titleKey);
        checkBox.setSelected(booleanProperty.get());
        booleanProperty.addListener((observable, oldValue, newValue) -> checkBox.setSelected(newValue));
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> booleanProperty.set(newValue));
    }

    @Override
    protected Node initContent() {
        checkBox = new CheckBox();
        return checkBox;
    }
}
