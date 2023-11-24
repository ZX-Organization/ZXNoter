package team.zxorg.ui.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 标题栏定义
 */
public final class TitleBar extends FunctionalComponent {
    private final ImageView titleIcon = new ImageView();
    private final MenuBar menuBar = new MenuBar();
    private final Label titleLabel = new Label("项目视图");
    private final HBox titleBar = new HBox(titleIcon, menuBar, titleLabel){
        {
            getStyleClass().addAll("title-bar");
        }
    };

    public TitleBar(ProjectView projectView) {
        super(projectView, projectView, "titleBar");
    }

    @Override
    public Node getNode() {
        return titleBar;
    }
}