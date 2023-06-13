package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.zxnoter.resource.ZXResources;

import java.nio.file.Path;

public class ZXNApp extends Application {
    /**
     * 标题栏
     */
    HBox titleBar = new HBox();
    /**
     * 正文容器
     */
    HBox bodyPane = new HBox();

    /**
     * 菜单栏
     */
    MenuBar menuBar = new MenuBar();

    /**
     * 根容器
     */
    VBox rootPane = new VBox(titleBar, bodyPane);

    @Override
    public void start(Stage stage) {


        //载入资源
        ZXResources.loadResourcePackage(Path.of("./resourcespackage/"));





        //构造zxn主窗口


        titleBar.setBackground(Background.fill(Color.RED));
        titleBar.setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        titleBar.setMinHeight(Region.USE_PREF_SIZE);
        titleBar.getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.getImage("img.zxnoter.zxnoter-x26"));
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));


        Menu menu = new Menu("az");
        Menu menuItem=new Menu("wdnmd2",ZXResources.getSvgPane("svg.zxnoter.file-notemap-line",16,Color.AQUA));
        menuItem.getItems().add(new MenuItem("@#$%^"));
        menu.getItems().addAll(new MenuItem("wdnmd"),menuItem);
        Menu menu2 = new Menu("666");
        menuBar.getMenus().addAll(menu,menu2);
        menuBar.setPadding(new Insets(0));

        titleBar.getChildren().addAll(zxnIcon, menuBar);


        bodyPane.setBackground(Background.fill(Color.GREEN));
        bodyPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);


        //VBox activityBar
        //bodyPane.getChildren().add();




        Scene mainScene = new Scene(rootPane);

        //应用样式
        mainScene.getStylesheets().add(ZXResources.getPath("css.root").toUri().toString());
        mainScene.getStylesheets().add(ZXResources.getPath("css.theme.dark").toUri().toString());

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
