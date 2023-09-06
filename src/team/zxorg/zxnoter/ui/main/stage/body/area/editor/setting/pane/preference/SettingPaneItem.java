package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

public class SettingPaneItem extends Label {
    public TreeItem<SettingPaneItem> thisTreeItem = new TreeItem<>(this);
    public BaseSettingPane settingPane;

    public SettingPaneItem(BaseSettingPane settingPane) {
        this.settingPane = settingPane;
        textProperty().bind(settingPane.title.textProperty());
    }
}
