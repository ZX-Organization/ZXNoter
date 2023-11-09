package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ZXStatus extends HBox {
    {
        setAlignment(Pos.CENTER);
        getStyleClass().addAll("status");
        setPadding(new Insets(0, 4, 0, 4));
        setSpacing(4);
    }

    public ZXStatus(Node... children) {
        super(children);
    }
}
