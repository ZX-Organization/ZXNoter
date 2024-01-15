package team.zxorg.zxnoter.ui.component;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ZXVHBox {
    private HBox hBox = new HBox();
    private VBox vBox = new VBox();
    private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);

    {
        hBox.spacingProperty().bindBidirectional(vBox.spacingProperty());
        hBox.paddingProperty().bindBidirectional(vBox.paddingProperty());
        hBox.prefWidthProperty().bindBidirectional(vBox.prefWidthProperty());
        hBox.prefHeightProperty().bindBidirectional(vBox.prefHeightProperty());
        hBox.minWidthProperty().bindBidirectional(vBox.minWidthProperty());
        hBox.minHeightProperty().bindBidirectional(vBox.minHeightProperty());
        hBox.maxWidthProperty().bindBidirectional(vBox.maxWidthProperty());
        hBox.maxHeightProperty().bindBidirectional(vBox.maxHeightProperty());


        orientation.addListener((observable, oldValue, newValue) -> {
            Pane lastPane = getBox();
            Pane newPane = (newValue.equals(Orientation.HORIZONTAL) ? hBox : vBox);
            newPane.getChildren().addAll(lastPane.getChildren());
            lastPane.getChildren().clear();
        });
    }


    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }

    public Pane getBox() {
        return (orientation.get().equals(Orientation.HORIZONTAL) ? hBox : vBox);
    }

    public ObservableList<Node> getChildren() {
        return getBox().getChildren();
    }

    public double getSpacing() {
        if (getBox() instanceof VBox) return ((VBox) getBox()).getSpacing();
        if (getBox() instanceof HBox) return ((HBox) getBox()).getSpacing();
        return 0;
    }

    public void setSpacing(double spacing) {
        if (getBox() instanceof VBox) ((VBox) getBox()).setSpacing(spacing);
        if (getBox() instanceof HBox) ((HBox) getBox()).setSpacing(spacing);
    }

    public void setAlignment(Pos pos) {
        if (getBox() instanceof VBox) ((VBox) getBox()).setAlignment(pos);
        if (getBox() instanceof HBox) ((HBox) getBox()).setAlignment(pos);
    }

    public void setPadding(Insets padding) {
        getBox().setPadding(padding);
    }
}
