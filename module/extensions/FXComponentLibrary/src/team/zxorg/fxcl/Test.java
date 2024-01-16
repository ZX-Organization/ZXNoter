package team.zxorg.fxcl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import team.zxorg.fxcl.javafx.ProjectView;
import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;

public class Test {
    public static ZXVersion VERSION=new ZXVersion(0,0,0, ZXVersion.ReleaseStatus.RC);

    public static void main(String[] args) {


        ZXLogger.info("===== > ZXNoter User Interface Frame < =====");
        ZXLogger.info("Version: " +VERSION + " Code: " +VERSION.getCode());
        VERSION.printInfo();

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

                /*try (InputStream inputStream = Files.newInputStream(Path.of("ZXNoterUIFrame/res/packs/baseExpansionPack/expansion.json"))) {
                    //ZXLogger.info("载入全局配置");
                    JSONObject root = JSON.parseObject(inputStream);
                    ExpansionPackInfo info = root.toJavaObject(ExpansionPackInfo.class);
                } catch (IOException e) {
                    //ZXLogger.severe("载入全局配置失败");
                    throw new RuntimeException(e);
                }*/
/*

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

                */
/*Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(pane.backgroundProperty(), Background.fill(Color.GOLD))),
                        new KeyFrame(Duration.seconds(1), new KeyValue(pane.backgroundProperty(), Background.fill(Color.RED))),
                        new KeyFrame(Duration.seconds(2), new KeyValue(pane.backgroundProperty(), Background.fill(Color.RED)))
                );

                // 设置动画循环次数
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.setAutoReverse(true);
                timeline.play();
*//*



                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();
                System.out.println("> "+ pane.getStyleClass());
*/
                ProjectView projectView = new ProjectView();
                projectView.show();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ZXLogger.info("关闭程序");

            }));

        });


    }


}
