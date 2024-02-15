package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.uiframe.ProjectView;

public abstract class ActivitySideBar extends VBox {
    private ProjectView projectView;
    private ActivityItem activityItem;

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

    protected void bind(ProjectView projectView, ActivityItem activityItem) {
        this.projectView = projectView;
        this.activityItem = activityItem;
    }

    /**
     * 初始化活动侧边栏
     *
     * @param id ID
     */
    protected final void init(String id) {
        setId(id);
    }

}
