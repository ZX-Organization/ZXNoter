package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane;

import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.language.LanguageSettingPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.ResourcesSettingPane;

public class RootSettingPane extends BaseSettingPane {
    public RootSettingPane() {
        super("editor.setting-editor.settings.user-preference");
    }

    {
        addSettingPaneItem(new LanguageSettingPane());
        addSettingPaneItem(new ResourcesSettingPane());
    }

}
