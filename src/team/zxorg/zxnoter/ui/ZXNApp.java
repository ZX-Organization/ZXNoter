package team.zxorg.zxnoter.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.one.MainVBox;

public class ZXNApp extends Application {

    @Override
    public void start(Stage fw) throws Exception {
        //初始化 (载入配置 使用资源)
        ZXConfiguration.reload();
        {
            ZXStage zxStage = new ZXStage();
            zxStage.show();
        }
    }

}
