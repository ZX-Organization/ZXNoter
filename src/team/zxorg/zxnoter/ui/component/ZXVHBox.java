package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ZXVHBox {
    Pane pane = new HBox();
    ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);

    {
        orientation.addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> children = pane.getChildren();
            if (newValue.equals(Orientation.HORIZONTAL)) {
                pane = new HBox();
            } else if (newValue.equals(Orientation.VERTICAL)) {
                pane = new VBox();
            }
            pane.getChildren().addAll(children);
            children.clear();
        });
    }

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Pane getBox() {
        return pane;
    }

    public ObservableList<Node> getChildren() {
        return pane.getChildren();
    }

    public void setSpacing(int spacing) {
        if (pane instanceof VBox) ((VBox) pane).setSpacing(spacing);
        if (pane instanceof HBox) ((HBox) pane).setSpacing(spacing);
    }

    public void setAlignment(Pos pos) {
        if (pane instanceof VBox) ((VBox) pane).setAlignment(pos);
        if (pane instanceof HBox) ((HBox) pane).setAlignment(pos);
    }

    public void setPadding(Insets padding) {
        pane.setPadding(padding);
    }
}
