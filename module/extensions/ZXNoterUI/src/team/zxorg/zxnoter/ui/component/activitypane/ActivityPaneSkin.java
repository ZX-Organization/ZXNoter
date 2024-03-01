package team.zxorg.zxnoter.ui.component.activitypane;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.ProjectView;

public abstract class ActivityPaneSkin extends VBox {
    private ProjectView projectView;
    private ActivityItemSkin activityItemSkin;
    private ActivityPane activityPane;

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
    protected final ActivityItemSkin getActivityItem() {
        return activityItemSkin;
    }

    {
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    void bind(ProjectView projectView, ActivityPane activityPane) {
        this.projectView = projectView;
        this.activityPane = activityPane;
    }

    public ActivityPaneSkin(String id) {
        setId(id);
        activityItemSkin = new ActivityItemSkin(this);
    }


}
