package team.zxorg.zxnoter.ui.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
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
                case HOVER_POP_UP -> Tooltip.install(bindNode, this);
                case POP_UP_INSTANTLY -> setShowDelay(Duration.ZERO);
                case NOT_DISAPPEAR -> setShowDuration(Duration.INDEFINITE);
                case CLICK_POP_UP -> bindNode.setOnMouseClicked(event -> showTrackTooltip());
                case LOSE_FOCUS_DISAPPEAR -> {
                    setAutoHide(true);
                    setAutoFix(true);
                }
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
        HOVER_POP_UP,//悬停弹出
        POP_UP_INSTANTLY,//弹出内部
        NOT_DISAPPEAR,//不消失
        CLICK_POP_UP,//点击弹出

        LOSE_FOCUS_DISAPPEAR,//丢失焦点消失
    }
}
