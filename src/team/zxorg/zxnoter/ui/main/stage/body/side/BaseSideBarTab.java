package team.zxorg.zxnoter.ui.main.stage.body.side;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.TrackTooltip;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

public class BaseSideBarTab extends Tab {
    public VBox body = new VBox();

    public BaseSideBarTab(String tipLanguageKey, String iconKey, SideBar tabPane) {
        setClosable(false);
        ZXIcon zxIcon = new ZXIcon();
        zxIcon.setSize(30);
        zxIcon.setIconKey(iconKey);
        zxIcon.setColor(ZXColor.FONT_USUALLY);
        zxIcon.setOnMousePressed(event -> {
            if (tabPane.getSelectionModel().getSelectedItem().equals(this)) {
                tabPane.isFold.set(!tabPane.isFold.get());
                event.consume();
            } else {
                tabPane.isFold.set(false);
            }
        });
        TrackTooltip trackTooltip = new TrackTooltip(zxIcon, Pos.BOTTOM_CENTER, 0, TrackTooltip.BindAttributes.HOVER_POP_UP);
        trackTooltip.setPos(Pos.CENTER_RIGHT, false, 14);
        trackTooltip.textProperty().bind(GlobalResources.getLanguageContent(tipLanguageKey));

        setGraphic(zxIcon);
        setContent(body);
        tabPane.getTabs().add(this);
    }
}
