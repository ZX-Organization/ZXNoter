package team.zxorg.zxnoter.uiframe.component;

import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ActivityBar extends VBox {
    VBox top = new VBox() {
        {
            setAlignment(Pos.TOP_CENTER);
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    };
    VBox bottom = new VBox() {
        {
            setAlignment(Pos.BOTTOM_CENTER);
        }
    };

    {
        getChildren().addAll(top, bottom);
        getStyleClass().addAll("activity-bar");
    }


}
