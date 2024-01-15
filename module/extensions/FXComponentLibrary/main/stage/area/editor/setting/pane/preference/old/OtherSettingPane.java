package team.zxorg.ui.main.stage.area.editor.setting.pane.preference.old;

import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BooleanSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.BaseSettingPane;

public class OtherSettingPane extends BaseSettingPane {
    public OtherSettingPane() {
        super("editor.setting-editor.settings.other");
        settingItems.add(new BooleanSettingItem("editor.setting-editor.settings.other.close-auto-save", UserPreference.isCloseAutoSave));
    }
}
