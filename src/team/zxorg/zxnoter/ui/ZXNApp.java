package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class ZXNApp extends Application {
    @Override
    public void start(Stage stage) {


        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void run() {
        launch();
    }
}
