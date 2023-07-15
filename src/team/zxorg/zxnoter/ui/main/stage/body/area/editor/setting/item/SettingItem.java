package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.ZXLabel;

public class SettingItem extends HBox {
    public ZXLabel title;
    public Node content;


    public SettingItem(String titleKey, Node content) {
        title = new ZXLabel(titleKey);
        title.setMinWidth(Region.USE_PREF_SIZE);
        this.content = content;
        getChildren().addAll(title, content);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);
        HBox.setHgrow(content, Priority.ALWAYS);
    }
}
