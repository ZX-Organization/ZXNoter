package team.zxorg.zxnoter.uiframe;

import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.fxcl.component.flexview.FlexArea;
import team.zxorg.zxnoter.uiframe.component.ActivityBar;
import team.zxorg.zxnoter.uiframe.component.SideBar;
import team.zxorg.zxnoter.uiframe.component.StatusBar;
import team.zxorg.zxnoter.uiframe.component.TitleBar;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


/**
 * 项目视图窗口
 */
public class ProjectViewStage {
    public static Map<String, SideBar> sideBarMap = new HashMap<>();


    /**
     * 标题栏
     */
    private final TitleBar titleBar = new TitleBar();
    /**
     * 活动栏
     */
    private final ActivityBar activityBar = new ActivityBar();


    /**
     * 视图区域
     */
    private final FlexArea viewArea = new FlexArea();
    /**
     * 状态栏
     */
    private final StatusBar statusBar = new StatusBar();
    /**
     * 窗口
     */
    private final Stage stage = new Stage();
    /**
     * 工作区
     */
    SplitPane workArea = new SplitPane(viewArea) {
        {
            getStyleClass().addAll("work-area");
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    };
    /**
     * 内容容器
     */
    private final HBox contentBody = new HBox(activityBar, workArea) {
        {
            getStyleClass().addAll("content-body");
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    };
    /**
     * 根
     */
    private final VBox root = new VBox(titleBar, contentBody, statusBar);

    /**
     * 场景
     */
    private final Scene scene = new Scene(root);

    private final ResourceEventListener loadStyle = new ResourceEventListener() {
        @Override
        public void onReload() {
            for (Path file : Resource.listResourceFiles("style")) {
                scene.getStylesheets().addAll(Resource.getResourceToUrl(file).toString());
            }
        }
    };

    public ProjectViewStage() {
        Resource.addEventListener(loadStyle);
        loadStyle.onReload();
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    public void show() {
        stage.show();
    }


}
