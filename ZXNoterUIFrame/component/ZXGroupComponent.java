package team.zxorg.ui.component;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ZXGroupComponent extends HBox {
    {
        getStyleClass().add("group-component");
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0,4,0,4));
    }
}
