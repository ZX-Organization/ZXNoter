package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.resource;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;

public class ResourceItem extends VBox {
    ImageView icon;
    Label name = new Label();
    BaseResourcePack resourcePack;

    public ResourceItem(BaseResourcePack resourcePack) {
        this.resourcePack = resourcePack;
        name.setText(resourcePack.getName());
        icon = new ImageView(resourcePack.getResourceIcon());
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(icon, name);
    }
}
