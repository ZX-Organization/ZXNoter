package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference;

import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseResourceSettingItem;

public class LanguageSettingPane extends BaseSettingPane {
    public LanguageSettingPane() {
        super("editor.setting-editor.settings.language");
        settingItems.add(new BaseResourceSettingItem("editor.setting-editor.settings.language.base", ResourceType.language));
    }
}
