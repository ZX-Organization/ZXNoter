package team.zxorg.ui.main.stage.side;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import team.zxorg.ui.component.ZXIcon;
import team.zxorg.ui.main.ZXStage;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.config.ZXProjectManager;
import team.zxorg.zxnoter.config.ZXConfigManager;
import team.zxorg.zxnoter.config.sub.preference.side.SideBarCfg;
import team.zxorg.ui.component.TrackTooltip;

public class BaseSideBarTab extends Tab {
    public VBox body = new VBox();
    private final String sideKey;
    protected ZXStage zxStage;
    protected SideBar sideBar;
    protected ZXProjectManager zxProjectManager;
    protected SideBarCfg cfg= ZXConfigManager.configuration.userPreference.sideBar;

    public BaseSideBarTab(String sideKey, String iconKey, ZXStage zxStage) {
        this.sideKey = sideKey;
        zxProjectManager = zxStage.zxProjectManager;
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