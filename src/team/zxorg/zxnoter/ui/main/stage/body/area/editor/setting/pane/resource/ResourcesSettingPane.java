package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource;

import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.BaseSettingPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.pack.ResourcePackSettingItem;

import java.util.ArrayList;

public class ResourcesSettingPane extends BaseSettingPane {
    ResourcePackSettingItem loadedPacks = new ResourcePackSettingItem("editor.setting-editor.settings.resources.packs");
    ResourceSettingItem usedResources = new ResourceSettingItem("editor.setting-editor.settings.resources.used-resources");

    public ResourcesSettingPane() {
        super("editor.setting-editor.settings.resources");

        settingItems.add(loadedPacks);
        settingItems.add(usedResources);
        for (ResourceType resourceType : ResourceType.values()) {
            BaseResourceSettingItem baseResourceSettingItem = new BaseResourceSettingItem("editor.setting-editor.settings." + resourceType.name() + ".base", resourceType);
            settingItems.add(baseResourceSettingItem);
       }



    }


}
