package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.language;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ResourceWeight;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.resource.preference.UserPreference;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;

import java.util.Map;

public class LanguageSettingItem extends BaseSettingItem {
    ChoiceBox<LanguageItem> languages;

    public LanguageSettingItem(String titleKey) {
        super(titleKey);
    }

    boolean isLoading = false;

    @Override
    protected Node initContent() {
        languages = new ChoiceBox<>();
        //ZXLabel example = new ZXLabel("show.hello");
        reload();

        //触发修改
        languages.setOnAction(event -> {
            if (!isLoading) {
                LanguageResourcePack languageResourcePack = languages.getValue().resourcePack;
                UserPreference.setUsedBaseResources(ResourceType.language, languageResourcePack.getResourceFullId());
                UserPreference.setLanguageCode(languageResourcePack.getResourceId());
                ZXResources.reloadGlobalResource(ResourceType.language);
                reload();
            }
        });


        //监听使用的资源
        /*ZXResources.usedResources.addListener((ListChangeListener<BaseResourcePack>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    BaseResourcePack language = c.getAddedSubList().get(0);
                    if (language instanceof LanguageResourcePack languageResourcePack) {
                        reload();
                        languages.setValue(languages.getItems().filtered(languageItem -> languageItem.resourcePack.equals(languageResourcePack)).get(0));
                    }

                }
            }
        });*/

        HBox hBox = new HBox(languages);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(6);
        return hBox;
    }

    /**
     * 重载语言列表
     */
    public void reload() {
        isLoading = true;
        languages.getItems().clear();
        for (Map.Entry<String, ResourcePack> stringResourcePackEntry : ZXResources.loadedResourcePackMap.entrySet()) {
            ResourcePack pack = stringResourcePackEntry.getValue();
            for (Map.Entry<String, BaseResourcePack> stringBaseResourcePackEntry : pack.getResources(ResourceType.language).entrySet()) {
                BaseResourcePack langPack = stringBaseResourcePackEntry.getValue();
                if (langPack instanceof LanguageResourcePack languageResourcePack) {
                    if (languageResourcePack.getWeight().equals(ResourceWeight.base))
                        languages.getItems().add(new LanguageItem(languageResourcePack));
                }
            }
        }
        //选择
        for (BaseResourcePack languageResourcePack : ZXResources.getUsedAllResources(ResourceType.language)) {
            languages.setValue(languages.getItems().filtered(languageItem -> languageItem.resourcePack.equals(languageResourcePack)).get(0));
        }
        isLoading = false;
    }
}
