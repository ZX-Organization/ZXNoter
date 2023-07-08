package team.zxorg.zxnoter.ui.component;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TrackTooltip extends Tooltip {
    Node bindNode;

    public void setBindNode(Node bindNode) {
        this.bindNode = bindNode;
    }


    public TrackTooltip() {

    }

    public TrackTooltip(Node bindNode, Pos pos, double offset, BindAttributes... attributes) {
        this.bindNode = bindNode;
        setPos(pos, false, offset);
        for (BindAttributes attr : attributes) {
            switch (attr) {
                case AUTO_POP_UP -> Tooltip.install(bindNode, this);
                case POP_UP_INSTANTLY -> setShowDelay(Duration.ZERO);
                case NOT_DISAPPEAR -> setShowDuration(Duration.INDEFINITE);
            }
        }
    }


    public void copyNodeSize() {
        Bounds bounds = bindNode.localToScreen(bindNode.getBoundsInLocal());
        setMinSize(bounds.getWidth(), bounds.getHeight());
        setPrefSize(bounds.getWidth(), bounds.getHeight());
        setMaxSize(bounds.getWidth(), bounds.getHeight());
    }

    public void setPos(Pos pos, boolean isInternal, double offset) {
        setOnShown(event -> {
            Bounds bounds = bindNode.localToScreen(bindNode.getBoundsInLocal());
            setY(switch (pos.getVpos()) {
                case TOP, BASELINE -> bounds.getMinY() - (isInternal ? 0 : getHeight()) - offset;
                case CENTER -> bounds.getCenterY() - getHeight() / 2;
                case BOTTOM -> bounds.getMaxY() - (!isInternal ? 0 : getHeight()) + offset;
            });
            setX(switch (pos.getHpos()) {
                case LEFT -> bounds.getMinX() - (isInternal ? 0 : getWidth()) - offset;
                case CENTER -> bounds.getCenterX() - getWidth() / 2;
                case RIGHT -> bounds.getMaxX() - (!isInternal ? 0 : getWidth()) + offset;
            });
        });
    }


    public void showTrackTooltip() {
        show(bindNode.getScene().getWindow());
    }

    public enum BindAttributes {
        AUTO_POP_UP,
        POP_UP_INSTANTLY,
        NOT_DISAPPEAR
    }
}
