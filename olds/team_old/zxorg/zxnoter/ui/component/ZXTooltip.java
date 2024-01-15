package team.zxorg.zxnoter.ui.component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import team.zxorg.zxnoter.resource.GlobalResources;

public class ZXTooltip extends TrackTooltip {
    public ZXTooltip(Node bindNode, String tipLanguageKey) {
        TrackTooltip trackTooltip = new TrackTooltip(bindNode, Pos.TOP_CENTER, 0, TrackTooltip.BindAttributes.HOVER_POP_UP);
        trackTooltip.textProperty().bind(GlobalResources.getLanguageContent(tipLanguageKey));
    }

    public ZXTooltip(Node bindNode, Pos pos, String tipLanguageKey) {
        TrackTooltip trackTooltip = new TrackTooltip(bindNode, pos, 0, TrackTooltip.BindAttributes.HOVER_POP_UP);
        trackTooltip.textProperty().bind(GlobalResources.getLanguageContent(tipLanguageKey));
    }
}
