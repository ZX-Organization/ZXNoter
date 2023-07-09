package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.one.MainVBox;

public class ZXNApp extends Application {

    @Override
    public void start(Stage fw) throws Exception {
        //初始化 (载入配置 使用资源)
        ZXLogger.logger.info("初始化配置");
        ZXConfiguration.reload();
        {
            ZXStage zxStage = new ZXStage();
            ZXLogger.logger.info("显示ZXN-UI窗口");
            zxStage.show();
        }
    }

}
