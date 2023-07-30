package team.zxorg.zxnoter.ui.main.stage;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.ui.main.ZXStage;

public class StatusBar extends HBox {
    public StatusBar(ZXStage zxStage) {
        getStyleClass().add("status-bar");

        getChildren().add(new Label("WCNMD"));
    }
}
