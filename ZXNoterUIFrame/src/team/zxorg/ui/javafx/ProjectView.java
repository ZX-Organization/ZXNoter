package team.zxorg.ui.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.ui.javafx.editor.EditorTabPane;
import team.zxorg.ui.javafx.editor.flexeditor.FlexEditorArea;
import team.zxorg.ui.javafx.editor.flexeditor.FlexEditorSplitPane;
import team.zxorg.ui.javafx.editor.flexeditor.FlexEditorTab;
import team.zxorg.ui.javafx.editor.flexeditor.FlexEditorTabPane;

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

    private final FlexEditorArea editorArea = new FlexEditorArea();

    {
        //测试区域

        {
            FlexEditorTabPane tabPane = editorArea.createTabPane();
            FlexEditorTab tab = new FlexEditorTab(editorArea, Path.of("project")) {
            };
            tab.setText("NMSL");
            tabPane.addTab(tab);
        }

        FlexEditorSplitPane splitPane = editorArea.createSplitPane();

        {
            FlexEditorTabPane tabPane = splitPane.createTabPane();
            FlexEditorTab tab = new FlexEditorTab(editorArea, Path.of("project")) {
            };
            tab.setText("NMSL");
            tabPane.addTab(tab);
        }
        {
            FlexEditorTabPane tabPane = splitPane.createTabPane();
            {
                FlexEditorTab tab = new FlexEditorTab(editorArea, Path.of("project")) {
                };
                tab.setText("NMSL");
                tabPane.addTab(tab);
            }
            {
                FlexEditorTab tab = new FlexEditorTab(editorArea, Path.of("project")) {
                };
                tab.setText("NMSL");
                tabPane.addTab(tab);
            }
        }
    }


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
     * 状态栏
     */
    private final StatusBar statusBar = new StatusBar(this);

    /**
     * 根
     */
    private final VBox root = new VBox(titleBar.getNode(), contentBody, statusBar.getNode());

    /**
     * 场景
     */
    private final Scene scene = new Scene(root);
    /**
     * 窗口
     */
    private final Stage stage = new Stage();


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
