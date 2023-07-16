package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.pack;

import javafx.collections.MapChangeListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.ZXIconButton;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;

import java.util.Iterator;
import java.util.Map;

public class ResourcePackSettingItem extends BaseSettingItem {
    ListView<ResourcePackItem> resourcePackItemListView;

    public ResourcePackSettingItem(String titleKey) {
        super(titleKey);
        setOrientation(Orientation.VERTICAL);
    }

    @Override
    protected Node initContent() {
        resourcePackItemListView = new ListView<>();
        resourcePackItemListView.setPrefWidth(260);
        resourcePackItemListView.setPrefHeight(108);
        resourcePackItemListView.setOrientation(Orientation.HORIZONTAL);
        reload();
        ZXResources.loadedResourcePackMap.addListener((MapChangeListener<String, ResourcePack>) change -> {
            reload();
        });

        ZXIconButton importPacks = new ZXIconButton();
        importPacks.setColor(ZXColor.FONT_USUALLY);
        importPacks.setIconKey("document.file-add");
        importPacks.setSize(28);

        ZXIconButton reloadPacks = new ZXIconButton();
        reloadPacks.setColor(ZXColor.FONT_USUALLY);
        reloadPacks.setIconKey("system.refresh");
        reloadPacks.setSize(28);

        ZXIconButton openPacks = new ZXIconButton();
        openPacks.setColor(ZXColor.FONT_USUALLY);
        openPacks.setIconKey("document.folder-4");
        openPacks.setSize(28);

        VBox buttons = new VBox(importPacks, reloadPacks, openPacks);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(8);
        HBox.setHgrow(resourcePackItemListView, Priority.ALWAYS);
        HBox hBox = new HBox(resourcePackItemListView, buttons);
        hBox.setSpacing(6);
        return hBox;
    }

    private void reload() {
        resourcePackItemListView.getItems().clear();
        Iterator<Map.Entry<String, ResourcePack>> loadPacks = ZXResources.loadedResourcePackMap.entrySet().iterator();
        while (loadPacks.hasNext()) {
            ResourcePack pack = loadPacks.next().getValue();
            resourcePackItemListView.getItems().add(new ResourcePackItem(pack));
        }
    }
}
