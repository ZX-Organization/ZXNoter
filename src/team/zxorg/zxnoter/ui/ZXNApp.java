package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZXNApp extends Application {
    @Override
    public void start(Stage stage) {


        VBox titlePane = new VBox();
        titlePane.setBackground(Background.fill(Color.RED));
        titlePane.setPrefSize(Region.USE_COMPUTED_SIZE, 30);

        HBox bodyPane = new HBox();
        bodyPane.setBackground(Background.fill(Color.GREEN));
        bodyPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);

        VBox rootPane = new VBox(titlePane, bodyPane);
        rootPane.setBackground(Background.fill(Color.YELLOW));

        Scene mainScene = new Scene(rootPane);


        stage.setScene(mainScene);
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
