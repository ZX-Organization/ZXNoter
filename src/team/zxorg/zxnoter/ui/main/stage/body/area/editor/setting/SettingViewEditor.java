package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseViewEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.RootSettingPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference.SettingPaneItem;

public class SettingViewEditor extends BaseViewEditor {
    VBox settingItemsPane;

    public SettingViewEditor(ZXProject zxProject) {
        super(zxProject);
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


        TreeView<SettingPaneItem> settingItemsView = new TreeView<>();
        settingItemsView.setMinWidth(Region.USE_PREF_SIZE);
        settingItemsView.setPrefWidth(160);

        settingItemsPane = new VBox();
        settingItemsPane.setPadding(new Insets(8));
        settingItemsPane.setSpacing(6);
        ScrollPane settingItemsScrollPane = new ScrollPane(settingItemsPane);
        settingItemsScrollPane.setFitToWidth(true);
        HBox.setHgrow(settingItemsScrollPane, Priority.ALWAYS);

        settingItemsView.setRoot(new RootSettingPane().settingPaneItem.thisTreeItem);
        settingItemsView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<SettingPaneItem>>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    SettingPaneItem settingPaneItemTreeItem = c.getAddedSubList().get(0).getValue();
                    ZXLogger.info("重新编制设置项" + settingPaneItemTreeItem);
                    settingItemsPane.getChildren().clear();
                    showSettingPanes(settingPaneItemTreeItem, searchTextField.getText());
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
    protected void closed() {
    }


    private void showSettingPanes(SettingPaneItem settingPaneItemTreeItem, String searchText) {
        settingItemsPane.getChildren().add(settingPaneItemTreeItem.settingPane.title);
        boolean allShow = settingPaneItemTreeItem.settingPane.title.getText().toLowerCase().contains(searchText.toLowerCase());
        boolean show = false;
        for (BaseSettingItem settingItem : settingPaneItemTreeItem.settingPane.settingItems) {
            if (settingItem.title.getText().toLowerCase().contains(searchText.toLowerCase()) || allShow) {
                settingItem.setPadding(new Insets(0, 0, 0, 12));
                settingItemsPane.getChildren().add(settingItem.getBox());
                show = true;
            }
        }
        if (!show)
            settingItemsPane.getChildren().remove(settingPaneItemTreeItem.settingPane.title);
        for (TreeItem<SettingPaneItem> treeItem : settingPaneItemTreeItem.thisTreeItem.getChildren()) {
            showSettingPanes(treeItem.getValue().settingPane.settingPaneItem, searchText);
        }
    }
}
