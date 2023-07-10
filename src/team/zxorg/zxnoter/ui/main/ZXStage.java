package team.zxorg.zxnoter.ui.main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.one.MainVBox;

public class ZXStage extends Stage {
    public ZXStage() {
        ZXLogger.info("实例化ZXN-UI窗口");
        MainVBox mainVBox = new MainVBox();//构建UI
        Scene scene = new Scene(mainVBox);
        ZXResources.setSceneStyle(scene);//设置样式
        //初始化主窗口
        setScene(scene);
        setMinWidth(1000);
        setMinHeight(618);
        setWidth(1000);
        setHeight(618);
    }
}
