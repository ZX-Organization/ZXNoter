package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.editor.MapEditor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * 活动栏
     */
    VBox activityBar = new VBox();

    /**
     * 侧边栏
     */
    VBox sideBar = new VBox();


    /**
     * 工作空间 分页
     */
    TabPane workspaceTabPane = new TabPane();


    /**
     * 工作空间
     */
    StackPane workspace = new StackPane();


    /**
     * 根容器
     */
    VBox rootPane = new VBox(titleBar, bodyPane);

    @Override
    public void start(Stage stage) {


        //载入资源
        ZXResources.loadResourcePackage(Path.of("./resourcespackage/"));


        //构造zxn主窗口

        //标题栏
        titleBar.setBackground(Background.fill(Color.RED));
        titleBar.setPrefSize(Region.USE_COMPUTED_SIZE, 30);
        titleBar.setMinHeight(Region.USE_PREF_SIZE);
        titleBar.getStyleClass().add("title-bar");
        ImageView zxnIcon = new ImageView(ZXResources.getImage("img.zxnoter.zxnoter-x26"));
        HBox.setMargin(zxnIcon, new Insets(2, 4, 2, 4));

        //菜单栏
        Menu menu = new Menu("az");
        Menu menuItem = new Menu("wdnmd2", ZXResources.getSvgPane("svg.zxnoter.file-notemap-line", 16, Color.AQUA));
        menuItem.getItems().add(new MenuItem("@#$%^"));
        menu.getItems().addAll(new MenuItem("wdnmd"), menuItem);
        Menu menu2 = new Menu("666");
        menuBar.getMenus().addAll(menu, menu2);
        menuBar.setPadding(new Insets(0));

        //标题栏 添加控件
        titleBar.getChildren().addAll(zxnIcon, menuBar);

        //正文容器
        //bodyPane.setBackground(Background.fill(Color.GREEN));
        bodyPane.getStyleClass().add("body");
        bodyPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);

        //活动栏
        activityBar.getStyleClass().add("activity-bar");
        //activityBar.setBackground(Background.fill(Color.BLACK));
        activityBar.setPrefWidth(50);
        activityBar.setMinWidth(Region.USE_PREF_SIZE);

        //侧边栏
        sideBar.getStyleClass().add("side-bar");
        //sideBar.setBackground(Background.fill(Color.YELLOW));
        sideBar.setPrefWidth(180);
        sideBar.setMinWidth(Region.USE_PREF_SIZE);

        //工作空间
        workspace.getStyleClass().add("workspace");

        Region region = ZXResources.getSvgPane("svg.icons.zxnoter.zxnoter");
        region.getStyleClass().add("icon");
        region.setMaxSize(400, 400);


        workspace.getChildren().addAll(region, workspaceTabPane);

        //workspacePane.setBackground(Background.fill(Color.GREEN));
        HBox.setHgrow(workspace, Priority.ALWAYS);
        //workspace.setShape(ZXResources.getSvg("svg.icons.zxnoter.zxnoter"));


        VBox.setVgrow(workspaceTabPane, Priority.ALWAYS);


        {//添加编辑器

            try {
                ZXMap zxMap = ImdReader.readFile(Paths.get("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
                Tab tab1 = new Tab(zxMap.unLocalizedMapInfo.getInfo("Title"));
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));
                HBox editor = new MapEditor(zxMap);
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


        //正文容器 添加控件
        bodyPane.getChildren().addAll(activityBar, sideBar, workspace);


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
