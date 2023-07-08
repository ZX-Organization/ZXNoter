package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.one.MainVBox;

public class ZXNApp extends Application {
    MainVBox mainVBox;

    @Override
    public void start(Stage stage) throws Exception {
        //初始化 (载入配置 使用资源)
        ZXConfiguration.reload();

        mainVBox = new MainVBox();//构建UI
        Scene scene = new Scene(mainVBox);

        ZXResources.setSceneStyle(scene);//设置样式

        //初始化主窗口
        stage.setScene(scene);
        stage.setMinWidth(1070);
        stage.setMinHeight(600);
        stage.setWidth(1070);
        stage.setHeight(600);
        stage.show();
    }

}
