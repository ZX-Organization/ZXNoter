package team.zxorg.zxnoter.ui.main.stage.side;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.config.ZXProject;
import team.zxorg.zxnoter.config.ZXConfig;
import team.zxorg.zxnoter.config.configuration.sub.preference.side.SideBarCfg;
import team.zxorg.zxnoter.ui.component.TrackTooltip;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.main.ZXStage;

public class BaseSideBarTab extends Tab {
    public VBox body = new VBox();
    private final String sideKey;
    protected ZXStage zxStage;
    protected SideBar sideBar;
    protected ZXProject zxProject;
    protected SideBarCfg cfg= ZXConfig.configuration.userPreference.sideBar;

    public BaseSideBarTab(String sideKey, String iconKey, ZXStage zxStage) {
        this.sideKey = sideKey;
        zxProject = zxStage.zxProject;
        this.zxStage = zxStage;
        sideBar = zxStage.sideBar;
        setClosable(false);
        ZXIcon zxIcon = new ZXIcon();
        zxIcon.setSize(30);
        zxIcon.setIconKey(iconKey);
        zxIcon.setColor(ZXColor.FONT_USUALLY);
        zxIcon.setOnMousePressed(event -> {
            if (sideBar.getSelectionModel().getSelectedItem().equals(this)) {
                sideBar.isFold.set(!sideBar.isFold.get());
                event.consume();
            } else {
                sideBar.isFold.set(false);
            }
        });
        TrackTooltip trackTooltip = new TrackTooltip(zxIcon, Pos.BOTTOM_CENTER, 0, TrackTooltip.BindAttributes.HOVER_POP_UP);
        trackTooltip.setPos(Pos.CENTER_RIGHT, false, 14);
        trackTooltip.textProperty().bind(GlobalResources.getLanguageContent("side-bar." + sideKey));

        setGraphic(zxIcon);
        setContent(body);

    }
}
