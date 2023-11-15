package team.zxorg.ui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.util.Logging;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;
import team.zxorg.zxncore.info.InfoManager;
import team.zxorg.zxncore.info.root.ExpansionPackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {


    public static void main(String[] args) {


        ZXLogger.info("===== > ZXNoter User Interface Frame < =====");
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

        ZXLogger.info("ZXNoter启动");


        //屏蔽javafx歌姬初始化时的异常
        Logging.getJavaFXLogger().disableLogging();
        ZXLogger.info("初始化图形系统");


        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
            //初始化 (载入配置 使用资源)
            ZXLogger.info("初始化配置");
            //ConfigManager.reload();

            //读取上一次状态
            //LastTimeStatesInfo lastTimeStates = ConfigManager.configuration.lastTimeStates;

            /*if (lastTimeStates.openedProjects.isEmpty()) {

            } else {

            }*/

            {

                try (InputStream inputStream = Files.newInputStream(Path.of("ZXNoterUIFrame/res/resources/baseExpansionPack/expansion.json"))) {
                    //ZXLogger.info("载入全局配置");
                    JSONObject root = JSON.parseObject(inputStream);
                    ExpansionPackInfo info = root.toJavaObject(ExpansionPackInfo.class);
                } catch (IOException e) {
                    //ZXLogger.severe("载入全局配置失败");
                    throw new RuntimeException(e);
                }

                Stage stage = new Stage();
                TextField textField = new TextField();

                Shape shape = new Rectangle();

                FillTransition fillTransition = new FillTransition();
                fillTransition.setFromValue(Color.RED);
                fillTransition.setToValue(Color.GOLD);
                fillTransition.setDuration(Duration.seconds(1));
                fillTransition.setCycleCount(Transition.INDEFINITE);
                fillTransition.setInterpolator(Interpolator.EASE_OUT);
                fillTransition.setShape(shape);
                System.out.println(textField.getChildrenUnmodifiable());

                // 绑定TextField的背景颜色和形状的颜色
                textField.backgroundProperty().bind(Bindings.createObjectBinding(
                        () -> new Background(new BackgroundFill(shape.getFill(), null, null)),
                        shape.fillProperty()
                ));



                Pane pane = new Pane(textField);

                TranslateTransition translateTransition=new TranslateTransition();
                translateTransition.setNode(shape);

                // 创建并行动画
                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().addAll(fillTransition,translateTransition);

                // 播放动画
                parallelTransition.play();

                /*Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(pane.backgroundProperty(), Background.fill(Color.GOLD))),
                        new KeyFrame(Duration.seconds(1), new KeyValue(pane.backgroundProperty(), Background.fill(Color.RED))),
                        new KeyFrame(Duration.seconds(2), new KeyValue(pane.backgroundProperty(), Background.fill(Color.RED)))
                );

                // 设置动画循环次数
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.setAutoReverse(true);
                timeline.play();
*/


                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();
                System.out.println("> "+ pane.getStyleClass());


            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ZXLogger.info("关闭程序");

            }));

        });


    }


}
