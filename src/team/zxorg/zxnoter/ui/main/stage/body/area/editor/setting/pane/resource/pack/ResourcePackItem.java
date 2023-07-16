package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource.pack;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.ResourcePack;

public class ResourcePackItem extends VBox {
    ImageView icon;
    Label name = new Label();

    public ResourcePackItem(ResourcePack pack) {
        name.setText(pack.getName());
        icon = new ImageView(pack.getIcon());
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(icon, name);
    }
}