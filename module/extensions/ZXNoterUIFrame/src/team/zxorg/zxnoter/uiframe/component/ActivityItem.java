package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.fxcl.component.Icon;

import static team.zxorg.zxnoter.uiframe.component.ActivityBar.ActivityItemContextMenu;

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
        this.id =  id;
        this.icon = new Icon(iconKey, 32);
        setGraphic(icon);
        setContextMenu(ActivityItemContextMenu);
    }
}
