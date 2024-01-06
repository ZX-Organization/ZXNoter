package team.zxorg.skin.uis.ui;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import team.zxorg.skin.DeviceType;
import team.zxorg.skin.ResolutionInfo;
import team.zxorg.skin.uis.UISCanvas;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class UISEditor extends HBox {

    public static final ZXVersion VERSION = new ZXVersion(1, 0, 3, ZXVersion.ReleaseStatus.BETA);

    UISCanvas uisCanvas = new UISCanvas() {
        {
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0))));
        }
    };
    TabPane tabPane = new TabPane() {
        {
            //getSelectionModel().selectedItemProperty().addListener(observable -> reload());
            VBox.setVgrow(this, Priority.ALWAYS);
            setPrefWidth(640);
            setMinWidth(640);
            setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        }
    };

    /**
     * 设备类型选择框
     */
    ChoiceBox<DeviceType> deviceTypeChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(DeviceType.values());
            setPrefWidth(60);
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                uisCanvas.setDeviceType(newValue);
            });
            //getSelectionModel().selectLast();
        }
    };
    /**
     * 屏幕比例选择框
     */
    ChoiceBox<ResolutionInfo> resolutionChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(ResolutionInfo.values());
            setPrefWidth(100);
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deviceTypeChoiceBox.setValue(newValue.getDevice());
                uisCanvas.setAspectRatio(newValue.getAspectRatio());
            });
            getSelectionModel().selectLast();
        }
    };
    /**
     * 单位选择框
     */
    ChoiceBox<UnitInfo> unitChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(
                    new UnitInfo("像素", "px", 1),
                    new UnitInfo("游戏像素", "gpx", 0),
                    new UnitInfo("百分比", "%", 2)
            );
            valueProperty().addListener((observable, oldValue, newValue) -> {
                uisCanvas.measureRuler.unit = newValue;
            });
            getSelectionModel().selectFirst();
            setPrefWidth(80);
        }
    };
    Label scalingFactorLabel = new Label("缩放: 100%") {
        {
            setPrefWidth(70);
        }
    };
    /**
     * 缩放因子滑块
     */
    Slider scalingFactorSlider = new Slider() {
        {
            setMin(0.2);
            setMax(4.0);
            setBlockIncrement(0.2);
            setMajorTickUnit(0.4);
            //setShowTickMarks(true);
            setShowTickLabels(true);
            setMinorTickCount(1);
            setValue(1);
            setPrefWidth(100);
            setSnapToTicks(true);
            setSnapToPixel(true);
            valueProperty().addListener((observable, oldValue, newValue) -> {
                if (!isValueChanging()) {
                    scalingFactorLabel.setText("缩放: " + (int) (newValue.doubleValue() * 100) + "%");
                    uisCanvas.setZoomRate(newValue.doubleValue());
                }
            });
        }
    };


    private File lastDirectory = new File(System.getProperty("user.dir")); // 记录上一次选择的目录

    public UISEditor() {
        setAlignment(Pos.CENTER_LEFT);


        uisCanvas.minWidthProperty().addListener((observable, oldValue, newValue) -> {
            this.setMinWidth(newValue.doubleValue() + tabPane.getMinWidth());
        });
        uisCanvas.minHeightProperty().addListener((observable, oldValue, newValue) -> {
            this.setMinHeight(newValue.doubleValue() + 40);
        });


        /*pane.widthProperty().addListener(observable -> {
            canvas.setWidth(pane.getWidth());
        });
        pane.heightProperty().addListener(observable ->{
            canvas.setHeight(pane.getHeight());
        });*/
        setBackground(Background.fill(Color.BLACK));





        /*canvas.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                measuringRulerRenderer.setPos = 1;
            } else {
                measuringRulerRenderer.setPos = 2;
            }
        });
        canvas.setOnMouseDragged(event -> {
            if (event.isShiftDown()) {
                measuringRulerRenderer.state = 1;
            } else if (event.isAltDown()) {
                measuringRulerRenderer.state = 2;
            } else {
                measuringRulerRenderer.state = 0;
            }
            switch (measuringRulerRenderer.setPos) {
                case 1 -> measuringRulerRenderer.setPos1(event.getX(), event.getY());
                case 2 -> measuringRulerRenderer.setPos2(event.getX(), event.getY());
            }
        });
        canvas.setOnMouseReleased(event -> measuringRulerRenderer.setPos = 0);*/
        //画布更新线程常驻
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                uisCanvas.draw();
            }
        };


        animationTimer.start();
        HBox.setHgrow(uisCanvas, Priority.ALWAYS);
        getChildren().addAll(sideVBox, uisCanvas);
    }

    public static void main(String[] args) {
        ZXLogger.info("===== > ZXNoter Skin Editor < =====");
        ZXLogger.info("Version: " + VERSION + " Code: " + VERSION.getVersionCode());
        switch (VERSION.status()) {
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
            UISEditor uISEditor = new UISEditor();
            Scene scene = new Scene(uISEditor);
            scene.getStylesheets().addAll("resources/baseExpansionPack/color/style.css");
            scene.getStylesheets().addAll("resources/baseExpansionPack/color/dark.css");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            uISEditor.minWidthProperty().addListener((observable, oldValue, newValue) -> {
                stage.setMinWidth(newValue.doubleValue());
            });
            uISEditor.minHeightProperty().addListener((observable, oldValue, newValue) -> {
                stage.setMinHeight(newValue.doubleValue());
            });
        });
    }


    public void openFIle(Path path) {
        Tab tab = new Tab();
        tab.setText(path.getFileName().toString());
        UISCodeArea uisCodeArea = null;
        try {
            uisCodeArea = new UISCodeArea(path, uisCanvas::updateSkin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        uisCodeArea.setAutoHeight(true);
        uisCodeArea.setAutoScrollOnDragDesired(true);
        VirtualizedScrollPane<UISCodeArea> vsPane = new VirtualizedScrollPane<>(uisCodeArea);
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        tab.setContent(vsPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        uisCanvas.loadSkin(path);
    }

    public record UnitInfo(String name, String unit, int id) {
        @Override
        public String toString() {
            return name + "(" + unit + ")";
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String unit() {
            return unit;
        }

        @Override
        public int id() {
            return id;
        }
    }


    /*Button reloadButton = new Button("重载") {
        {
            setOnAction(event -> uisCanvas.updateSkin());
        }
    };*/


    Button openFileButton = new Button("打开mui文件") {
        {
            setOnAction(event -> {

                // 创建文件选择器
                FileChooser fileChooser = new FileChooser();
                // 设置初始目录为上一次选择的目录
                if (lastDirectory != null && lastDirectory.isDirectory()) {
                    fileChooser.setInitialDirectory(lastDirectory);
                }
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MUI 文件", "*.mui"));

                // 显示文件选择对话框
                File selectedFile = fileChooser.showOpenDialog(null);

                if (selectedFile != null) {
                    System.out.println("Selected File: " + selectedFile.getAbsolutePath());

                    openFIle(selectedFile.toPath());
                    // 记录本次选择的目录
                    lastDirectory = selectedFile.getParentFile();
                } else {
                    System.out.println("No file selected.");
                }

            });
        }
    };

    /**
     * 顶部工具栏
     */
    HBox topToolbar = new HBox(resolutionChoiceBox, deviceTypeChoiceBox, unitChoiceBox, scalingFactorLabel, scalingFactorSlider, openFileButton) {
        {
            setMinHeight(40);
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0), new Insets(0))));
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(8);
            setPadding(new Insets(0, 8, 0, 8));
        }
    };


    CheckBox autoReplayCheckBox = new CheckBox("自动重播") {
        {
            setSelected(true);
        }
    };
    Button replayButton = new Button("重放") {
        {
            setOnAction(event -> uisCanvas.resetTime());
        }
    };

    Button playButton = new Button("播放") {
        {
            setOnAction(event -> {
                /*if (isPaused) {
                    isPaused = false;
                    startTime = System.currentTimeMillis() - pauseTime;
                }*/
            });
        }
    };
    Button pauseButton = new Button("暂停") {
        {
            setOnAction(event -> {
               /* if (!isPaused) {
                    isPaused = true;
                    pauseTime = System.currentTimeMillis() - startTime;
                }*/
            });
        }
    };

    Label timeLabel = new Label() {
        {
            setPrefWidth(80);
            setAlignment(Pos.CENTER_RIGHT);
        }
    };
    Slider timeLineSlider = new Slider(-3.5, 30, -3) {
        {
            HBox.setHgrow(this, Priority.ALWAYS);
            setPrefWidth(240);

            //设置块增量
            setBlockIncrement(0.01);
            //设置主要刻度单位
            setMajorTickUnit(0.5);
            setShowTickLabels(true);
            setMinorTickCount(1);
            setSnapToTicks(true);
            setSnapToPixel(true);
            valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isValueChanging()) {
                    /*if (isPaused) {
                        pauseTime = (long) (newValue.doubleValue() * 1000);
                    } else {
                        startTime = System.currentTimeMillis() - (long) (newValue.doubleValue() * 1000);
                    }*/
                }
            });
        }
    };

    HBox bottomToolbar = new HBox(autoReplayCheckBox, timeLabel, timeLineSlider, playButton, pauseButton, replayButton) {
        {
            setMinHeight(40);
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 0, 0, 0), new Insets(0))));
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(8);
            setPadding(new Insets(0, 8, 0, 8));
        }
    };

    VBox sideVBox = new VBox(topToolbar, tabPane, bottomToolbar) {
        {
            setHgrow(this, Priority.ALWAYS);
            setPrefWidth(260);
            setBackground(Background.fill(Color.BLACK));
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 0), new Insets(0))));
        }
    };


}
