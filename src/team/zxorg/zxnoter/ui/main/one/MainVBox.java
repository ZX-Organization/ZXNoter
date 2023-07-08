package team.zxorg.zxnoter.ui.main.one;

import javafx.scene.layout.VBox;
import javafx.stage.Window;
import team.zxorg.zxnoter.ui.main.one.two.StatusBar;
import team.zxorg.zxnoter.ui.main.one.two.TitleBar;
import team.zxorg.zxnoter.ui.main.one.two.BodyHBox;

public class MainVBox extends VBox {
    public TitleBar titleBar = new TitleBar();
    public BodyHBox bodyHBox = new BodyHBox();
    public StatusBar statusBar = new StatusBar();//状态栏

    public MainVBox() {
        getStyleClass().add("main");
        getChildren().addAll(titleBar, bodyHBox, statusBar);
    }
}
