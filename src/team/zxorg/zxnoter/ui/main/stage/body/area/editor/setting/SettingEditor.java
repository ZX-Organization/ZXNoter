package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.RootSettingPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.SettingPaneItem;

public class SettingEditor extends BaseEditor {
    VBox settingItemsPane;

    public SettingEditor() {
        icon.setColor(ZXColor.FONT_USUALLY);
        icon.setIconKey("system.list-settings");
        textProperty().bind(GlobalResources.getLanguageContent("editor.setting-editor.tab-title"));

        VBox body = new VBox();
        body.setSpacing(8);
        body.setMaxWidth(1200);
        body.setAlignment(Pos.TOP_CENTER);
        body.setPadding(new Insets(8));
        ZXTextField searchTextField = new ZXTextField();
        searchTextField.setPromptTextKey("editor.setting-editor.settings.search");
        HBox.setHgrow(searchTextField, Priority.ALWAYS);

        ZXGroupComponent searchGroup = new ZXGroupComponent();

        ZXIcon searchIcon = new ZXIcon();
        searchIcon.setColor(ZXColor.FONT_USUALLY);
        searchIcon.setSize(22);
        searchIcon.setIconKey("system.search");
        ZXIconButton searchClearButton = new ZXIconButton();
        searchClearButton.setColor(ZXColor.FONT_USUALLY);
        searchClearButton.setIconKey("system.close");
        searchClearButton.setSize(22);
        searchClearButton.setOnAction((event) -> searchTextField.clear());
        searchClearButton.setVisible(false);
        Tooltip searchClearButtonTip = ComponentFactory.getTooltip(searchClearButton, 0);
        searchGroup.getChildren().addAll(searchIcon, searchTextField, searchClearButton);
        searchGroup.setPrefWidth(Double.MAX_VALUE);

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
                    showSettingPanes(settingPaneItemTreeItem);

                }
            }
        });


        HBox settingsBox = new HBox(settingItemsView, settingItemsScrollPane);
        VBox.setVgrow(settingsBox, Priority.ALWAYS);

        body.getChildren().addAll(searchGroup, settingsBox);


        HBox root = new HBox(body);
        root.setAlignment(Pos.TOP_CENTER);
        setContent(root);


        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchClearButton.setVisible(newValue.length() != 0);
        });


        setOnCloseRequest(event -> {

        });

    }


    private void showSettingPanes(SettingPaneItem settingPaneItemTreeItem) {
        settingItemsPane.getChildren().add(settingPaneItemTreeItem.settingPane.title);
        for (BaseSettingItem settingItem : settingPaneItemTreeItem.settingPane.settingItems) {
            settingItem.setPadding(new Insets(0, 0, 0, 12));
            settingItemsPane.getChildren().add(settingItem.getBox());
        }
        for (TreeItem<SettingPaneItem> treeItem : settingPaneItemTreeItem.thisTreeItem.getChildren()) {
            showSettingPanes(treeItem.getValue().settingPane.settingPaneItem);
        }
    }
}
