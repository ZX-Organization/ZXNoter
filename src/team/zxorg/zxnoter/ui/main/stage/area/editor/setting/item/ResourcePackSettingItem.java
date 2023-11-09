package team.zxorg.zxnoter.ui.main.stage.area.editor.setting.item;

import javafx.collections.MapChangeListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.ZXIconButton;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        resourcePackItemListView.setPrefHeight(120);
        resourcePackItemListView.setOrientation(Orientation.HORIZONTAL);
        reload();
        ZXResources.loadedResourcePackMap.addListener((MapChangeListener<String, ResourcePack>) change -> {
            reload();
        });

        ZXIconButton importPacks = new ZXIconButton();
        importPacks.setColor(ZXColor.FONT_USUALLY);
        importPacks.setIconKey("document.file-add");
        importPacks.setSize(28);
        importPacks.setOnAction((event -> {
            ZXLogger.info("用户导入资源包");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择资源包文件");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("资源包压缩包", "*.zip"),
                    new FileChooser.ExtensionFilter("资源包文件", "*.zxrp"));
            File selectedFile = fileChooser.showOpenDialog(this.title.getScene().getWindow());
            if (selectedFile != null) {
                try {
                    Path of = Path.of("./resource");
                    Files.move(selectedFile.toPath(), of.normalize().resolve(selectedFile.getName()));
                    ZXResources.searchPacks(of);
                } catch (IOException e) {
                    ZXLogger.info("资源文件移动失败");
                    throw new RuntimeException(e);
                }
            } else {
                ZXLogger.info("没有文件被选择 用户取消");
            }
        }));

        ZXIconButton reloadPacks = new ZXIconButton();
        reloadPacks.setColor(ZXColor.FONT_USUALLY);
        reloadPacks.setIconKey("system.refresh");
        reloadPacks.setSize(28);
        reloadPacks.setOnAction((event -> {
            ZXResources.clearPacks();//清除一遍资源
            ZXResources.loadInternalPacks();
            ZXResources.searchPacks(Path.of("./resource"));//搜索本地资源包
        }));

        ZXIconButton openPacks = new ZXIconButton();
        openPacks.setColor(ZXColor.FONT_USUALLY);
        openPacks.setIconKey("document.folder-open");
        openPacks.setSize(28);
        openPacks.setOnAction((event) -> {
            try {
                ZXLogger.info("打开资源包目录");
                Desktop.getDesktop().open(new File("./resource"));
            } catch (IOException e) {
                ZXLogger.warning("无法打开资源包目录");
                throw new RuntimeException(e);
            }
        });

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
