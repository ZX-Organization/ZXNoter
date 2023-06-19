package team.zxorg.zxnoter.ui.editor;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.component.HToolGroupBar;
import team.zxorg.zxnoter.ui.component.VToolGroupBar;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitBackgroundRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitMapRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitPreviewBackgroundRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitRenderInfo;

import java.sql.Time;
import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    ZXMap zxMap;

    ZXMap selectedNoteMap = new ZXMap();//选中的ZXMap

    //ArrayList<Render> renders = new ArrayList<>();

    FixedOrbitMapRender previewMapRender;//预览渲染器
    FixedOrbitPreviewBackgroundRender previewBackgroundRender;//预览背景渲染器
    FixedOrbitMapRender previewSelectedMapRender;//预览渲染器

    FixedOrbitMapRender mainMapRender;//主渲染器
    FixedOrbitMapRender mainSelectedMapRender;//选中渲染器
    FixedOrbitBackgroundRender backgroundRender;//主背景渲染器
    //谱面画板
    CanvasPane mapCanvas = new CanvasPane();
    //预览画板
    CanvasPane previewCanvas = new CanvasPane();

    BooleanProperty timelineIsFormat = new SimpleBooleanProperty(true);


    public MapEditor(ZXMap zxMap) {
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.zxMap = zxMap;

        //初始化选中的歌姬  虚影Map
        selectedNoteMap.unLocalizedMapInfo = zxMap.unLocalizedMapInfo;
        selectedNoteMap.notes = new ArrayList<>();

        //谱面画板
        mapCanvas.setMinWidth(200);
        mapCanvas.setMaxWidth(800);
        HBox.setHgrow(mapCanvas, Priority.ALWAYS);

        //预览堆叠
        StackPane previewBar = new StackPane();
        previewBar.setPrefWidth(120);
        previewBar.setMinWidth(Region.USE_PREF_SIZE);
        previewBar.getStyleClass().add("preview-bar");


        //预览容器
        Pane previewPane = new Pane();
        previewPane.getStyleClass().add("preview-pane");
        previewPane.setPrefHeight(120);
        previewPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        previewPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(previewPane, Pos.CENTER);
        StackPane.setMargin(previewPane, new Insets(0, 0, 0, 0));


        {//事件监听


            //谱面画板事件
            mapCanvas.setOnMouseClicked(event -> {
                long time = mainMapRender.getRenderInfo().getPositionToTime(event.getY());
                ArrayList<BaseNote> index = zxMap.findClosestNotes(time);
                //selectedNoteMap.notes.clear();
                selectedNoteMap.notes.addAll(index);
            });

            //滚轮监听
            mapCanvas.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                mainMapRender.getRenderInfo().timelinePosition.set(mainMapRender.getRenderInfo().timelinePosition.get() + (long) (deltaY / mainMapRender.getRenderInfo().timelineZoom.get()));
            });


            previewCanvas.setOnScroll(mapCanvas.getOnScroll());


            //滚轮监听
            previewPane.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                mainMapRender.getRenderInfo().timelineZoom.set(mainMapRender.getRenderInfo().timelineZoom.get() * (deltaY < 0 ? 0.9f : 1.1f));

                long topTime = mainMapRender.getRenderInfo().getPositionToTime(mainMapRender.getHeight());
                long bottomTime = mainMapRender.getRenderInfo().getPositionToTime(0);

                double topPos = previewSelectedMapRender.getRenderInfo().getTimeToPosition(topTime);
                double bottomPos = previewSelectedMapRender.getRenderInfo().getTimeToPosition(bottomTime);

                previewPane.setPrefHeight(topPos - bottomPos);
                //StackPane.setMargin(previewPane, new Insets(bottomPos, 0, 0, 0));
            });


        }


        //预览渲染器
        previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), previewCanvas, zxMap, "preview", "default");
        previewMapRender.getRenderInfo().judgedLinePositionPercentage.setValue(0.5f);
        previewMapRender.getRenderInfo().timelineZoom.setValue(0.18f);


        //预览选中渲染器
        previewSelectedMapRender = new FixedOrbitMapRender(previewMapRender.getRenderInfo(), previewCanvas, selectedNoteMap, "preview-selected", "default");


        previewBackgroundRender = new FixedOrbitPreviewBackgroundRender(previewMapRender.getRenderInfo(), zxMap, previewCanvas.canvas, "default");


        previewBar.getChildren().addAll(previewCanvas, previewPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getRenderInfo().timelineZoom.setValue(1.2f);
        mainMapRender.getRenderInfo().judgedLinePositionPercentage.setValue(0.95f);


        //选中渲染器
        mainSelectedMapRender = new FixedOrbitMapRender(mainMapRender.getRenderInfo(), mapCanvas, selectedNoteMap, "normal-selected", "default");


        //背景渲染器
        backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getRenderInfo(), zxMap, mapCanvas.canvas, "default");


        //属性栏
        TabPane tabPane = new TabPane();

        HBox.setHgrow(tabPane, Priority.SOMETIMES);

        for (int i = 0; i < 4; i++) {
            Tab tab = new Tab("WDNMD");
            tab.setGraphic(ZXResources.getSvgPane("svg.icons.System.information-line", 18, Color.DARKGREEN));
            tabPane.getTabs().addAll(tab);
        }

        //滚动栏
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);


        scrollBar.valueProperty().bindBidirectional(mainMapRender.getRenderInfo().timelinePosition);
        scrollBar.setMin(0);
        scrollBar.maxProperty().bind(mainMapRender.getRenderInfo().noteLastTime);

        scrollBar.setBlockIncrement(10);
        scrollBar.setUnitIncrement(100);
        scrollBar.setRotate(180);
        scrollBar.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0 || event.getDeltaX() != 0) {
                scrollBar.setValue(scrollBar.getValue() + event.getDeltaY());
                event.consume(); // 阻止事件传播
            }
        });


        //侧边工具组栏
        VToolGroupBar sideToolBar = new VToolGroupBar();


        {//模式
            ToggleGroup toggleGroup = new ToggleGroup();
            ToggleButton toggleButton;
            //编辑模式
            toggleButton = sideToolBar.addToggleButton("mode", "svg.icons.design.edit-line", "编辑模式");
            toggleButton.setSelected(true);
            toggleButton.setToggleGroup(toggleGroup);
            //选择模式
            toggleButton = sideToolBar.addToggleButton("mode", "svg.icons.development.cursor-line", "选择模式");
            toggleButton.setToggleGroup(toggleGroup);
            //只读模式
            toggleButton = sideToolBar.addToggleButton("mode", "svg.icons.system.eye-line", "只读模式");
            toggleButton.setToggleGroup(toggleGroup);
        }


        {//状态
            //跳转开头
            sideToolBar.addButton("state", "svg.icons.editor.align-top", "跳转开头");
            //播放
            sideToolBar.addButton("state", "svg.icons.media.play-line", "播放");
            //暂停
            sideToolBar.addButton("state", "svg.icons.media.pause-line", "暂停");
            //跳转末尾
            sideToolBar.addButton("state", "svg.icons.editor.align-bottom", "跳转末尾");

        }

        {//工具
            //吸附编辑
            sideToolBar.addToggleButton("tool", "svg.icons.design.pencil-ruler-2-line", "吸附");
            //判定线对齐
            sideToolBar.addToggleButton("tool", "svg.icons.zxnoter.judged-line-align", "判定线对齐");
        }


        //主体
        HBox bodyPane = new HBox(sideToolBar, mapCanvas, previewBar, scrollBar, tabPane);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);

        //顶部工具组栏
        HToolGroupBar topToolBar = new HToolGroupBar();


        {//时间
            Button button;
            //单位切换
            button = topToolBar.addButton("time", "svg.icons.zxnoter.time-format-line", "格式化时间/总毫秒时间");
            button.setOnAction(event -> {
                timelineIsFormat.set(!timelineIsFormat.get());
                if (timelineIsFormat.get()) {
                    button.setShape(ZXResources.getSvg("svg.icons.zxnoter.time-format-line"));
                } else {
                    button.setShape(ZXResources.getSvg("svg.icons.zxnoter.time-ms-line"));
                }
            });
            TextField textField = new TextField("00.000");
            textField.setPrefWidth(80);
            textField.setAlignment(Pos.CENTER);
            textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    textField.getParent().requestFocus();
                    mainMapRender.getRenderInfo().timelinePosition.set((timelineIsFormat.get() ? TimeUtils.parseTime(textField.getText()) : Long.parseLong(textField.getText())));
                    event.consume();
                }
            });


            //格式化时间转换同步到文本框
            ChangeListener<Object> updateTimelinePosition = (observable, oldValue, newValue) -> textField.setText((timelineIsFormat.get() ? TimeUtils.formatTime(mainMapRender.getRenderInfo().timelinePosition.get()) : mainMapRender.getRenderInfo().timelinePosition.get() + " ms"));

            mainMapRender.getRenderInfo().timelinePosition.addListener(updateTimelinePosition);
            timelineIsFormat.addListener(updateTimelinePosition);


            topToolBar.addNode("time", textField);

        }

        {//状态

            //播放变速
            topToolBar.addButton("state", "svg.icons.media.slow-down-line", "播放变速");
            //分拍
            topToolBar.addButton("state", "svg.icons.zxnoter.beat-16", "分拍");
        }


        {//显示工具
            //显示测量
            topToolBar.addToggleButton("tool", "svg.icons.design.ruler-line", "显示时间");
        }


        {//布局
            //左侧扩展
            topToolBar.addToggleButton("layout", "svg.icons.design.layout-left-line", "扩展");
            //滚动栏
            topToolBar.addToggleButton("layout", "svg.icons.design.layout-right-2-line", "滚动栏");


            //右侧属性布局
            topToolBar.addToggleButton("layout", "svg.icons.design.layout-right-line", "属性栏");
        }


        //添加给自己
        this.getChildren().addAll(topToolBar, bodyPane);
    }


    @Override
    public void render() {
        previewSelectedMapRender.clearRect();
        mainMapRender.clearRect();


        previewBackgroundRender.render();
        previewMapRender.render();
        previewSelectedMapRender.render();

        backgroundRender.render();
        mainSelectedMapRender.render();
        mainMapRender.render();

        //计算

        previewBackgroundRender.mainJudgedLinePositionPercentage.setValue(
                mainMapRender.getRenderInfo().timelinePosition.get()
        );
        previewMapRender.getRenderInfo().timelinePosition.setValue(mainMapRender.getRenderInfo().getPositionToTime(mainMapRender.getHeight() / 2));
    }

    @Override
    public boolean close() {
        return false;
    }
}
