package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.ListSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.BaseSettingPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.pack.ResourcePackSettingItem;

import java.util.Iterator;
import java.util.Map;

public class ResourcesSettingPane extends BaseSettingPane {
    ResourcePackSettingItem loadedPacks = new ResourcePackSettingItem("editor.setting-editor.settings.resources.packs");
    ListSettingItem usedResources = new ListSettingItem("editor.setting-editor.settings.resources.used-resources");

    public ResourcesSettingPane() {
        super("editor.setting-editor.settings.resources");

        settingItems.add(loadedPacks);
        settingItems.add(usedResources);


        reload();
    }

    public void reload() {
        //loadedPacks.listView.getItems().clear();
        usedResources.listView.getItems().clear();

        Iterator<Map.Entry<String, ResourcePack>> loadPacks = ZXResources.loadedResourcePackMap.entrySet().iterator();
        /*while (loadPacks.hasNext()) {
            ResourcePack pack = loadPacks.next().getValue();
            Iterator<Map.Entry<String, BaseResourcePack>> languagePacks = pack.getResources(ResourceType.language).entrySet().iterator();
            loadedPacks.listView.getItems().add(pack);
        }
        for (BaseResourcePack resourcePack : ZXResources.usedResources) {
            usedResources.listView.getItems().add(resourcePack);
        }*/
    }
}
