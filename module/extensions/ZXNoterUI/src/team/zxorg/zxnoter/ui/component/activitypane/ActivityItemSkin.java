package team.zxorg.zxnoter.ui.component.activitypane;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.TrackLangTooltip;
import team.zxorg.zxnoter.ui.ZXNoter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public final class ActivityItemSkin extends ToggleButton {
    public static final DataFormat ACTIVITY_ITEM_DATA_FORMAT = new DataFormat("application/x-activity-item");

    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.item.";
    private final ActivityPaneSkin activityPaneSkin;
    private ActivityPane activityPane;
    private ActivityBarPaneSkin activityBarPaneSkin;
    private static final ActivityBarConfig config = ZXNoter.config.get(ActivityBarConfig.class);

    void bind(ActivityPane activityPane, ActivityBarPaneSkin activityBarPaneSkin) {
        this.activityPane = activityPane;
        this.activityBarPaneSkin = activityBarPaneSkin;
    }


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

    public ActivityPaneSkin getActivityPane() {
        return activityPaneSkin;
    }

    public String getNameKey() {
        return LANG + getId() + ".name";
    }

    public String getIconKey() {
        return Language.get(LANG + getId() + ".icon");
    }

    public ActivityItemSkin(ActivityPaneSkin activityPaneSkin) {
        this.activityPaneSkin = activityPaneSkin;
        getStyleClass().add("activity-item");
        //setToggleGroup(GROUP);
        setId(activityPaneSkin.getId());
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
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                activityPane.dragedActivityItemSkin = this;
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
            }
        });

        setOnDragOver(event -> {
            if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT))
                return;
            if (event.getGestureSource() == this) {
                event.consume();
                return;
            }

            if (activityPane.dragedActivityItemSkin == null)
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
            if (!event.getDragboard().getContentTypes().contains(ACTIVITY_ITEM_DATA_FORMAT))
                return;
            if (activityPane.dragedActivityItemSkin == null)
                return;
            ActivityItemSkin activityItemSkin = activityPane.dragedActivityItemSkin;
            config.mainTopBarItems.remove(activityItemSkin.getId());
            config.secondBarItems.remove(activityItemSkin.getId());
            config.mainBottomBarItems.remove(activityItemSkin.getId());

            LinkedHashSet<String> items = activityBarPaneSkin.getItems();

            List<String> list = new ArrayList<>(items);
            list.add(list.indexOf(getId()) + (dragStyle.get().equals(DragStyle.up) ? 0 : 1), activityItemSkin.getId());
            items.clear();
            items.addAll(list);

            activityPane.dragedActivityItemSkin = null;

            //立即更新
            config.save();

            /*if (getParent() instanceof ActivityBar activityBar) {
                ObservableList<Node> children = activityBar.getChildren();
                //目标索引
                int index = children.indexOf(this);

            }*/
            //System.out.println(event.getDragboard().getContent(ACTIVITY_ITEM_DATA_FORMAT));
            event.setDropCompleted(true);
            event.consume();
        });
        setOnDragEntered(event ->{
            getParent().getOnDragExited().handle(event);
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
