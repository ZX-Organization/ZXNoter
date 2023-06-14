package team.zxorg.zxnoter.ui.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitBackgroundRender;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitMapRender;
import team.zxorg.zxnoter.ui.render.Render;
import team.zxorg.zxnoter.ui.render.fixedorbit.FixedOrbitRenderInfo;

import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    ZXMap zxMap;
    ArrayList<Render> renders = new ArrayList<>();

    FixedOrbitMapRender previewMapRender;//预览渲染器
    FixedOrbitMapRender selectedMapRender;//选中渲染器
    FixedOrbitMapRender mainMapRender;//主渲染器
    FixedOrbitBackgroundRender backgroundRender;

    long timeline = 0;

    public MapEditor(ZXMap zxMap) {
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.zxMap = zxMap;


        //谱面画板
        CanvasPane mapCanvas = new CanvasPane();
        mapCanvas.setMaxWidth(1000);
        HBox.setHgrow(mapCanvas, Priority.ALWAYS);

        StackPane scrollBar = new StackPane();
        scrollBar.setPrefWidth(120);
        scrollBar.setMinWidth(Region.USE_PREF_SIZE);
        scrollBar.getStyleClass().add("scroll-bar");

        //谱面画板事件
        mapCanvas.setOnMouseClicked(event -> {
            System.out.println(event);
        });


        //预览容器
        Pane scrollPane = new Pane();
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setPrefHeight(120);
        scrollPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        scrollPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(scrollPane, Pos.CENTER);
        StackPane.setMargin(scrollPane, new Insets(0, 0, 50, 0));

        //预览画板
        CanvasPane scrollCanvas = new CanvasPane();

        //预览渲染器
        previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), scrollCanvas, zxMap, "preview", "default");
        previewMapRender.getRenderInfo().timelinePosition = 103950;
        previewMapRender.getRenderInfo().timelineZoom = 0.2f;
        previewMapRender.getRenderInfo().judgedLinePosition = 0.5f;
        scrollBar.getChildren().addAll(scrollCanvas, scrollPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getRenderInfo().timelinePosition = 103950;
        mainMapRender.getRenderInfo().timelineZoom = 1.2f;

        selectedMapRender=new FixedOrbitMapRender(mainMapRender.getRenderInfo(),mapCanvas,zxMap,"selected","default");


        //背景渲染器
        backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getRenderInfo(), zxMap, mapCanvas.canvas, "default");


        //编辑栏
        TabPane tabPane = new TabPane();
        tabPane.setMinWidth(240);
        tabPane.setPrefWidth(240);
        tabPane.setMaxWidth(360);
        HBox.setHgrow(tabPane, Priority.SOMETIMES);

        for (int i = 0; i < 4; i++) {
            Tab tab = new Tab("WDNMD");
            tab.setGraphic(ZXResources.getSvgPane("svg.icons.System.information-line", 18, Color.DARKGREEN));
            tabPane.getTabs().addAll(tab);
        }


        //主体
        HBox bodyPane = new HBox(mapCanvas, scrollBar, tabPane);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);

        //布局栏
        HBox layoutBar = new HBox();
        layoutBar.setPrefHeight(26);
        layoutBar.setMinHeight(Region.USE_PREF_SIZE);
        layoutBar.getStyleClass().add("layout");

        //添加给自己
        this.getChildren().addAll(layoutBar, bodyPane);


    }


    public void updateTimeline(long t) {
        timeline = t;
        mainMapRender.renderInfo.timelinePosition = t;
        previewMapRender.renderInfo.timelinePosition = t;
    }


    @Override
    public void render() {
        previewMapRender.clearRect();
        mainMapRender.clearRect();

        backgroundRender.render();
        previewMapRender.render();
        selectedMapRender.render();
        mainMapRender.render();


    }

    @Override
    public boolean close() {
        return false;
    }
}
