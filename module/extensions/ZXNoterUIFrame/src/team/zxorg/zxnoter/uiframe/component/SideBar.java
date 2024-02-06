package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public abstract class SideBar extends VBox {
    ActivityItem activityItem;


    public SideBar(String id) {
        setId(id);
        this.activityItem = new ActivityItem(id);
    }

    {
        SplitPane.setResizableWithParent(this, false);
        getStyleClass().addAll("side-bar");
    }

}
