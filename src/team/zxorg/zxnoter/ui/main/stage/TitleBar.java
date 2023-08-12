package team.zxorg.zxnoter.ui.main.stage;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.menu.TitleBarFileMenu;

public class TitleBar extends HBox {
    //菜单栏
    public MenuBar menuBar = new MenuBar();
    ZXProject zxProject;

    public TitleBar(ZXStage zxStage) {
        this.zxProject = zxStage.zxProject;
        //标题栏
        setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.LOGO_X26);//ZXResources.getImage("img.zxnoter.zxnoter-x26")
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        //菜单栏
        menuBar.setPadding(new Insets(0));
        getChildren().addAll(zxnIcon, menuBar);


        menuBar.getMenus().addAll(new TitleBarFileMenu(zxProject));


    }

    public ObservableList<Menu> getMenus() {
        return menuBar.getMenus();
    }
}
