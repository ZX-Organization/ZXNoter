package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane;

import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.*;

public class RootSettingPane extends BaseSettingPane {
    public RootSettingPane() {
        super("editor.setting-editor.settings.user-preference");
    }

    {
        addSettingPaneItem(new UserPreferencePane());

    }

}
