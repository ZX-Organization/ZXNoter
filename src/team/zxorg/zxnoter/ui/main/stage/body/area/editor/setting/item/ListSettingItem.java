package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ListSettingItem extends SettingItem {
    public ListView listView;

    public ListSettingItem(String titleKey) {
        super(titleKey, new ListView<>());
        listView = (ListView) content;
        listView.setPrefHeight(80);
        listView.setPrefWidth(480);
    }
}
