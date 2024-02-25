package team.zxorg.zxnoter.ui.component;

import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.ui.ProjectView;

/**
 * 状态栏定义
 */
public class StatusBar extends HBox{
    /**
     * 状态栏左部分
     */
    private final HBox statusBarLeft = new HBox();
    /**
     * 状态栏右部分
     */
    private final HBox statusBarRight = new HBox();


    private final ProjectView projectView;

    public StatusBar(ProjectView projectView) {
        this.projectView = projectView;
    }

    {
        getStyleClass().addAll("status-bar");
    }

}
