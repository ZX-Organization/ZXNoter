package team.zxorg.zxnoter.ui.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.*;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

import java.nio.file.Path;
import java.util.Locale;

public class ZXStage extends Stage {
    public ZXProject project = new ZXProject(this);
    public TitleBar titleBar = new TitleBar(this);
    public HBox bodyHBox = new HBox();
    public StatusBar statusBar = new StatusBar(this);//状态栏

    public EditorArea editorArea= new EditorArea();//编辑区域
    public SideBar sideBar=new SideBar(project);//侧边栏

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
        project.projectPath.set(null);
        project.openLastProject();
        //project.projectPath.set(Path.of("./docs/reference"));

        setOnCloseRequest(event -> ZXConfiguration.saveConfig());
    }


}
