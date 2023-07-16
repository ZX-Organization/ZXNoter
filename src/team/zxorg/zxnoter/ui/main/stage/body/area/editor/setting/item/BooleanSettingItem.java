package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class BooleanSettingItem extends BaseSettingItem {
    CheckBox checkBox ;

    public BooleanSettingItem(String titleKey) {
        super(titleKey);
    }

    @Override
    protected Node initContent() {
        checkBox = new CheckBox();
        return checkBox;
    }
}
