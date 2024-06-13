package team.zxorg.zxnoter.ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.extensionloader.core.Configuration;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.core.Version;
import team.zxorg.extensionloader.event.ResourceEventListener;
import team.zxorg.fxcl.component.flexview.FlexArea;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;
import team.zxorg.zxnoter.ui.component.StatusBar;
import team.zxorg.zxnoter.ui.component.titlebar.TitleBar;

import java.nio.file.Path;


/**
 * 项目视图窗口
 */
public class ProjectView {

    /**
     * 标题栏
     */
    private final TitleBar titleBar;

    public Stage getStage() {
        return stage;
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    /**
     * 视图区域
     */
    private final FlexArea viewArea;
    /**
     * 活动栏面板
     */
    private final ActivityPane activityBar;

    /**
     * 状态栏
     */
    private final StatusBar statusBar;
    /**
     * 窗口
     */
    private final Stage stage = new Stage();

    /**
     * 根
     */
    private final VBox root;

    /**
     * 场景
     */
    private final Scene scene;

    private final ResourceEventListener loadStyle = new ResourceEventListener() {
        @Override
        public void onReload() {
            for (Path file : Resource.listResourceFiles("style")) {
                scene.getStylesheets().addAll(Resource.getResourceToUrl(file).toString());
            }
        }
    };

    public ProjectView() {

        titleBar = new TitleBar(this);
        viewArea = new FlexArea() {
            {
                getStyleClass().addAll("view-area");
                createTabPane().addTab(new Tab());
                createSplitPane().createTabPane().addTab(new Tab());
            }
        };
        activityBar = new ActivityPane(this, viewArea);
        statusBar = new StatusBar(this);
        root = new VBox(titleBar, activityBar, statusBar);
        scene = new Scene(root);

        Resource.addEventListener(loadStyle);
        loadStyle.onReload();
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("ZXNoter");


        stage.setOnCloseRequest(event -> {
            Configuration.save();
        });
    }

    public void show() {
        stage.show();
    }


}
