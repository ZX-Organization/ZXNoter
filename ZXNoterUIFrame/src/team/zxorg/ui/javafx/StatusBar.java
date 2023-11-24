package team.zxorg.ui.javafx;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * 状态栏定义
 */
public class StatusBar extends FunctionalComponent {
    /**
     * 状态栏左部分
     */
    private final HBox statusBarLeft = new HBox();
    /**
     * 状态栏右部分
     */
    private final HBox statusBarRight = new HBox();
    /**
     * 状态栏容器
     */
    private final HBox statusBar = new HBox(statusBarLeft, statusBarRight){
        {
            getStyleClass().addAll("status-bar");
        }
    };

    public StatusBar(ProjectView projectView) {
        super(projectView, projectView, "statusBar");
    }

    @Override
    public Node getNode() {
        return statusBar;
    }
}
