package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.ui.component.TrackTooltip;

public class ResourcePackItem extends VBox {
    ImageView icon;
    Label name = new Label();
    ResourcePack pack;

    public ResourcePackItem(ResourcePack pack) {
        this.pack = pack;
        name.setText(pack.getName());
        icon = new ImageView(pack.getIcon());
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        setMaxWidth(100);
        setAlignment(Pos.CENTER);
        getChildren().addAll(icon, name);
        buildTooltip();
    }


    void buildTooltip() {
        ImageView icon = new ImageView(pack.getIcon());
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        Label name = new Label(pack.getName());
        name.setFont(Font.font(18));
        Label packName = new Label(pack.getAuthor() + " " + pack.getId());
        packName.setFont(Font.font(14));
        Label describe = new Label(pack.getDescribe());
        describe.setFont(Font.font(12));
        VBox info = new VBox(name, packName, describe);
        info.setPadding(new Insets(2));
        HBox root = new HBox(icon, info);
        TrackTooltip trackTooltip = new TrackTooltip(this, Pos.TOP_CENTER, 0, TrackTooltip.BindAttributes.HOVER_POP_UP);
        trackTooltip.setShowDelay(Duration.ZERO);
        trackTooltip.setShowDuration(Duration.INDEFINITE);
        trackTooltip.setHideDelay(Duration.ZERO);
        trackTooltip.setGraphic(root);
    }
}