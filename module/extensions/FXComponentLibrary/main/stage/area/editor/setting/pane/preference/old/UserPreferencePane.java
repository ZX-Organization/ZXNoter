package team.zxorg.ui.main.stage.area.editor.setting.pane.preference.old;

import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.BaseSettingPane;

public class UserPreferencePane extends BaseSettingPane {

    public UserPreferencePane() {
        super("editor.setting-editor.settings.user-preference");
    }

    {
        addSettingPaneItem(new LanguageSettingPane());
        addSettingPaneItem(new ResourcesSettingPane());
        addSettingPaneItem(new IconSettingPane());
        addSettingPaneItem(new OtherSettingPane());
    }


}
