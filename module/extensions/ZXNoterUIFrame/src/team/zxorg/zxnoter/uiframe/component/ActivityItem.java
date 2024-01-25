package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.fxcl.component.Icon;

import static team.zxorg.zxnoter.uiframe.component.ActivityBar.activityItemContextMenu;

public abstract class ActivityItem extends ToggleButton {
    private static final ToggleGroup GROUP = new ToggleGroup();
    Extension extension;
    String id;
    Icon icon;

    public Extension getExtension() {
        return extension;
    }

    public String getItemId() {
        return id;
    }

    public Icon getIcon() {
        return icon;
    }


    public ActivityItem() {
        getStyleClass().add("activity-item");
        setToggleGroup(GROUP);
    }

    protected void init(Extension extension, String id, String iconKey) {
        this.extension = extension;
        this.id = id;
        this.icon = new Icon(iconKey, 32);
        setGraphic(icon);
        //setContextMenu(ActivityItemContextMenu);

        //设置右键活动项的菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                activityItemContextMenu.setId(getItemId());
                activityItemContextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
        });
    }
}
