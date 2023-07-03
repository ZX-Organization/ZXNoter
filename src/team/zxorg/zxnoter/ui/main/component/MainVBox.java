package team.zxorg.zxnoter.ui.main.component;

import javafx.scene.layout.VBox;

public class MainVBox extends VBox {
    public TitleBar titleBar = new TitleBar();
    public BodyHBox bodyHBox = new BodyHBox();
    public StatusBar statusBar = new StatusBar();//状态栏

    public MainVBox() {
        getStyleClass().add("main");
        getChildren().addAll(titleBar, bodyHBox, statusBar);
    }
}
