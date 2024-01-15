package team.zxorg.zxnoter.ui.component;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class TrackPopupVHBox extends TrackTooltip {
    private final ZXVHBox body = new ZXVHBox();
    {
        setGraphic(body.getBox());
    }

    public TrackPopupVHBox(Node bindNode, Pos pos, double offset, BindAttributes... attributes) {
        super(bindNode, pos, offset, attributes);
    }

    public TrackPopupVHBox(Orientation orientation,Node bindNode, Pos pos, double offset, BindAttributes... attributes) {
        super(bindNode, pos, offset, attributes);
        setOrientation(orientation);
    }


    public void showTrackTooltip() {
        show(bindNode.getScene().getWindow());
    }


    public void setOrientation(Orientation orientation) {
        body.setOrientation(orientation);
        setGraphic(body.getBox());
    }

    public ObservableList<Node> getChildren() {
        return body.getChildren();
    }

    public double getSpacing() {
        return body.getSpacing();
    }

    public void setSpacing(double spacing) {
        body.setSpacing(spacing);
    }

    public void setAlignment(Pos pos) {
        body.setAlignment(pos);
    }

    public void setPadding(Insets padding) {
        body.setPadding(padding);
    }

}
