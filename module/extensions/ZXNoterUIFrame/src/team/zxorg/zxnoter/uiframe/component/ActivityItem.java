package team.zxorg.zxnoter.uiframe.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.TrackLangTooltip;


public final class ActivityItem extends ToggleButton {
    private static final DataFormat ACTIVITY_ITEM_DATA_FORMAT = new DataFormat("application/x-activity-item");

    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.item.";
    private static final ToggleGroup GROUP = new ToggleGroup();


    private final Icon icon;

    private final ObjectProperty<DragStyle> dragStyle = new SimpleObjectProperty<>(null) {
        {
            addListener((observable, oldValue, newValue) -> {
                ObservableList<String> style = getStyleClass();
                if (newValue != null) {
                    style.add(newValue.toString());
                }
                if (oldValue != null)
                    style.remove(oldValue.toString());
            });
        }
    };

    public Icon getIcon() {
        return icon;
    }


    public String getNameKey() {
        return LANG + getId() + ".name";
    }

    public String getIconKey() {
        return Language.get(LANG + getId() + ".icon");
    }

    public ActivityItem(String id) {
        getStyleClass().add("activity-item");
        setToggleGroup(GROUP);
        setId(id);
        this.icon = new Icon(getIconKey());
        setGraphic(icon);
        //setContextMenu(ActivityItemContextMenu);

        //设置右键活动项的菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                //activityItemContextMenu.setId(getId());
                //activityItemContextMenu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
        TrackLangTooltip tooltip = new TrackLangTooltip(getNameKey());
        tooltip.setBindNode(this);
        tooltip.setPos(Pos.CENTER_RIGHT, false, -4);
        tooltip.setAutoHide(false);
        setTooltip(tooltip);


        setOnDragDetected(event -> {
            // 创建快照
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT); // 快照背景透明
            Image snapshot = snapshot(params, null);

            // 开始拖拽操作
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            // 将自定义数据格式作为拖拽的内容
            content.put(ACTIVITY_ITEM_DATA_FORMAT, getId());
            dragboard.setContent(content);
            // 设置拖拽时的图标
            dragboard.setDragView(snapshot, getWidth() / 2, getHeight() / 2);
            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() == this)
                return;

            /*if (getParent() instanceof ActivityBar activityBar) {

            }*/

            double h = getHeight();
            double y = event.getY();
            if (y < h / 2) {
                dragStyle.set(DragStyle.up);
            } else {
                dragStyle.set(DragStyle.down);
            }

            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });

        setOnDragDropped(event -> {
            if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT)) {
                return;
            }

            /*if (getParent() instanceof ActivityBar activityBar) {
                ObservableList<Node> children = activityBar.getChildren();
                //目标索引
                int index = children.indexOf(this);

            }*/
            System.out.println(event.getDragboard().getContent(ACTIVITY_ITEM_DATA_FORMAT));
            event.setDropCompleted(true);
            event.consume();
        });

        setOnDragExited(event -> dragStyle.set(null));
    }

    private enum DragStyle {
        up, down;

        @Override
        public String toString() {
            return "drag-" + name();
        }
    }
}
