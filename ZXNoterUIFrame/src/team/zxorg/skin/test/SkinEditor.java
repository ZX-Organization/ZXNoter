package team.zxorg.skin.test;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;

import java.net.MalformedURLException;
import java.nio.file.Path;

public class SkinEditor extends HBox {


    public SkinEditor() {


        Pane pane = new Pane();

        SubScene scene = new SubScene(pane, 1280, 720);
        scene.setFill(Color.GREEN);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            pane.setPrefWidth(newValue.doubleValue());
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            pane.setPrefHeight(newValue.doubleValue());
        });
        pane.setPrefWidth(scene.getWidth());
        pane.setPrefHeight(scene.getHeight());

        pane.setBackground(Background.fill(Color.GRAY));

        pane.setTranslateX(-pane.getPrefWidth() / 2);
        pane.setTranslateY(-pane.getPrefHeight() / 2);
        pane.setTranslateZ(0);


        {
            ImageView imageView = null;
            try {
                imageView = new ImageView(Path.of("D:/malody/skin/Xiang Vma-1.2 - 副本/hit/r.png").toUri().toURL().toString());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setX(pane.getPrefWidth() / 2 - 50);
            imageView.setY(pane.getPrefHeight() / 2 - 50);

            pane.getChildren().addAll(imageView);
        }


        // 创建透视相机
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        //camera.setFieldOfView(60);


        double fieldOfView = camera.getFieldOfView();
        double width = pane.getPrefHeight();


        // 计算相机位置，确保整个 Pane 的宽度和视野中看到的宽度相同
        double distance = -width / (2 * Math.tan(Math.toRadians(fieldOfView / 2)));
        camera.setTranslateZ(distance);
        camera.setTranslateX(0);
        camera.setTranslateY(0);


        // 将相机设置到场景中
        scene.setCamera(camera);

        StackPane stackPane = new StackPane(scene);
        stackPane.setBackground(Background.fill(Color.RED));


        VBox toolbar = new VBox();
        toolbar.setPrefWidth(100);
        toolbar.setBackground(Background.fill(Color.BLACK));


        getChildren().addAll(stackPane, toolbar);

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
