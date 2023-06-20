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
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.component.HToolGroupBar;
import team.zxorg.zxnoter.ui.component.VToolGroupBar;
import team.zxorg.zxnoter.ui.render.basis.RenderPoint;
import team.zxorg.zxnoter.ui.render.fixedorbit.*;

import java.sql.Time;
import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    ZXMap zxMap;


    //ArrayList<Render> renders = new ArrayList<>();

    FixedOrbitMapRender previewMapRender;//预览渲染器
    FixedOrbitPreviewBackgroundRender previewBackgroundRender;//预览背景渲染器
    FixedOrbitMapRender previewSelectedMapRender;//预览渲染器

    FixedOrbitTimingRender previewTimingRender;//时间点渲染器
    FixedOrbitBeatLineRender previewBeatLineRender;//节拍线渲染器

    FixedOrbitMapRender mainMapRender;//主渲染器
    FixedOrbitMapRender mainSelectedMapRender;//选中渲染器
    FixedOrbitBackgroundRender backgroundRender;//主背景渲染器
    FixedOrbitTimingRender timingRender;//时间点渲染器
    FixedOrbitBeatLineRender beatLineRender;//节拍线渲染器
    //谱面画板
    CanvasPane mapCanvas = new CanvasPane();
    //预览画板
    CanvasPane previewCanvas = new CanvasPane();

    BooleanProperty timelineIsFormat = new SimpleBooleanProperty(true);


    ZXFixedOrbitMapEditor zxFixedOrbitMapEditor;
    RenderNote renderNote;//当前选择的键
    MouseEvent editMouseEvent;//编辑鼠标事件

    public MapEditor(ZXMap zxMap) {
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.zxMap = zxMap;


        zxFixedOrbitMapEditor = new ZXFixedOrbitMapEditor(zxMap);


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


            //移动事件
            mapCanvas.setOnMouseMoved(event -> {

            });

            //按下事件
            mapCanvas.setOnMousePressed(event -> {
                editMouseEvent = event;
                renderNote = mainMapRender.drawAllNote(new RenderPoint(event.getX(), event.getY()));
            });

            //拖拽事件
            mapCanvas.setOnMouseDragged(event -> {
                int orbit = (int) (event.getX() / mainMapRender.getRenderInfo().orbitWidth.get());
                if (renderNote != null) {
                    //zxFixedOrbitMapEditor.shadowMap.notes.clear();
                    System.out.println("将" + renderNote.note + "的轨道移动到" + orbit);

                    zxFixedOrbitMapEditor.move(renderNote.note, orbit ,true);
                    System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);
                }

                //zxFixedOrbitMapEditor.shadowMap.notes.clear();

                //zxFixedOrbitMapEditor.move(renderNote.note, 1);
            });

            //松开事件
            mapCanvas.setOnMouseReleased(event -> {
                System.out.println("完成编辑");
                try {
                    //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);
                    zxFixedOrbitMapEditor.modifyDone();
                    //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);
                } catch (Exception e) {
                    System.out.println("急了");
                }
            });


            /*mapCanvas.setOnMouseClicked(event -> {
                RenderNote renderNote = mainMapRender.drawAllNote(new RenderPoint(event.getX(), event.getY()));
                //selectedNoteMap.notes.clear();
                if (renderNote != null) {
                    zxFixedOrbitMapEditor.move(renderNote.note, 1);
                    zxFixedOrbitMapEditor.modifyDone();
                }
            });*/

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
        previewSelectedMapRender = new FixedOrbitMapRender(previewMapRender.getRenderInfo(), previewCanvas, zxFixedOrbitMapEditor.shadowMap, "preview-selected", "default");

        previewBackgroundRender = new FixedOrbitPreviewBackgroundRender(previewMapRender.getRenderInfo(), zxMap, previewCanvas.canvas, "default");

        previewTimingRender = new FixedOrbitTimingRender(previewMapRender.getRenderInfo(), zxMap, previewCanvas.canvas, "default");

        previewBeatLineRender = new FixedOrbitBeatLineRender(previewMapRender.getRenderInfo(), zxMap, previewCanvas.canvas, "default");
        previewBeatLineRender.beats = 0;

        previewBar.getChildren().addAll(previewCanvas, previewPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getRenderInfo().timelineZoom.setValue(1.2f);
        mainMapRender.getRenderInfo().judgedLinePositionPercentage.setValue(0.95f);


        //选中渲染器
        mainSelectedMapRender = new FixedOrbitMapRender(mainMapRender.getRenderInfo(), mapCanvas, zxFixedOrbitMapEditor.shadowMap, "normal-selected", "default");


        //背景渲染器
        backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getRenderInfo(), zxMap, mapCanvas.canvas, "default");

        timingRender = new FixedOrbitTimingRender(mainMapRender.getRenderInfo(), zxMap, mapCanvas.canvas, "default");

        beatLineRender = new FixedOrbitBeatLineRender(mainMapRender.getRenderInfo(), zxMap, mapCanvas.canvas, "default");

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
                    mainMapRender.getRenderInfo().timelinePosition.set((timelineIsFormat.get() ? TimeUtils.parseTime(textField.getText()) : Long.parseLong(textField.getText().replaceAll("ms", "").replaceAll(" ", ""))));
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
            Button button = topToolBar.addButton("state", "svg.icons.zxnoter.beat-16", "分拍");
            button.setOnScroll(new EventHandler<ScrollEvent>() {
                @Override
                public void handle(ScrollEvent event) {
                    if (event.getDeltaY() > 0)
                        beatLineRender.beats++;
                    else
                        beatLineRender.beats--;
                    button.setShape(ZXResources.getSvg("svg.icons.zxnoter.beat-" + beatLineRender.beats));
                }
            });
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
        previewBeatLineRender.render();
        previewMapRender.render();
        previewSelectedMapRender.render();
        previewTimingRender.render();

        backgroundRender.render();
        beatLineRender.render();
        mainSelectedMapRender.render();
        mainMapRender.render();
        timingRender.render();


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
