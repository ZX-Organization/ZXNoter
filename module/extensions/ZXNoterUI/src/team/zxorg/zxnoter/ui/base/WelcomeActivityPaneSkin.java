package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPaneSkin;

public class WelcomeActivityPaneSkin extends ActivityPaneSkin {
    public WelcomeActivityPaneSkin() {
        super("welcome");
        setAlignment(Pos.CENTER);
        getChildren().addAll(new Label("欢迎"));
    }
}
