package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource;

import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;

public class ResourceSettingItem extends BaseSettingItem {
    ListView<ResourceItem> resourceItemListView;

    public ResourceSettingItem(String titleKey) {
        super(titleKey);
        setOrientation(Orientation.VERTICAL);
    }

    @Override
    protected Node initContent() {
        resourceItemListView = new ListView<>();
        resourceItemListView.setPrefWidth(260);
        resourceItemListView.setPrefHeight(108);
        resourceItemListView.setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(resourceItemListView, Priority.ALWAYS);
        HBox hBox = new HBox(resourceItemListView);
        hBox.setSpacing(6);
        reload();


        ZXResources.usedAllResources.addListener((ListChangeListener<BaseResourcePack>) c -> {
            reload();
        });


        return hBox;
    }

    private void reload() {
        resourceItemListView.getItems().clear();
        for (BaseResourcePack resourcePack : ZXResources.usedAllResources) {
            resourceItemListView.getItems().add(new ResourceItem(resourcePack));
        }
    }
}
