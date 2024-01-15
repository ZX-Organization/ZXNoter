package team.zxorg.ui.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.ui.component.CanvasPane;
import team.zxorg.ui.javafx.sub.*;
import team.zxorg.ui.javafx.sub.editor.flexeditor.FlexArea;
import team.zxorg.ui.javafx.sub.editor.flexeditor.FlexSplitPane;
import team.zxorg.ui.javafx.sub.editor.flexeditor.FlexTab;
import team.zxorg.ui.javafx.sub.editor.flexeditor.FlexTabPane;

import java.nio.file.Path;


/**
 * 项目视图窗口
 */
public class ProjectView extends FunctionalComponent {


    /**
     * 标题栏
     */
    private final TitleBar titleBar = new TitleBar(this);
    /**
     * 活动栏
     */
    private final ActivityBar activityBar = new ActivityBar(this);


    private final SideBar sideBar = new SideBar(this);

    private final FlexArea editorArea = new FlexArea();
    /**
     * 状态栏
     */
    private final StatusBar statusBar = new StatusBar(this);
    /**
     * 窗口
     */
    private final Stage stage = new Stage();
    /**
     * 工作区
     */
    SplitPane workArea = new SplitPane(sideBar.getNode(), editorArea) {
        {
            getStyleClass().addAll("work-area");
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    };
    /**
     * 内容容器
     */
    private final HBox contentBody = new HBox(activityBar.getNode(), workArea) {
        {
            getStyleClass().addAll("content-body");
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    };
    /**
     * 根
     */
    private final VBox root = new VBox(titleBar.getNode(), contentBody, statusBar.getNode());

    /**
     * 场景
     */
    private final Scene scene = new Scene(root);

    {
        //测试区域

        {
            FlexTabPane tabPane = editorArea.createTabPane();
            Tab tab = new Tab();
            tab.setText("111");
            tabPane.addTab(tab);
            VBox hBox = new VBox(new TextField(), new Button(), new TextArea(), new CanvasPane());
            tab.setContent(hBox);
        }

        FlexSplitPane splitPane = editorArea.createSplitPane();

        {
            FlexTabPane tabPane = splitPane.createTabPane();
            FlexTab tab = new FlexTab(Path.of("project")) {
            };
            tab.setText("222");
            tabPane.addTab(tab);
        }
        {
            FlexTabPane tabPane = splitPane.createTabPane();
            {
                FlexTab tab = new FlexTab(Path.of("project")) {
                };
                tab.setText("333");
                tabPane.addTab(tab);
            }
            {
                FlexTab tab = new FlexTab(Path.of("project")) {
                };
                tab.setText("444");
                tabPane.addTab(tab);
                tab.setContent(new TextArea());
            }
            Platform.runLater(() -> {
                {
                    FlexTab tab = new FlexTab(Path.of("project")) {
                    };
                    tab.setText("555");
                    tabPane.addTab(tab);
                    tab.setContent(new TextArea());
                }
            });
        }
    }


    public ProjectView() {
        super(null, null, "projectView");
        scene.getStylesheets().addAll("resources/baseExpansionPack/color/style.css");
        scene.getStylesheets().addAll("resources/baseExpansionPack/color/dark.css");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    public void show() {
        stage.show();
    }


    @Override
    public Node getNode() {
        return null;
    }
}
