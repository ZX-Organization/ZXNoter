package team.zxorg.ui.javafx.sub;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import team.zxorg.ui.javafx.ProjectView;

public class SideBar extends FunctionalComponent {
    protected VBox sideBar = new VBox() {
        {
            SplitPane.setResizableWithParent(this, false);
            getStyleClass().addAll("side-bar");
        }
    };

    @Override
    public Node getNode() {
        return sideBar;
    }

    public SideBar(ProjectView projectView) {
        super(projectView, projectView, "sideBar");
    }
}
