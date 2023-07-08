package team.zxorg.zxnoter.ui.main.one.two;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar extends HBox {
    public StatusBar() {
        getStyleClass().add("status-bar");

        getChildren().add(new Label("WCNMD"));
    }
}
