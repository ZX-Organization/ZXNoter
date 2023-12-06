package team.zxorg.skin;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;

import java.util.ArrayList;

public class SkinEditor extends HBox {

    ArrayList<ElementRender> elements = new ArrayList<>();

    public SkinEditor() {
        Canvas canvas = new Canvas();
        canvas.heightProperty().bind(heightProperty());
        canvas.widthProperty().bind(widthProperty());
        setBackground(Background.fill(Color.BLACK));
        GraphicsContext context = canvas.getGraphicsContext2D();

        //画布更新线程常驻
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for (ElementRender e : elements) {
                    e.render(context);
                }
            }
        };
        animationTimer.start();


    }

    public static void main(String[] args) {
        ZXLogger.info("===== > ZXNoter Skin Editor < =====");
        ZXLogger.info("Version: " + ZXVersion.VERSION + " Code: " + ZXVersion.VERSION.getVersionCode());
        switch (ZXVersion.VERSION.status()) {
            case RC -> {
                ZXLogger.warning("当前为 [内部测试版本] 请不要泄漏软件到外部");
            }
            case BETA -> {
                ZXLogger.warning("当前为 [提前预览版本] 如有问题请联系开发者");
            }
            case ALPHA -> {
                ZXLogger.warning("当前为 [早期开发版本] 请谨慎测试软件功能");
            }
            case STABLE -> {
                ZXLogger.info("当前为 [稳定发布版本] 请尽情使用");
            }
        }

        ZXLogger.info("ZXNoter Skin Editor启动");


        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");


        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            SkinEditor skinEditor = new SkinEditor();
            Scene scene = new Scene(skinEditor);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.show();
        });
    }
}
