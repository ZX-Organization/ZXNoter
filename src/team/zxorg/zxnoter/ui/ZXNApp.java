package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.ui.main.MainWindow;

import java.nio.file.Path;

public class ZXNApp extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        //初始化资源
        ZXResources.loadResourcePackage(Path.of("./resourcespackage/"));

        //初始化主窗口


        stage.setScene(new MainWindow().init("dark"));
        stage.setMinWidth(1070);
        stage.setMinHeight(600);
        stage.setWidth(1070);
        stage.setHeight(600);
        stage.show();
    }

    public static void run() {
        launch();
    }

}
