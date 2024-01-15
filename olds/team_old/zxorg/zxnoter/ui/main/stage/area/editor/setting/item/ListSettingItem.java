package team.zxorg.zxnoter.ui.main.stage.area.editor.setting.item;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ListSettingItem extends BaseSettingItem {
    public ListView<Label> listView;

    public ListSettingItem(String titleKey) {
        super(titleKey);
        listView.setPrefHeight(80);
        listView.setPrefWidth(480);
    }

    @Override
    protected Node initContent() {
        listView = new ListView<>();
        return listView;
    }
}
