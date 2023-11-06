package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.old;

import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseResourceSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.BaseSettingPane;

public class IconSettingPane extends BaseSettingPane {
    public IconSettingPane() {
        super("editor.setting-editor.settings.icon");

        settingItems.add(new BaseResourceSettingItem("editor.setting-editor.settings.icon.base", ResourceType.icon));
    }
}
