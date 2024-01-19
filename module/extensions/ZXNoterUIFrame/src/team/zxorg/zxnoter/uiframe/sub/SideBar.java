package team.zxorg.zxnoter.uiframe.sub;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.uiframe.ProjectView;

public class SideBar extends VBox {
    {
        SplitPane.setResizableWithParent(this, false);
        getStyleClass().addAll("side-bar");
    }

}
