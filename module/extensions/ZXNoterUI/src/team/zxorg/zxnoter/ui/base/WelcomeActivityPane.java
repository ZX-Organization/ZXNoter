package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;

public class WelcomeActivityPane extends ActivityPane {
    public WelcomeActivityPane() {
        super("welcome");
        setAlignment(Pos.CENTER);
        getChildren().addAll(new Label("欢迎"));
    }
}
