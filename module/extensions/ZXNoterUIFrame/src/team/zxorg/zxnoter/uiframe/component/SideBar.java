package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public abstract class SideBar extends VBox {

    {
        SplitPane.setResizableWithParent(this, false);
        getStyleClass().addAll("side-bar");
    }
}
