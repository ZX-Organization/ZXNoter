package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;
import team.zxorg.zxnoter.resource.ResourceWeight;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.resource.preference.UserPreference;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.ResourceItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.pack.ResourcePackItem;

import java.util.HashMap;
import java.util.Map;

public class BaseResourceSettingItem extends BaseSettingItem {
    public ResourceType resourceType;
    ListView<ResourceItem> resourceItemListView;
    boolean isLoading = false;

    public BaseResourceSettingItem(String titleKey, ResourceType resourceType) {
        super(titleKey);
        setOrientation(Orientation.VERTICAL);
        this.resourceType = resourceType;
        reload();
    }

    @Override
    protected Node initContent() {
        resourceItemListView = new ListView<>();
        resourceItemListView.setPrefWidth(260);
        resourceItemListView.setPrefHeight(108);
        resourceItemListView.setOrientation(Orientation.HORIZONTAL);

        ZXResources.loadedResourcePackMap.addListener((MapChangeListener<String, ResourcePack>) change -> {
            if (!isLoading)
                reload();
        });
        ZXResources.usedAllResources.addListener((ListChangeListener<BaseResourcePack>) c -> {
            if (!isLoading)
                reload();
        });
        resourceItemListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ResourceItem>) c -> {
            if (!isLoading) {
                BaseResourcePack pack = resourceItemListView.getSelectionModel().getSelectedItem().resourcePack;
                System.out.println("设置" + pack);
                UserPreference.setUsedBaseResources(pack.getType(), pack.getResourceFullId());
                if (pack instanceof LanguageResourcePack)
                    UserPreference.setLanguageCode(pack.getResourceId());
                ZXResources.reloadGlobalResource(pack.getType());
                reload();
            }
        });

        return resourceItemListView;
    }

    private void reload() {
        isLoading = true;
        resourceItemListView.getItems().clear();
        HashMap<BaseResourcePack, ResourceItem> indexes = new HashMap<>();
        for (Map.Entry<String, ResourcePack> pack : ZXResources.loadedResourcePackMap.entrySet()) {
            ResourcePack resourcePack = pack.getValue();
            for (Map.Entry<String, BaseResourcePack> resource : resourcePack.getResources(resourceType).entrySet()) {
                BaseResourcePack resourceValue = resource.getValue();
                if (resourceValue.getType().equals(resourceType)) {
                    if (resourceValue.getWeight().equals(ResourceWeight.base)) {
                        indexes.put(resourceValue, new ResourceItem(resourceValue));
                        resourceItemListView.getItems().add(indexes.get(resourceValue));
                    }
                }
            }
        }
        resourceItemListView.getSelectionModel().select(indexes.get(ZXResources.getUsedBaseResources(resourceType).get()));
        isLoading = false;
    }
}
