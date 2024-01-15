package team.zxorg.fxcl.javafx.sub;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.fxcl.javafx.ProjectView;

public class ActivityBar extends FunctionalComponent {
    VBox top = new VBox() {
        {
            setAlignment(Pos.TOP_CENTER);
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    };
    VBox bottom = new VBox() {
        {
            setAlignment(Pos.BOTTOM_CENTER);
        }
    };
    VBox activityBar = new VBox(top, bottom) {
        {
            getStyleClass().addAll("activity-bar");
        }
    };

    public ActivityBar(ProjectView projectView) {
        super(projectView, projectView, "activityBar");
    }

    @Override
    public Node getNode() {
        return activityBar;
    }
}
