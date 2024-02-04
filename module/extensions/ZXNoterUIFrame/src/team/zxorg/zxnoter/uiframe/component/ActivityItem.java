package team.zxorg.zxnoter.uiframe.component;

import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.TrackLangTooltip;

import static team.zxorg.zxnoter.uiframe.component.ActivityBar.activityItemContextMenu;

public final class ActivityItem extends ToggleButton {
    public static final String LANG = "zxnoterUiFrame.projectView.activityBar.item.";
    private static final ToggleGroup GROUP = new ToggleGroup();
    Extension extension;
    Icon icon;

    public Extension getExtension() {
        return extension;
    }


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
        this.icon = new Icon(getIconKey(), 32);
        setGraphic(icon);
        //setContextMenu(ActivityItemContextMenu);

        //设置右键活动项的菜单
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                activityItemContextMenu.setId(getId());
                activityItemContextMenu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
        TrackLangTooltip tooltip = new TrackLangTooltip(getNameKey());
        tooltip.setBindNode(this);
        tooltip.setPos(Pos.CENTER_RIGHT, false, -4);
        tooltip.setAutoHide(false);
        setTooltip(tooltip);
    }
}
