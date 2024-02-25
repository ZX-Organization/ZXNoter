package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;

public class SetupActivityPane extends ActivityPane {

    public SetupActivityPane() {
        super("setup");
        getChildren().addAll(new Label("设置"));
        setAlignment(Pos.CENTER);
    }
}
