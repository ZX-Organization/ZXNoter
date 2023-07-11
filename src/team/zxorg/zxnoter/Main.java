package team.zxorg.zxnoter;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import team.zxorg.zxnoter.resource.ZXConfiguration;
import team.zxorg.zxnoter.ui.main.ZXStage;

public class Main {
    public static String[] commandLineArgs;


    public static void main(String[] args) {
        ZXLogger.info("ZXNoter启动");
        commandLineArgs = args;

        ZXLogger.info("初始化图形系统");



        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        PlatformImpl.startup(() -> {
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            ZXConfiguration.reload();

            //创建软件实例
            ZXStage zxStage = new ZXStage();
            ZXLogger.info("显示ZXN-UI窗口");
            zxStage.show();
        });


    }

}
