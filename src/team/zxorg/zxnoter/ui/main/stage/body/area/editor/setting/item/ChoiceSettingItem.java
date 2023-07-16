package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class ChoiceSettingItem extends BaseSettingItem {
    public ChoiceBox<Label> choiceBox;

    public ChoiceSettingItem(String titleKey) {
        super(titleKey);
    }

    @Override
    protected Node initContent() {
        choiceBox = new ChoiceBox<>();
        return choiceBox;
    }
}
