package team.zxorg.zxnoter.ui.main.component;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.resource_old.ZXResources;

public class TitleBar extends HBox {
    //菜单栏
    public MenuBar menuBar = new MenuBar();

    public TitleBar() {

        //标题栏
        setBackground(Background.fill(Color.RED));
        setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.getImage("img.zxnoter.zxnoter-x26"));
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        //菜单栏
        menuBar.setPadding(new Insets(0));
        getChildren().addAll(zxnIcon, menuBar);

    }

    public ObservableList<Menu> getMenus() {
        return menuBar.getMenus();
    }
}
