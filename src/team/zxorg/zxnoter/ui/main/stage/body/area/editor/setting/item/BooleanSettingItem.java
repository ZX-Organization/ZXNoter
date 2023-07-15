package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;

public class BooleanSettingItem extends SettingItem {
    public BooleanSettingItem(String titleKey) {
        super(titleKey, new CheckBox());
    }
}
