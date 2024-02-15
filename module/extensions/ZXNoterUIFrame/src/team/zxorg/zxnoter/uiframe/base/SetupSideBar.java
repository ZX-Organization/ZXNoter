package team.zxorg.zxnoter.uiframe.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.uiframe.component.ActivitySideBar;

public class SetupSideBar extends ActivitySideBar {

    public SetupSideBar() {

        getChildren().addAll(new Label("设置"));
        setAlignment(Pos.CENTER);
        init("setup");
    }
}
