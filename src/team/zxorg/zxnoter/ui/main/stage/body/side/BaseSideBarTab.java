package team.zxorg.zxnoter.ui.main.stage.body.side;

import com.alibaba.fastjson2.JSONObject;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.TrackTooltip;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

public class BaseSideBarTab extends Tab {
    public VBox body = new VBox();
    private final String sideKey;
    public final JSONObject config;

    public BaseSideBarTab(String sideKey, String iconKey, SideBar tabPane) {
        this.sideKey = sideKey;
        config = UserPreference.getSideConfiguration(sideKey);
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
        trackTooltip.textProperty().bind(GlobalResources.getLanguageContent("side-bar." + sideKey));

        setGraphic(zxIcon);
        setContent(body);
        tabPane.getTabs().add(this);
    }
}
