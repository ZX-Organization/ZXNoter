package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.zxnoter.resource.ZXResources;

import java.nio.file.Path;

public class ZXNApp extends Application {
    @Override
    public void start(Stage stage) {

        /**
         * 载入资源
         */
        ZXResources.loadResourcePackage(Path.of("./resourcespackage/"));

        Pane icon = ZXResources.getSvgPane("a");
        icon.setPrefSize(22, 22);
        icon.setBackground(Background.fill(Color.YELLOW));
        HBox.setMargin(icon, new Insets(4));

        Pane icon2 = ZXResources.getSvgPane("svg.icons.zxnoter.zxnoter");
        icon2.setPrefSize(22, 22);
        icon2.setBackground(Background.fill(Color.YELLOW));
        HBox.setMargin(icon2, new Insets(4));



        HBox titlePane = new HBox(icon, icon2);
        titlePane.setBackground(Background.fill(Color.RED));
        titlePane.setPrefSize(Region.USE_COMPUTED_SIZE, 30);


        Image image=ZXResources.getImage("sdas");
        Image image2=ZXResources.getImage("img.zxnoter.zxnoter");
        HBox bodyPane = new HBox(new ImageView(image),new ImageView(image2));
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
