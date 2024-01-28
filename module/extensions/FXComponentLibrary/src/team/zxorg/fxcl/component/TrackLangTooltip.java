package team.zxorg.fxcl.component;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TrackLangTooltip extends LangTooltip {

    Node bindNode;

    public void setBindNode(Node bindNode) {
        this.bindNode = bindNode;
    }


    public TrackLangTooltip(String key, Property<?>... args) {
        super(key, args);
    }

    public TrackLangTooltip(Node bindNode, Pos pos, double offset, BindAttributes... attributes) {
        super();
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
                case HOVER_EXIT_DISAPPEAR -> {
                    Platform.runLater(() -> getGraphic().setOnMouseExited(event -> hide()));
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

    /**
     * 设置显示位置
     *
     * @param pos        位置
     * @param isInternal 在内部
     * @param offset     偏移
     */
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
        HOVER_EXIT_DISAPPEAR,//悬停退出消失
    }
}
