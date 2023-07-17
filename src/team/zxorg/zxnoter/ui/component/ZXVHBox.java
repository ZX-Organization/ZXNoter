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
            Pane lastPane = pane;
            double spacing = getSpacing();
            if (newValue.equals(Orientation.HORIZONTAL)) {
                pane = new HBox();
            } else if (newValue.equals(Orientation.VERTICAL)) {
                pane = new VBox();
            }
            pane.getChildren().addAll(lastPane.getChildren());
            lastPane.getChildren().clear();
            pane.setPadding(lastPane.getPadding());
            pane.setPrefSize(lastPane.getPrefWidth(), lastPane.getPrefHeight());
            pane.setMinSize(lastPane.getMinWidth(), lastPane.getMinHeight());
            pane.setMaxSize(lastPane.getMaxWidth(), lastPane.getMaxHeight());
            setSpacing(spacing);
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

    public double getSpacing() {
        if (pane instanceof VBox) return ((VBox) pane).getSpacing();
        if (pane instanceof HBox) return ((HBox) pane).getSpacing();
        return 0;
    }

    public void setSpacing(double spacing) {
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
