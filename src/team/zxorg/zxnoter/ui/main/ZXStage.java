package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.stage.*;

import java.util.Locale;

public class ZXStage extends Stage {
    public TitleBar titleBar = new TitleBar();
    public BodyHBox bodyHBox = new BodyHBox();
    public StatusBar statusBar = new StatusBar();//状态栏

    public ZXStage() {
        ZXLogger.info("实例化ZXN-UI窗口");
        VBox vBox = new VBox(titleBar, bodyHBox, statusBar);
        vBox.getStyleClass().add("main");
        Scene scene = new Scene(vBox);
        GlobalResources.bindSceneStyle(scene);//设置样式
        //初始化主窗口
        setScene(scene);
        setMinWidth(1000);
        setMinHeight(618);
        setWidth(1000);
        setHeight(618);
        setTitle("ZXNoter");
        getIcons().add(ZXResources.LOGO);
    }
}
