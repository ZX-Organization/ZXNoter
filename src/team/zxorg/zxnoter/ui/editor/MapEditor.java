package team.zxorg.zxnoter.ui.editor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.component.ToolGroupBar;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitBackgroundRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitMapRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitRenderInfo;

import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    ZXMap zxMap;

    ZXMap selectedNoteMap = new ZXMap();//选中的ZXMap

    //ArrayList<Render> renders = new ArrayList<>();

    FixedOrbitMapRender previewMapRender;//预览渲染器
    FixedOrbitMapRender previewSelectedMapRender;//预览选中渲染器

    FixedOrbitMapRender mainMapRender;//主渲染器
    FixedOrbitMapRender mainSelectedMapRender;//选中渲染器
    FixedOrbitBackgroundRender backgroundRender;//主背景渲染器
    //谱面画板
    CanvasPane mapCanvas = new CanvasPane();
    //预览画板
    CanvasPane previewCanvas = new CanvasPane();


    public MapEditor(ZXMap zxMap) {
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.zxMap = zxMap;

        //初始化选中的歌姬
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
        StackPane.setMargin(previewPane, new Insets(0, 0, 50, 0));


        {//事件监听


            //谱面画板事件
            mapCanvas.setOnMouseClicked(event -> {
                long time = mainMapRender.getPositionToTime(event.getY());
                int index = zxMap.findClosestNote(time);
                selectedNoteMap.notes.clear();
                selectedNoteMap.notes.add(zxMap.notes.get(index));
            });

            //滚轮监听
            mapCanvas.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                setTimelinePosition(getTimelinePosition() + (long) deltaY);
            });

            //滚轮监听
            previewCanvas.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                setTimelineZoom((float) (getTimelineZoom() * (deltaY < 0 ? 1.1 : 0.9)));
            });


        }


        //预览渲染器
        previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), previewCanvas, zxMap, "preview", "default");
        previewMapRender.getRenderInfo().judgedLinePosition = 0.5f;
        previewMapRender.getRenderInfo().timelineZoom = 0.18f;


        //预览选中渲染器
        previewSelectedMapRender = new FixedOrbitMapRender(previewMapRender.getRenderInfo(), previewCanvas, selectedNoteMap, "preview-selected", "default");


        previewBar.getChildren().addAll(previewCanvas, previewPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getRenderInfo().timelinePosition = 0;
        mainMapRender.getRenderInfo().timelineZoom = 1.2f;
        mainMapRender.getRenderInfo().judgedLinePosition = 0.95f;

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
        //scrollBar.setRotate(180);
        //scrollBar.valueProperty()
        DoubleProperty a = new SimpleDoubleProperty(10);
        DoubleProperty b = new SimpleDoubleProperty();
        b.bind(a.multiply(2));
        System.out.println(b.getValue());

        //主体
        HBox bodyPane = new HBox(mapCanvas, previewBar, scrollBar, tabPane);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);


        //工具组栏
        ToolGroupBar toolBar = new ToolGroupBar();
        {//状态
            //跳转开头
            toolBar.addButton("state", "svg.icons.media.skip-back-line", "跳转开头");
            //播放
            toolBar.addButton("state", "svg.icons.media.play-line", "播放");
            //暂停
            toolBar.addButton("state", "svg.icons.media.pause-line", "暂停");
            //跳转末尾
            toolBar.addButton("state", "svg.icons.media.skip-forward-line", "跳转末尾");
            //时间
            TextField textField = new TextField("WDNMD");
            toolBar.addNode("state", textField);

            //播放变速
            toolBar.addButton("state", "svg.icons.media.slow-down-line", "播放变速");


        }


        {//工具
            //显示测量
            toolBar.addToggleButton("tool", "svg.icons.design.ruler-line", "测量");
            //吸附编辑
            toolBar.addToggleButton("tool", "svg.icons.design.pencil-ruler-2-line", "吸附");
        }

        {//模式
            ToggleGroup toggleGroup = new ToggleGroup();
            ToggleButton toggleButton;
            //编辑模式
            toggleButton = toolBar.addToggleButton("mode", "svg.icons.design.edit-line", "编辑模式");
            toggleButton.setSelected(true);
            toggleButton.setToggleGroup(toggleGroup);
            //选择模式
            toggleButton = toolBar.addToggleButton("mode", "svg.icons.development.cursor-line", "选择模式");
            toggleButton.setToggleGroup(toggleGroup);
            //只读模式
            toggleButton = toolBar.addToggleButton("mode", "svg.icons.system.eye-line", "只读模式");
            toggleButton.setToggleGroup(toggleGroup);
        }

        {//布局
            //左侧扩展
            toolBar.addToggleButton("layout", "svg.icons.design.layout-left-line", "扩展");
            //滚动栏
            toolBar.addToggleButton("layout", "svg.icons.design.layout-right-2-line", "滚动栏");


            //右侧属性布局
            toolBar.addToggleButton("layout", "svg.icons.design.layout-right-line", "属性栏");
        }


        setTimelinePosition(5000);

        //添加给自己
        this.getChildren().addAll(toolBar, bodyPane);
    }

    public long getTimelinePosition() {
        return mainMapRender.renderInfo.timelinePosition;
    }

    public void setTimelinePosition(long t) {
        mainMapRender.renderInfo.timelinePosition = t;
        previewSelectedMapRender.renderInfo.timelinePosition = t;
    }

    public float getTimelineZoom() {
        return mainMapRender.renderInfo.timelineZoom;
    }

    public void setTimelineZoom(float zoom) {
        mainMapRender.renderInfo.timelineZoom = zoom;
    }


    @Override
    public void render() {
        previewSelectedMapRender.clearRect();
        mainMapRender.clearRect();

        backgroundRender.render();

        previewMapRender.render();
        previewSelectedMapRender.render();

        mainSelectedMapRender.render();
        mainMapRender.render();


    }

    @Override
    public boolean close() {
        return false;
    }
}
