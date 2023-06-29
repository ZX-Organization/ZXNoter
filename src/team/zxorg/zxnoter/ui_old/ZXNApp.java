package team.zxorg.zxnoter.ui_old;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import team.zxorg.zxnoter.Main;
import team.zxorg.zxnoter.audiomixer.AudioMixer;
import team.zxorg.zxnoter.map.mapInfo.OsuInfo;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.editor.BaseEditor;
import team.zxorg.zxnoter.ui_old.editor.MapEditor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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


    public static AudioFormat audioFormat = new AudioFormat(96000, 16, 2, true, false);//音频格式

    public static AudioMixer audioMixer;

    static {
        try {
            audioMixer = new AudioMixer(48000, 1024);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) {

        try {

        } catch (Exception e) {
            System.out.println("音频播放设备载入失败");
            throw new RuntimeException(e);
        }


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
        Menu menu = new Menu("文件");

        MenuItem testMenuItem = new MenuItem("测试", ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 16, Color.DARKGREEN));
        testMenuItem.setOnAction(event -> {
            {//添加编辑器
                Tab tab1 = new Tab();
                MapEditor editor = new MapEditor(Paths.get("docs/reference/Corruption/Corruption_4k_ez.imd"), tab1);


                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-osu-line", 18, Color.DARKGREEN));

                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);
                workspaceTabPane.getSelectionModel().select(tab1);
                rootPane.setOnKeyPressed(editor.getOnKeyPressed());


                //画布更新线程常驻
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        editor.render();
                    }
                };
                animationTimer.start();


            }
        });


        MenuItem openMenuItem = new MenuItem("打开谱面", ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 16, Color.DARKGREEN));
        openMenuItem.setOnAction(event -> {
            {//添加编辑器

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("导出谱面到");

                // 添加文件过滤器
                FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("全部谱面", "*.osu", "*.imd");
                FileChooser.ExtensionFilter osuFilter = new FileChooser.ExtensionFilter("osu谱面", "*.osu");
                FileChooser.ExtensionFilter imdFilter = new FileChooser.ExtensionFilter("节奏大师谱面", "*.imd");
                fileChooser.getExtensionFilters().addAll(allFilter, osuFilter, imdFilter);
                File file = fileChooser.showOpenDialog(stage.getOwner());
                if (file != null) {

                    Tab tab1 = new Tab();
                    MapEditor editor = new MapEditor(file.toPath(), tab1);


                    tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-osu-line", 18, Color.DARKGREEN));

                    tab1.setContent(editor);
                    workspaceTabPane.getTabs().add(tab1);
                    workspaceTabPane.getSelectionModel().select(tab1);
                    rootPane.setOnKeyPressed(editor.getOnKeyPressed());


                    //画布更新线程常驻
                    AnimationTimer animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            editor.render();
                        }
                    };
                    animationTimer.start();

                }
            }
        });

        MenuItem creatMenuItem = new MenuItem("新建谱面", ZXResources.getSvgPane("svg.icons.zxnoter.file-zxm-line", 16, Color.WHEAT));
        creatMenuItem.setOnAction(event -> {
            {//添加编辑器
                Tab tab1 = new Tab();
                MapEditor editor = new MapEditor(null, tab1);
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-osu-line", 18, Color.DARKGREEN));
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);
                workspaceTabPane.getSelectionModel().select(tab1);
                rootPane.setOnKeyPressed(editor.getOnKeyPressed());
            }
        });


        menu.getItems().addAll(testMenuItem, openMenuItem, creatMenuItem);
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
        //sideBar.setN(140);

        HBox.setHgrow(sideBar, Priority.ALWAYS);
        //sideBar.setVisible(false);
        //sideBar.setPrefWidth(0);

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


        //画布更新线程常驻
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Tab tab = workspaceTabPane.getSelectionModel().getSelectedItem();
                if (tab != null)
                    if (tab.getContent() instanceof BaseEditor editor)
                        editor.render();
            }
        };
        animationTimer.start();


        for (String file : Main.args) {
            Path path = Paths.get(file);
            if (Files.exists(path)) {//添加编辑器
                Tab tab1 = new Tab();
                MapEditor editor = new MapEditor(path, tab1);
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-osu-line", 18, Color.DARKGREEN));
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);
                rootPane.setOnKeyPressed(editor.getOnKeyPressed());
            }
        }


        rootPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                // 允许拖放
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        rootPane.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                // 获取拖放的文件列表
                for (File file : db.getFiles()) {
                    {//添加编辑器
                        Tab tab1 = new Tab();
                        MapEditor editor = new MapEditor(file.toPath(), tab1);
                        tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-osu-line", 18, Color.DARKGREEN));
                        tab1.setContent(editor);
                        workspaceTabPane.getTabs().add(tab1);
                        workspaceTabPane.getSelectionModel().select(tab1);
                        rootPane.setOnKeyPressed(editor.getOnKeyPressed());
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();


        });



        /*{//添加编辑器

            try {
                ZXMap zxMap = ImdReader.readFile(Paths.get("docs/reference/Contrapasso -paradiso-/t+pazolite - Contrapasso -paradiso-_4k_hd.imd"));
                Tab tab1 = new Tab(zxMap.unLocalizedMapInfo.getInfo("Title"));
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));
                MapEditor editor = new MapEditor(zxMap);
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);


                //画布更新线程常驻
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        editor.render();
                    }
                };
                animationTimer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }*/






        /*{//添加编辑器


            MapEditor editor = new MapEditor(Paths.get("docs/reference/LeaF - NANO DEATH!!!!!/LeaF - NANO DEATH!!!!! (nowsmart) [DEATH].osu"));

            Tab tab1 = new Tab(editor.zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title));
            tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));

            tab1.setContent(editor);
            workspaceTabPane.getTabs().add(tab1);

            //画布更新线程常驻
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    editor.render();
                }
            };
            animationTimer.start();


        }*/

/*
        {//添加编辑器

            try {
                ZXMap zxMap = OsuReader.readFile(Paths.get("docs/reference/896710 VA - 6k High Speed Ultimate Pack Vol3/V.A. - 6k High Speed Ultimate Pack Vol.3 (_IceRain) [Camellia - Blastix Riotz x1.1].osu"));
                Tab tab1 = new Tab(zxMap.unLocalizedMapInfo.getInfo("Title"));
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));
                MapEditor editor = new MapEditor(zxMap);
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);


                //画布更新线程常驻
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        editor.render();
                    }
                };
                animationTimer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        {//添加编辑器

            try {
                ZXMap zxMap = OsuReader.readFile(Paths.get("docs/reference/Risshuu feat. Choko - Take/Risshuu feat. Choko - Take (LNP-) [Beyond].osu"));
                Tab tab1 = new Tab(zxMap.unLocalizedMapInfo.getInfo("Title"));
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));
                MapEditor editor = new MapEditor(zxMap);
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);


                //画布更新线程常驻
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        editor.render();
                    }
                };
                animationTimer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        {//添加编辑器

            try {
                ZXMap zxMap = OsuReader.readFile(Paths.get("docs/reference/Dan reform jackmap mashup/1.osu"));
                Tab tab1 = new Tab(zxMap.unLocalizedMapInfo.getInfo("Title"));
                tab1.setGraphic(ZXResources.getSvgPane("svg.icons.zxnoter.file-notemap-line", 18, Color.DARKGREEN));
                MapEditor editor = new MapEditor(zxMap);
                tab1.setContent(editor);
                workspaceTabPane.getTabs().add(tab1);


                //画布更新线程常驻
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        editor.render();
                    }
                };
                animationTimer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }*/


        //正文容器 添加控件
        bodyPane.getChildren().addAll(activityBar, sideBar, workspace);


        Scene mainScene = new Scene(rootPane);


        System.out.println(ZXResources.getPath("css.root"));
        //应用样式
        mainScene.getStylesheets().add(ZXResources.getPath("css.root").toUri().toString());
        mainScene.getStylesheets().add(ZXResources.getPath("css.theme.dark").toUri().toString());

        stage.setScene(mainScene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

    }

    public static void run() {
        launch();
    }
}
