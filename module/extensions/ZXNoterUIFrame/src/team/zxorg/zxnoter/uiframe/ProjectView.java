package team.zxorg.zxnoter.uiframe;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.fxcl.component.flexview.FlexArea;
import team.zxorg.zxnoter.uiframe.activitypane.ActivityBarPane;
import team.zxorg.zxnoter.uiframe.component.StatusBar;
import team.zxorg.zxnoter.uiframe.component.TitleBar;

import java.nio.file.Path;


/**
 * 项目视图窗口
 */
public class ProjectView {

    /**
     * 标题栏
     */
    private final TitleBar titleBar = new TitleBar(this);

    /**
     * 视图区域
     */
    private final FlexArea viewArea = new FlexArea() {
        {
            getStyleClass().addAll("view-area");
            createTabPane().addTab(new Tab());
            createSplitPane().createTabPane().addTab(new Tab());
        }
    };
    /**
     * 活动栏面板
     */
    private final ActivityBarPane activityBar = new ActivityBarPane(this, viewArea);

    /**
     * 状态栏
     */
    private final StatusBar statusBar = new StatusBar(this);
    /**
     * 窗口
     */
    private final Stage stage = new Stage();

    /**
     * 根
     */
    private final VBox root = new VBox(titleBar, activityBar, statusBar);

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

    public ProjectView() {
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
