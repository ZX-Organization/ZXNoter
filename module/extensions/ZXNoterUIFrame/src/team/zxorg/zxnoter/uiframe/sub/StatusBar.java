package team.zxorg.zxnoter.uiframe.sub;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.uiframe.ProjectView;

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
    {
        getStyleClass().addAll("status-bar");
    }

}
