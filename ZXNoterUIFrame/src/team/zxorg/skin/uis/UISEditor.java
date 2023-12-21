package team.zxorg.skin.uis;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.LayerCanvasPane;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.components.*;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;

public class UISEditor extends HBox {
    HashMap<String, ElementRender> elements = new HashMap<>();
    LayerCanvasPane layerCanvasPane = new LayerCanvasPane() {
        {
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0))));
        }
    };
    TabPane tabPane = new TabPane() {
        {
            VBox.setVgrow(this, Priority.ALWAYS);
            setPrefWidth(600);
            setMinWidth(600);
        }
    };
    Button resetButton = new Button("解析当前") {
        {
            setOnAction(event -> {
                if (tabPane.getSelectionModel().getSelectedItem() != null)
                    if (tabPane.getSelectionModel().getSelectedItem().getContent() instanceof UISCodeArea uisCodeArea) {
                        elements.clear();
                        elements.putAll(UISParser.parseToElementMap(uisCodeArea.getFile()));
                    }
            });
        }
    };
    Runnable reload = new Runnable() {

        @Override
        public void run() {
            resetButton.getOnAction().handle(new ActionEvent());
        }
    };
    ChoiceBox<ResolutionInfo> resolutionChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(
                    new ResolutionInfo(1920, 1080, "电脑 1080p 16:9"),
                    new ResolutionInfo(1600, 900, "电脑 900p 16:9"),
                    new ResolutionInfo(1440, 900, "平板 900p 16:10"),
                    new ResolutionInfo(1280, 800, "平板 800p 16:10"),
                    new ResolutionInfo(1280, 720, "笔记本 720p 16:9"),
                    new ResolutionInfo(1024, 768, "平板 768p 4:3"),
                    new ResolutionInfo(1024, 600, "手机 600p 17:10"),
                    new ResolutionInfo(800, 600, "手机 600p 4:3"),
                    new ResolutionInfo(512, 384, "手机 384p 4:3")
            );

        }
    };
    private File lastDirectory = new File("D:\\malody\\skin\\ttb5测试-再修改"); // 记录上一次选择的目录
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
    HBox toolbar = new HBox(resolutionChoiceBox, openFileButton) {
        {
            setMinHeight(40);
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 0, 0, 0), new Insets(0))));
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(16);
            setPadding(new Insets(0, 8, 0, 8));
        }
    };
    VBox rightVBox = new VBox(tabPane, toolbar) {
        {
            setHgrow(this, Priority.ALWAYS);
            setPrefWidth(260);
            setBackground(Background.fill(Color.BLACK));
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 1), new Insets(0))));
        }
    };

    public UISEditor() {
        setAlignment(Pos.CENTER_LEFT);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> resetButton.getOnAction().handle(new ActionEvent()));

        resolutionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            layerCanvasPane.setMinSize(newValue.getWidthInfo(), newValue.getHeightInfo());
            layerCanvasPane.setMaxSize(newValue.getWidthInfo(), newValue.getHeightInfo());
            setMinSize(layerCanvasPane.getMinWidth() + tabPane.getMinWidth(), layerCanvasPane.getMinHeight());
            setPrefSize(layerCanvasPane.getMinWidth() + tabPane.getMinWidth(), layerCanvasPane.getMinHeight());
        });
        resolutionChoiceBox.getSelectionModel().select(4);


        /*pane.widthProperty().addListener(observable -> {
            canvas.setWidth(pane.getWidth());
        });
        pane.heightProperty().addListener(observable ->{
            canvas.setHeight(pane.getHeight());
        });*/
        setBackground(Background.fill(Color.BLACK));

        layerCanvasPane.createCanvas("other");
        layerCanvasPane.createCanvas("note");
        layerCanvasPane.createCanvas("press");
        layerCanvasPane.createCanvas("hit");
        layerCanvasPane.createCanvas("touch");

        //画布更新线程常驻
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                layerCanvasPane.clearRect();
                PerspectiveComponent perspectiveComponent=null;
                for (ElementRender e : elements.values()) {

                    String key = "other";
                    if (e instanceof NoteComponent) {
                        key = "note";
                    } else if (e instanceof PressComponent) {
                        key = "press";
                    } else if (e instanceof HitComponent) {
                        key = "hit";
                    } else if (e instanceof TouchComponent) {
                        key = "touch";
                    } else if (e instanceof PerspectiveComponent perspectiveComponent_) {
                        perspectiveComponent = perspectiveComponent_;
                        continue;
                    }

                    GraphicsContext gc = layerCanvasPane.getGraphicsContext2D(key);


                    try {
                        e.render(gc, layerCanvasPane.getWidth(), layerCanvasPane.getHeight());
                    } catch (Exception exception) {

                    }
                }

                if (perspectiveComponent != null) {
                    GraphicsContext gc = layerCanvasPane.getGraphicsContext2D("note");
                    perspectiveComponent.render(gc,layerCanvasPane.getWidth(), layerCanvasPane.getHeight());
                }
                //透视

            }
        };
        //宽度变更
        layerCanvasPane.widthProperty().addListener(observable -> {

            ExpressionVector.expressionCalculator.updateCanvasWidth(layerCanvasPane.getWidth());
            for (ElementRender e : elements.values()) {
                e.canvasResized(layerCanvasPane.getWidth(), layerCanvasPane.getHeight(), Orientation.HORIZONTAL);
            }
        });
        //高度变更
        layerCanvasPane.heightProperty().addListener(observable -> {
            ExpressionVector.expressionCalculator.updateCanvasHeight(layerCanvasPane.getHeight());
            for (ElementRender e : elements.values()) {
                e.canvasResized(layerCanvasPane.getWidth(), layerCanvasPane.getHeight(), Orientation.VERTICAL);
            }
        });


        animationTimer.start();
        HBox.setHgrow(layerCanvasPane, Priority.ALWAYS);
        getChildren().addAll(layerCanvasPane, rightVBox);


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
            UISEditor UISEditor = new UISEditor();
            Scene scene = new Scene(UISEditor);
            scene.getStylesheets().addAll("resources/baseExpansionPack/color/style.css");
            scene.getStylesheets().addAll("resources/baseExpansionPack/color/dark.css");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
    }

    public void openFIle(Path path) {
        Tab tab = new Tab();
        tab.setText(path.getFileName().toString());
        UISCodeArea uisCodeArea = new UISCodeArea(path, reload);
        tab.setContent(uisCodeArea);
        tabPane.getTabs().add(tab);
    }

    private class ResolutionInfo {
        private final double width;
        private final double height;
        private final String name;

        public ResolutionInfo(double width, double height, String name) {
            this.width = width;
            this.height = height;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public double getWidthInfo() {
            return width;
        }

        public double getHeightInfo() {
            return height;
        }

        public String getName() {
            return name;
        }
    }


}
