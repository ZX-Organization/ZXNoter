package team.zxorg.zxnoter.ui.editor;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.component.CanvasPane;
import team.zxorg.zxnoter.ui.render.FixedOrbitMapRender;
import team.zxorg.zxnoter.ui.render.MapRender;

import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    ZXMap zxMap;
    ArrayList<MapRender> mapRenders = new ArrayList<>();

    public MapEditor(ZXMap zxMap) {
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.zxMap = zxMap;
        //谱面画板
        CanvasPane mapCanvas = new CanvasPane();
        HBox.setHgrow(mapCanvas, Priority.ALWAYS);

        StackPane scrollBar = new StackPane();
        scrollBar.setPrefWidth(120);
        scrollBar.setMinWidth(Region.USE_PREF_SIZE);
        scrollBar.getStyleClass().add("scroll-bar");


        {


            this.setOnMouseClicked(event -> {
                int index = zxMap.findClosestNote(103950);
                BaseNote note = zxMap.notes.get(index);
                zxMap.moveNote(note, note.timeStamp + 10);
            });


        }


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
        FixedOrbitMapRender scrollMapRender = new FixedOrbitMapRender(scrollCanvas, zxMap, "small");
        scrollMapRender.fixedOrbitRenderInfo.orbits = 4;
        scrollMapRender.fixedOrbitRenderInfo.timelinePosition = 103950;
        scrollMapRender.fixedOrbitRenderInfo.timelineZoom = 0.2f;
        scrollMapRender.fixedOrbitRenderInfo.judgedLinePosition = 0.5f;
        scrollBar.getChildren().addAll(scrollCanvas, scrollPane);



        //编辑栏
        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(240);
        Tab tab = new Tab("WDNMD");

        tab.setGraphic(ZXResources.getSvgPane("svg.icons.System.information-line", 18, Color.DARKGREEN));
        tabPane.getTabs().addAll(tab);

        this.getChildren().addAll(mapCanvas, scrollBar, tabPane);

        FixedOrbitMapRender fixedOrbitMapRender = new FixedOrbitMapRender(mapCanvas, zxMap, null);
        fixedOrbitMapRender.fixedOrbitRenderInfo.orbits = 4;
        fixedOrbitMapRender.fixedOrbitRenderInfo.timelinePosition = 103950;
        fixedOrbitMapRender.fixedOrbitRenderInfo.timelineZoom = 1.2f;

        //画布更新线程常驻
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // fixedOrbitMapRender.fixedOrbitRenderInfo.timelinePosition++;
                scrollMapRender.clearRect();
                scrollMapRender.render();
                fixedOrbitMapRender.clearRect();
                fixedOrbitMapRender.render();
            }
        };
        animationTimer.start();


    }
}
