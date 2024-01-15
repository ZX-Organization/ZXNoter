package team.zxorg.ui.main.stage.menu;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import team.zxorg.ui.main.ZXStage;
import team.zxorg.zxnoter.resource.ZXResources;

public class TitleBar extends HBox {
    //菜单栏
    public MenuBar menuBar = new MenuBar();

    public TitleBar(ZXStage zxStage) {
        //标题栏
        setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.LOGO_X26);//ZXResources.getImage("img.zxnoter.zxnoter-x26")
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        //菜单栏
        menuBar.setPadding(new Insets(0));
        getChildren().addAll(zxnIcon, menuBar);


        menuBar.getMenus().addAll(new TitleBarFileMenu(zxStage));


    }

    public ObservableList<Menu> getMenus() {
        return menuBar.getMenus();
    }
}