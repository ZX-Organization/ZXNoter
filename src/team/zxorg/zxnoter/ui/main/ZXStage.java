package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.config.ZXConfig;
import team.zxorg.zxnoter.config.ZXProject;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.stage.area.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.side.SideBar;
import team.zxorg.zxnoter.ui.main.stage.menu.TitleBar;
import team.zxorg.zxnoter.ui.main.stage.status.StatusBar;

public class ZXStage extends Stage {


    /**
     * 项目类
     */
    public ZXProject zxProject = new ZXProject(this);


    /**
     * 顶部 标题栏
     */
    public TitleBar titleBar = new TitleBar(this);

    /**
     * 中间 编辑区域
     */
    public EditorArea editorArea = new EditorArea(this);
    /**
     * 左边 侧边栏
     */
    public SideBar sideBar = new SideBar(this);
    /**
     * 底部 状态栏
     */
    public StatusBar statusBar = new StatusBar(this);


    private HBox bodyHBox = new HBox();


    public ZXStage() {
        ZXLogger.info("实例化ZXStage");

        /*System.out.println(List.of(projectPath.getClass().getDeclaredFields()));
        projectPath.getClass().getDeclaredField("")*/
        //测试代码
        //initStyle(StageStyle.TRANSPARENT);


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
        //zxProject.projectPath.set(null);
        //zxProject.openLastProject();
        //project.projectPath.set(Path.of("./docs/reference"));


        setOnCloseRequest(event -> {
            ZXConfig.saveConfig();
            zxProject.closeProject();
        });


    }


}
