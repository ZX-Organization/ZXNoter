package team.zxorg.zxnoter.ui.component.activitypane;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.ProjectView;

public abstract class ActivityPane extends VBox {
    private ProjectView projectView;
    private ActivityItem activityItem;
    private ActivityBarPane activityBarPane;

    /**
     * 获取项目视图
     *
     * @return 项目视图
     */
    public final ProjectView getProjectView() {
        return projectView;
    }

    /**
     * 获取活动项
     *
     * @return 活动项
     */
    protected final ActivityItem getActivityItem() {
        return activityItem;
    }

    {
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    void bind(ProjectView projectView, ActivityBarPane activityBarPane) {
        this.projectView = projectView;
        this.activityBarPane = activityBarPane;
    }

    public ActivityPane(String id) {
        setId(id);
        activityItem = new ActivityItem(this);
    }


}
