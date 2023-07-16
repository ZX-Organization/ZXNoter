package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.language;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class LanguageSettingItem extends BaseSettingItem {
    ChoiceBox<LanguageItem> languages;

    public LanguageSettingItem(String titleKey) {
        super(titleKey);
    }

    @Override
    protected Node initContent() {
        languages = new ChoiceBox<>();
        Label example = new Label();
        reload();
        languages.setOnAction(event -> {
            GlobalResources.loadResourcesPack(languages.getValue().resourcePack);
            example.setText(languages.getValue().resourcePack.getLanguageContent("show.hello"));
        });

        for (BaseResourcePack languageResourcePack : ZXResources.getUsedResources(ResourceType.language)) {
            languages.setValue(languages.getItems().filtered(languageItem -> languageItem.resourcePack.equals(languageResourcePack)).get(0));
        }


        HBox hBox = new HBox(languages, example);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(6);
        return hBox;
    }

    public void reload() {
        languages.getItems().clear();
        Iterator<Map.Entry<String, ResourcePack>> loadPacks = ZXResources.loadedResourcePackMap.entrySet().iterator();
        while (loadPacks.hasNext()) {
            ResourcePack pack = loadPacks.next().getValue();
            Iterator<Map.Entry<String, BaseResourcePack>> languagePacks = pack.getResources(ResourceType.language).entrySet().iterator();
            while (languagePacks.hasNext()) {
                BaseResourcePack langPack = languagePacks.next().getValue();
                if (langPack instanceof LanguageResourcePack languageResourcePack) {
                    languages.getItems().add(new LanguageItem(languageResourcePack));
                }
            }
        }
    }
}
