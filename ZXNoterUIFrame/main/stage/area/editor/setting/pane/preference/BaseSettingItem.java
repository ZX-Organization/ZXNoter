package team.zxorg.ui.main.stage.area.editor.setting.pane.preference;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

public class BaseSettingItem extends Label {
    public TreeItem<BaseSettingItem> thisTreeItem = new TreeItem<>(this);
    public BaseSettingPane settingPane;

    public BaseSettingItem(BaseSettingPane settingPane) {
        this.settingPane = settingPane;
        textProperty().bind(settingPane.title.textProperty());
    }
}
