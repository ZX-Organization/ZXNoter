package team.zxorg.newskin.uis.ui;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import team.zxorg.newskin.DeviceType;
import team.zxorg.newskin.ResolutionInfo;
import team.zxorg.newskin.uis.ExpressionCalculator;
import team.zxorg.newskin.uis.UISComponent;
import team.zxorg.newskin.uis.UISSkin;
import team.zxorg.newskin.uis.component.AbstractComponentRenderer;
import team.zxorg.newskin.uis.component.AnimationComponentRenderer;
import team.zxorg.ui.component.LayerCanvasPane;
import team.zxorg.zxncore.ZXLogger;
import team.zxorg.zxncore.ZXVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class UISEditor extends HBox {
    UISSkin skin;
    Perspective perspective = new Perspective();
    ExpressionCalculator expressionCalculator = new ExpressionCalculator();

    ArrayList<AbstractComponentRenderer> componentRenders = new ArrayList<>();
    HashMap<UISComponent, AbstractComponentRenderer> componentMap = new HashMap<>();

    LayerCanvasPane layerCanvasPane = new LayerCanvasPane() {
        {
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1), new Insets(0))));
        }
    };
    TabPane tabPane = new TabPane() {
        {
            VBox.setVgrow(this, Priority.ALWAYS);
            setPrefWidth(640);
            setMinWidth(640);
        }
    };

    ChoiceBox<DeviceType> deviceTypeChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(DeviceType.values());
            setPrefWidth(60);
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateSize();
            });
            //getSelectionModel().selectLast();
        }
    };
    ChoiceBox<ResolutionInfo> resolutionChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(ResolutionInfo.values());
            setPrefWidth(100);
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                deviceTypeChoiceBox.setValue(newValue.getDevice());
                updateSize();
            });
            getSelectionModel().selectLast();
        }
    };

    ChoiceBox<UnitInfo> unitChoiceBox = new ChoiceBox<>() {
        {
            getItems().addAll(
                    new UnitInfo("像素", "px", 1),
                    new UnitInfo("游戏像素", "gpx", 0),
                    new UnitInfo("百分比", "%", 2)
            );
            valueProperty().addListener((observable, oldValue, newValue) -> {
                //measuringRulerRenderer.unit = newValue;
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
                    updateSize();
                }
            });
        }
    };
    private File lastDirectory = new File("D:\\malody\\skin\\Evans-进行中"); // 记录上一次选择的目录

    public UISEditor() {
        setAlignment(Pos.CENTER_LEFT);
        tabPane.getSelectionModel().selectedItemProperty().addListener(observable -> reloadButton.getOnAction().handle(new ActionEvent()));




        /*pane.widthProperty().addListener(observable -> {
            canvas.setWidth(pane.getWidth());
        });
        pane.heightProperty().addListener(observable ->{
            canvas.setHeight(pane.getHeight());
        });*/
        setBackground(Background.fill(Color.BLACK));

        layerCanvasPane.createCanvas("bottom");
        layerCanvasPane.createCanvas("3d").setEffect(perspective.getEffect());
        layerCanvasPane.createCanvas("top");

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
                layerCanvasPane.clearRect();
                for (AbstractComponentRenderer cr : componentRenders) {
                    GraphicsContext gc = layerCanvasPane.getGraphicsContext2D(cr.getLayoutName());
                    try {
                        AbstractComponentRenderer anim = componentMap.get(cr.getMotion());
                        gc.save();
                        if (anim instanceof AnimationComponentRenderer renderer) {
                            renderer.update(gc, expressionCalculator.getCanvasWidth(), expressionCalculator.getCanvasHeight(), cr);
                        }
                        cr.draw(gc, expressionCalculator.getCanvasWidth(), expressionCalculator.getCanvasHeight());
                        gc.restore();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        animationTimer.start();
        HBox.setHgrow(layerCanvasPane, Priority.ALWAYS);
        getChildren().addAll(sideVBox, layerCanvasPane);
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

    public void update() {
        if (skin == null)
            return;
        skin.setDeviceType(deviceTypeChoiceBox.getValue());
        setCanvasSize(resolutionChoiceBox.getValue().getAspectRatio(), scalingFactorSlider.getValue());
        skin.updateRenderer(componentRenders, componentMap, layerCanvasPane);
        perspective.setAngle(skin.getAngle());

    }

    public void updateSize() {
        update();
        for (AbstractComponentRenderer component : componentMap.values()) {
            component.reloadPos();
        }
    }

    public void reload() {
        if (tabPane.getSelectionModel().getSelectedItem() != null)
            if (tabPane.getSelectionModel().getSelectedItem().getContent() instanceof VirtualizedScrollPane uis) {
                componentRenders.clear();
                componentMap.clear();

                skin = new UISSkin(((UISCodeArea) uis.getContent()).getFile(), expressionCalculator);
                setCanvasSize(resolutionChoiceBox.getValue().getAspectRatio(), scalingFactorSlider.getValue());

                perspective.setAngle(skin.getAngle());
                for (UISComponent component : skin.getComponents()) {
                    AbstractComponentRenderer render = AbstractComponentRenderer.toRenderer(component, layerCanvasPane);
                    if (render != null) {
                        componentRenders.add(render);
                        componentMap.put(component, render);

                    }

                }
                UISSkin.sortRenders(componentRenders);
            }

    }

    public void setCanvasSize(double aspectRatio, double zoomRate) {
        double unitHeight = expressionCalculator.getUnitCanvasHeight();
        double width = unitHeight * aspectRatio * zoomRate;
        double height = unitHeight * zoomRate;

        layerCanvasPane.setMinSize(width, height);
        layerCanvasPane.setMaxSize(width, height);
        setMinSize(width + tabPane.getMinWidth(), height);
        setPrefSize(width + tabPane.getMinWidth(), height);
        expressionCalculator.setCanvasSize(width, height);
        perspective.setSize(width, height);
        //expressionCalculator.setCanvasSize(width, height);
        /*ExpressionVector.expressionCalculator.updateCanvasWidth(width);
        for (ElementRenderer e : elements.values()) {
            e.canvasResized(width, height, Orientation.HORIZONTAL);
        }
        ExpressionVector.expressionCalculator.updateCanvasHeight(height);
        for (ElementRenderer e : elements.values()) {
            e.canvasResized(width, height, Orientation.VERTICAL);
        }*/
        //reload();
    }

    public void openFIle(Path path) {
        Tab tab = new Tab();
        tab.setText(path.getFileName().toString());
        UISCodeArea uisCodeArea = null;
        try {
            uisCodeArea = new UISCodeArea(path, this::update);
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
    }

    public record UnitInfo(String name, String unit, int id) {
        @Override
        public String toString() {
            return name + "(" + unit + ")";
        }

    }


    Button reloadButton = new Button("重载") {
        {
            setOnAction(event -> reload());
        }
    };


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


    HBox toolbar = new HBox(reloadButton, resolutionChoiceBox, deviceTypeChoiceBox, unitChoiceBox, scalingFactorLabel, scalingFactorSlider, openFileButton) {
        {
            setMinHeight(40);
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0), new Insets(0))));
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(8);
            setPadding(new Insets(0, 8, 0, 8));
        }
    };

    VBox sideVBox = new VBox(toolbar, tabPane) {
        {
            setHgrow(this, Priority.ALWAYS);
            setPrefWidth(260);
            setBackground(Background.fill(Color.BLACK));
            setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 1, 0, 0), new Insets(0))));
        }
    };


}
