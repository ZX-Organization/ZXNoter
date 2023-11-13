package team.zxorg.ui.main.stage.area.editor.setting.pane.preference.old;

import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseResourceSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.BaseSettingPane;

public class LanguageSettingPane extends BaseSettingPane {
    public LanguageSettingPane() {
        super("editor.setting-editor.settings.language");
        settingItems.add(new BaseResourceSettingItem("editor.setting-editor.settings.language.base", ResourceType.language));
    }
}
