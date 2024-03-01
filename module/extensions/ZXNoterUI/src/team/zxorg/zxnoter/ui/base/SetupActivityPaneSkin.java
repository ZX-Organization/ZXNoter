package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPaneSkin;

public class SetupActivityPaneSkin extends ActivityPaneSkin {

    public SetupActivityPaneSkin() {
        super("setup");
        getChildren().addAll(new Label("设置"));
        setAlignment(Pos.CENTER);
    }
}
