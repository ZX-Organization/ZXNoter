package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BooleanSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.ChoiceSettingItem;

import java.util.Iterator;
import java.util.Map;

public class LanguageSettingPane extends BaseSettingPane {
    public LanguageSettingPane() {
        super("editor.setting-editor.settings.language");
        settingItems.add(new BooleanSettingItem("editor.setting-editor.settings.language.use-local-language"));
        ChoiceSettingItem languagesCode = new ChoiceSettingItem("editor.setting-editor.settings.language.language-code");
        languagesCode.choiceBox.getItems().clear();
        Iterator<Map.Entry<String, ResourcePack>> loadPacks = ZXResources.loadedResourcePackMap.entrySet().iterator();
        while (loadPacks.hasNext()) {
            ResourcePack pack = loadPacks.next().getValue();
            Iterator<Map.Entry<String, BaseResourcePack>> languagePacks = pack.getResources(ResourceType.language).entrySet().iterator();
            while (languagePacks.hasNext()) {
                BaseResourcePack langPack = languagePacks.next().getValue();
                if (langPack instanceof LanguageResourcePack languageResourcePack){
                    languagesCode.choiceBox.getItems().add(languageResourcePack);
                }
            }
        }

        settingItems.add(languagesCode);
    }
}
