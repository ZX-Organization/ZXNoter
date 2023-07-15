package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

public class ChoiceSettingItem extends SettingItem {
    public ChoiceBox choiceBox;

    public ChoiceSettingItem(String titleKey) {
        super(titleKey, new ChoiceBox<>());
        choiceBox = (ChoiceBox) content;
    }
}
