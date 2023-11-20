package team.zxorg.ui.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * 项目视图窗口
 */
public class ProjectView {

    /**
     * 项目视图组件
     */
    private static abstract class ProjectComponent {
        protected final ProjectView projectView;

        public ProjectComponent(ProjectView projectView) {
            this.projectView = projectView;
        }

        public abstract Node getNode();
    }

    /**
     * 标题栏
     */
    private final TitleBar titleBar = new TitleBar(this);


    /**
     * 内容容器
     */
    private final HBox contentBody = new HBox();

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


    /**
     * 标题栏定义
     */
    private static final class TitleBar extends ProjectComponent {
        private final ImageView titleIcon = new ImageView();
        private final MenuBar menuBar = new MenuBar();
        private final Label titleLabel = new Label("项目视图");
        private final HBox root = new HBox(titleIcon, menuBar, titleLabel);

        public TitleBar(ProjectView projectView) {
            super(projectView);
        }

        @Override
        public Node getNode() {
            return root;
        }
    }

    /**
     * 状态栏定义
     */
    private static class StatusBar extends ProjectComponent {
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
        private final HBox statusBar = new HBox(statusBarLeft, statusBarRight);

        public StatusBar(ProjectView projectView) {
            super(projectView);
        }

        @Override
        public Node getNode() {
            return statusBar;
        }
    }


    public ProjectView() {
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }
}
