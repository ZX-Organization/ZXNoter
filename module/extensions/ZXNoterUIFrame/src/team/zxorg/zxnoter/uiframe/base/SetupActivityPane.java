package team.zxorg.zxnoter.uiframe.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.uiframe.activitypane.ActivityPane;

public class SetupActivityPane extends ActivityPane {

    public SetupActivityPane() {
        super("setup");
        getChildren().addAll(new Label("设置"));
        setAlignment(Pos.CENTER);
    }
}
