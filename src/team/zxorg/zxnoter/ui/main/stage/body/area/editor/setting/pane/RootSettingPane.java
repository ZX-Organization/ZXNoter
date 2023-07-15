package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane;

public class RootSettingPane extends BaseSettingPane {
    public RootSettingPane() {
        super("editor.setting-editor.settings.user-preference");
    }

    {
        addSettingPaneItem(new LanguageSettingPane());
        addSettingPaneItem(new ResourcesSettingPane());
    }

}
