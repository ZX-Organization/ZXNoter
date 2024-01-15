package team.zxorg.zxnoter.ui.main.stage.area.editor.setting;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.area.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.area.editor.setting.pane.RootSettingPane;
import team.zxorg.zxnoter.ui.main.stage.area.editor.setting.pane.preference.BaseSettingItem;

import java.nio.file.Path;

public class SettingViewEditor extends BaseEditor {
    VBox settingItemsPane;

    public SettingViewEditor(EditorArea editorArea) {
        super(Path.of("zxn/setting"),editorArea);
        icon.setColor(ZXColor.FONT_USUALLY);
        icon.setIconKey("system.list-settings");
        textProperty().bind(GlobalResources.getLanguageContent("editor.setting-editor.tab-title"));

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setPrefWidth(1200);
        //body.setMaxWidth(1200);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(8, 8, 0, 8));

        ZXTextFieldGroup searchTextField = new ZXTextFieldGroup("editor.setting-editor.settings.search", "system.search");


        TreeView<BaseSettingItem> settingItemsView = new TreeView<>();
        settingItemsView.setMinWidth(Region.USE_PREF_SIZE);
        settingItemsView.setPrefWidth(160);

        settingItemsPane = new VBox();
        settingItemsPane.setPadding(new Insets(8));
        settingItemsPane.setSpacing(6);
        ScrollPane settingItemsScrollPane = new ScrollPane(settingItemsPane);
        settingItemsScrollPane.setFitToWidth(true);
        HBox.setHgrow(settingItemsScrollPane, Priority.ALWAYS);

        settingItemsView.setRoot(new RootSettingPane().baseSettingItem.thisTreeItem);
        settingItemsView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<BaseSettingItem>>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    BaseSettingItem baseSettingItemTreeItem = c.getAddedSubList().get(0).getValue();
                    ZXLogger.info("重新编制设置项" + baseSettingItemTreeItem);
                    settingItemsPane.getChildren().clear();
                    showSettingPanes(baseSettingItemTreeItem, searchTextField.getText());
                }
            }
        });

        settingItemsView.getRoot().setExpanded(true);
        settingItemsView.getSelectionModel().selectFirst();
        settingItemsView.setShowRoot(false);

        HBox settingsBox = new HBox(settingItemsView, settingItemsScrollPane);
        VBox.setVgrow(settingsBox, Priority.ALWAYS);

        vBox.getChildren().addAll(searchTextField, settingsBox);


        HBox root = new HBox(vBox);
        root.setAlignment(Pos.TOP_CENTER);
        body.setCenter(root);


        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settingItemsView.getSelectionModel().clearSelection();
            settingItemsView.getSelectionModel().selectFirst();
        });


        setOnCloseRequest(event -> {

        });

    }

    @Override
    protected void saveFile() {

    }

    @Override
    protected void closed() {
    }


    private void showSettingPanes(BaseSettingItem baseSettingItemTreeItem, String searchText) {
        settingItemsPane.getChildren().add(baseSettingItemTreeItem.settingPane.title);
        boolean allShow = baseSettingItemTreeItem.settingPane.title.getText().toLowerCase().contains(searchText.toLowerCase());
        boolean show = false;
        for (team.zxorg.zxnoter.ui.main.stage.area.editor.setting.item.BaseSettingItem settingItem : baseSettingItemTreeItem.settingPane.settingItems) {
            if (settingItem.title.getText().toLowerCase().contains(searchText.toLowerCase()) || allShow) {
                settingItem.setPadding(new Insets(0, 0, 0, 12));
                settingItemsPane.getChildren().add(settingItem.getBox());
                show = true;
            }
        }
        if (!show)
            settingItemsPane.getChildren().remove(baseSettingItemTreeItem.settingPane.title);
        for (TreeItem<BaseSettingItem> treeItem : baseSettingItemTreeItem.thisTreeItem.getChildren()) {
            showSettingPanes(treeItem.getValue().settingPane.baseSettingItem, searchText);
        }
    }
}
