package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane;

import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BooleanSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseResourceSettingItem;

import java.util.Locale;

public class LanguageSettingPane extends BaseSettingPane {
    public LanguageSettingPane() {
        super("editor.setting-editor.settings.language");
        settingItems.add(new BaseResourceSettingItem("editor.setting-editor.settings.language.base", ResourceType.language));
    }
}
