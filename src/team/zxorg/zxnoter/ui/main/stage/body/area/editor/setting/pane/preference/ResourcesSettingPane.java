package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference;

import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.ResourceSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.ResourcePackSettingItem;

public class ResourcesSettingPane extends BaseSettingPane {
    ResourcePackSettingItem loadedPacks = new ResourcePackSettingItem("editor.setting-editor.settings.resources.packs");
    ResourceSettingItem usedResources = new ResourceSettingItem("editor.setting-editor.settings.resources.used-resources");



    public ResourcesSettingPane() {
        super("editor.setting-editor.settings.resources");
        settingItems.add(loadedPacks);
        settingItems.add(usedResources);
    }

}