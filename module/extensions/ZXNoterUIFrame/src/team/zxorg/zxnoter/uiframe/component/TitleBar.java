package team.zxorg.zxnoter.uiframe.component;

import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 标题栏定义
 */
public final class TitleBar extends HBox {
    private final ImageView titleIcon = new ImageView();
    private final MenuBar menuBar = new MenuBar();
    private final Label titleLabel = new Label("项目视图");

    {
        getChildren().addAll(titleIcon, menuBar, titleLabel);
        getStyleClass().addAll("title-bar");
    }


}