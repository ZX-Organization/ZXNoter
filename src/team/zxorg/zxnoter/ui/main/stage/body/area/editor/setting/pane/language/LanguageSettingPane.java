package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.language;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BooleanSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.ChoiceSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.BaseSettingPane;

import java.util.Iterator;
import java.util.Map;

public class LanguageSettingPane extends BaseSettingPane {
    public LanguageSettingPane() {
        super("editor.setting-editor.settings.language");
        settingItems.add(new BooleanSettingItem("editor.setting-editor.settings.language.use-local-language"));
    }
}
