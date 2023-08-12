package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.*;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

public class ZXStage extends Stage {
    public ZXProject zxProject = new ZXProject(this);
    public TitleBar titleBar = new TitleBar(this);
    public HBox bodyHBox = new HBox();


    public EditorArea editorArea = new EditorArea(zxProject);//编辑区域
    public SideBar sideBar = new SideBar(zxProject);//侧边栏

    public StatusBar statusBar = new StatusBar(this);//状态栏

    public ZXStage() {
        ZXLogger.info("实例化ZXN-UI窗口");

        bodyHBox.getStyleClass().add("body");
        bodyHBox.getChildren().addAll(sideBar, editorArea);
        VBox.setVgrow(bodyHBox, Priority.ALWAYS);
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

        //初始化项目
        //project.projectPath.set(Path.of("./docs/reference"));
        zxProject.projectPath.set(null);
        zxProject.openLastProject();
        //project.projectPath.set(Path.of("./docs/reference"));

        setOnCloseRequest(event -> {
            ZXConfiguration.saveConfig();
            zxProject.closeProject();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ZXLogger.info("关闭程序");
            close();
            // 在这里执行您需要在关机前执行的操作
        }));
    }


}
