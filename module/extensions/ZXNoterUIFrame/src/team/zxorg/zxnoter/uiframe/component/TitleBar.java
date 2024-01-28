package team.zxorg.zxnoter.uiframe.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.menu.LangMenu;

/**
 * 标题栏定义
 */
public final class TitleBar extends HBox {
    private static final String LANG = "zxnoterUiFrame.projectView.titleBar.";
    private final Icon titleIcon = new Icon(Language.get(LANG + "icon"), 22){
        {
            getStyleClass().add("title-icon");
        }
    };
    private final MenuBar menuBar = new MenuBar(new LangMenu("aaa"));
    private final Label titleLabel = new Label("ZXNoter 项目视图") {
        {
            HBox.setHgrow(this, Priority.ALWAYS);
            setMaxWidth(Double.MAX_VALUE);
            setAlignment(Pos.CENTER);
        }
    };

    {
        getChildren().addAll(titleIcon, menuBar, titleLabel);
        getStyleClass().addAll("title-bar");
    }


}