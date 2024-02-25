package team.zxorg.zxnoter.ui.component.titlebar;

import javafx.geometry.Pos;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.LangLabel;
import team.zxorg.fxcl.component.menu.LangMenuItem;
import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.factory.MenuFactory;

import java.util.LinkedHashMap;

/**
 * 标题栏定义
 */
public final class TitleBar extends HBox {

    private static final LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LangMenuItem>>> register = new LinkedHashMap<>();



    private final ProjectView projectView;

    public TitleBar(ProjectView projectView) {
        this.projectView = projectView;
    }

    private static final String LANG = "zxnoterUiFrame.projectView.titleBar.";
    private final Icon titleIcon = new Icon(Language.get(LANG + "icon"), 22) {
        {
            getStyleClass().add("title-icon");
        }
    };
    private final MenuBar menuBar = new MenuBar(MenuFactory.getMenu(LANG + "menuBar.file",MenuFactory.getMenu(LANG + "menuBar.file")), MenuFactory.getMenu(LANG + "menuBar.edit"), MenuFactory.getMenu(LANG + "menuBar.help"));
    private final LangLabel titleLabel = new LangLabel(LANG + "title") {
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