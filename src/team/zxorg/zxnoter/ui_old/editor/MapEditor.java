package team.zxorg.zxnoter.ui_old.editor;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.audiochannel.channel.MemoryAudioInputChannel;
import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.TimeUtils;
import team.zxorg.zxnoter.ui_old.component.CanvasPane;
import team.zxorg.zxnoter.ui_old.component.HToolGroupBar;
import team.zxorg.zxnoter.ui_old.component.VToolGroupBar;
import team.zxorg.zxnoter.ui_old.render.basis.RenderPoint;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MapEditor extends BaseEditor {
    /**
     * 需要对应的zxmap
     */
    public ZXMap zxMap;


    //ArrayList<Render> renders = new ArrayList<>();

    FixedOrbitMapRender previewMapRender;//预览渲染器
    FixedOrbitPreviewBackgroundRender previewBackgroundRender;//预览背景渲染器
    FixedOrbitMapRender previewShadowMapRender;//预览渲染器

    FixedOrbitTimingRender previewTimingRender;//时间点渲染器
    FixedOrbitBeatLineRender previewBeatLineRender;//节拍线渲染器

    FixedOrbitMapRender mainMapRender;//主渲染器
    FixedOrbitMapRender mainShadowMapRender;//选中渲染器
    FixedOrbitBackgroundRender backgroundRender;//主背景渲染器
    FixedOrbitTimingRender timingRender;//时间点渲染器
    FixedOrbitBeatLineRender beatLineRender;//节拍线渲染器
    //谱面画板
    CanvasPane mapCanvas = new CanvasPane();
    //预览画板
    CanvasPane previewCanvas = new CanvasPane();

    BooleanProperty timelineIsFormat = new SimpleBooleanProperty(true);


    ZXFixedOrbitMapEditor zxFixedOrbitMapEditor;
    ObjectProperty<RenderNote> renderNote = new SimpleObjectProperty<>();//当前选择的键
    MouseEvent pressedMouseEvent = new MouseDragEvent(null, null, null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, null, null);//鼠标按下事件

    MemoryAudioInputChannel audioInputChannel;//音频文件通道

    ArrayList<RenderBeat> renderBeats = new ArrayList<>();//节拍时间点列表


    Path mapResourcePath;//谱面资源路径


    //预览容器
    Pane previewPane = new Pane();

    //预览堆叠
    StackPane previewBar = new StackPane();

    public MapEditor(Path mapPath) {

        try {
            this.zxMap = new OsuReader().read(mapPath);
        } catch (Exception e) {
            try {
                this.zxMap = new ImdReader().read(mapPath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        this.mapResourcePath = mapPath.getParent();
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        RenderBeat.upDateBeats(zxMap, renderBeats);

        zxFixedOrbitMapEditor = new ZXFixedOrbitMapEditor(zxMap);


        //谱面画板
        mapCanvas.setMinWidth(200);
        mapCanvas.setMaxWidth(800);
        HBox.setHgrow(mapCanvas, Priority.ALWAYS);


        //预览堆叠初始化
        previewBar.setPrefWidth(120);
        previewBar.setMinWidth(Region.USE_PREF_SIZE);
        previewBar.getStyleClass().add("preview-bar");


        //预览容器初始化
        previewPane.getStyleClass().add("preview-pane");
        previewPane.setPrefHeight(120);
        previewPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        previewPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(previewPane, Pos.CENTER);
        StackPane.setMargin(previewPane, new Insets(0, 0, 0, 0));


        {//预览栏事件
            //滚轮监听
            previewPane.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                mainMapRender.getInfo().timelineZoom.set(mainMapRender.getInfo().timelineZoom.get() * (deltaY < 0 ? 0.9f : 1.1f));

                long topTime = mainMapRender.getInfo().getPositionToTime(mainMapRender.getHeight());
                long bottomTime = mainMapRender.getInfo().getPositionToTime(0);

                double topPos = previewShadowMapRender.getInfo().getTimeToPosition(topTime);
                double bottomPos = previewShadowMapRender.getInfo().getTimeToPosition(bottomTime);
                previewPane.setPrefHeight(topPos - bottomPos);
            });
            //按下时的原始位置
            LongProperty previewPanePressedTime = new SimpleLongProperty();
            previewPane.setOnMousePressed(event -> {
                pressedMouseEvent = event;
                previewPanePressedTime.set(mainMapRender.getInfo().timelinePosition.get());
            });
            previewPane.setOnMouseReleased(event -> {
                previewPanePressedTime.set(0);
                StackPane.setMargin(previewPane, new Insets(0, 0, 0, 0));
            });
            previewPane.setOnMouseDragged(event -> {
                double offset = pressedMouseEvent.getSceneY() - event.getSceneY();
                mainMapRender.getInfo().timelinePosition.set(previewPanePressedTime.get() + previewMapRender.getInfo().getHeightToTime(offset));
                StackPane.setMargin(previewPane, new Insets(-offset, 0, offset, 0));
            });


        }


        {//事件监听


            //移动事件
            mapCanvas.setOnMouseMoved(event -> {
                RenderNote renderNote1 = mainMapRender.drawAllNote(new RenderPoint(event.getX(), event.getY()));
                if (renderNote1 != null)
                    if (renderNote.get() != null)
                        if (!renderNote1.note.equals(renderNote.get().note))
                            renderNote.set(renderNote1);
            });

            //按下事件
            mapCanvas.setOnMousePressed(event -> {
                pressedMouseEvent = event;
                renderNote.set(mainMapRender.drawAllNote(new RenderPoint(event.getX(), event.getY())));
            });

            //拖拽事件
            mapCanvas.setOnMouseDragged(event -> {
                int orbit = (int) (event.getX() / mainMapRender.getInfo().orbitWidth.get());
                long time = mainMapRender.getInfo().getPositionToTime(event.getY());
                time = RenderBeat.alignBeatsTime(renderBeats, time);
                if (renderNote != null) {
                    //zxFixedOrbitMapEditor.shadowMap.notes.clear();

                    RenderNote renderNote1 = renderNote.get();
                    if (renderNote1 != null) {
                        if (renderNote1.complexNote == null)
                            zxFixedOrbitMapEditor.move(renderNote1.note, orbit, true);
                        else {//组合键
                            System.out.println(zxMap.notes.indexOf(renderNote1.complexNote));
                            zxFixedOrbitMapEditor.move(renderNote1.complexNote,orbit, renderNote1.complexNote.notes.indexOf(renderNote1.note) ,false, true);
                        }
                        //zxFixedOrbitMapEditor.move(renderNote.note, time);
                    }
                }

                //zxFixedOrbitMapEditor.shadowMap.notes.clear();

                //zxFixedOrbitMapEditor.move(renderNote.note, 1);
            });

            //松开事件
            mapCanvas.setOnMouseReleased(event -> {
                //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);
                zxFixedOrbitMapEditor.modifyDone();
                //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);

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
                mainMapRender.getInfo().timelinePosition.set(mainMapRender.getInfo().timelinePosition.get() + (long) (deltaY / mainMapRender.getInfo().timelineZoom.get()));
            });


            previewCanvas.setOnScroll(mapCanvas.getOnScroll());


        }


        //预览渲染器
        previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(previewCanvas.canvas), previewCanvas, zxMap, "preview", "default");
        //预览居中
        previewMapRender.getInfo().judgedLinePositionPercentage.setValue(0.5f);
        //预览缩放值
        previewMapRender.getInfo().timelineZoom.setValue(0.18f);


        //预览选中渲染器
        previewShadowMapRender = new FixedOrbitMapRender(previewMapRender.getInfo(), previewCanvas, zxFixedOrbitMapEditor.shadowMap, "preview-selected", "default");

        previewBackgroundRender = new FixedOrbitPreviewBackgroundRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default");

        previewTimingRender = new FixedOrbitTimingRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default");

        previewBeatLineRender = new FixedOrbitBeatLineRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default", renderBeats);

        previewBar.getChildren().addAll(previewCanvas, previewPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(mapCanvas.canvas), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getInfo().timelineZoom.setValue(1.2f);
        mainMapRender.getInfo().judgedLinePositionPercentage.setValue(0.95f);

        //绑定预览和主部分渲染信息
        previewMapRender.getInfo().orbits.bind(mainMapRender.getInfo().orbits);


        //选中渲染器
        mainShadowMapRender = new FixedOrbitMapRender(mainMapRender.getInfo(), mapCanvas, zxFixedOrbitMapEditor.shadowMap, "normal-selected", "default");
        mainShadowMapRender.singleRenderNote.bind(renderNote);

        //背景渲染器
        backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

        timingRender = new FixedOrbitTimingRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

        beatLineRender = new FixedOrbitBeatLineRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default", renderBeats);

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


        scrollBar.valueProperty().bindBidirectional(mainMapRender.getInfo().timelinePosition);
        scrollBar.setMin(0);
        scrollBar.maxProperty().bind(mainMapRender.getInfo().noteLastTime);

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
                    mainMapRender.getInfo().timelinePosition.set((timelineIsFormat.get() ? TimeUtils.parseTime(textField.getText()) : Long.parseLong(textField.getText().replaceAll("ms", "").replaceAll(" ", ""))));
                    event.consume();
                }
            });


            //格式化时间转换同步到文本框
            ChangeListener<Object> updateTimelinePosition = (observable, oldValue, newValue) -> textField.setText((timelineIsFormat.get() ? TimeUtils.formatTime(mainMapRender.getInfo().timelinePosition.get()) : mainMapRender.getInfo().timelinePosition.get() + " ms"));

            mainMapRender.getInfo().timelinePosition.addListener(updateTimelinePosition);
            timelineIsFormat.addListener(updateTimelinePosition);


            topToolBar.addNode("time", textField);

        }

        {//状态

            //播放变速
            topToolBar.addButton("state", "svg.icons.media.slow-down-line", "播放变速");
            {
                //分拍
                Button button = topToolBar.addButton("state", "svg.icons.zxnoter.beat-16", "分拍");
                button.setOnScroll(event -> {
                    RenderBeat renderBeat = RenderBeat.findTime(renderBeats, mainMapRender.getInfo().timelinePosition.get());
                    if (renderBeat != null) {
                        if (event.getDeltaY() > 0)
                            renderBeat.measure++;
                        else
                            renderBeat.measure--;
                        button.setShape(ZXResources.getSvg("svg.icons.zxnoter.beat-" + renderBeat.measure));
                    }
                });
            }

            {
                //轨道数
                Button button = topToolBar.addButton("state", "svg.icons.zxnoter.4k", "轨道数");
                button.setOnScroll(event -> {
                    int orbits = mainMapRender.getInfo().orbits.get();
                    if (event.getDeltaY() > 0)
                        orbits++;
                    else
                        orbits--;
                    zxMap.unLocalizedMapInfo.addInfo(ZXMInfo.KeyCount, String.valueOf(orbits));
                    button.setShape(ZXResources.getSvg("svg.icons.zxnoter." + orbits + "k"));
                    mainMapRender.getInfo().orbits.set(orbits);
                });
            }
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


        /*{//初始化音频
            try {

                Path audioPath = mapResourcePath.resolve(zxMap.unLocalizedMapInfo.getInfo("AudioPath").trim());
                if (Files.exists(audioPath)) {
                    Path workAudioPath = audioPath.getParent().resolve("work.wav");
                    //格式转换
                    if (FFmpeg.audioFormatConversion(audioPath, workAudioPath) != 0)
                        throw new RuntimeException("音频转换失败");


                    audioInputChannel = new MemoryAudioInputChannel(ZXNApp.audioFormat, workAudioPath);
                    audioInputChannel.setFramePosition(500);
                    //ZXNApp.audioMixer.addAudioChannel(audioInputChannel);
                }
            } catch (Exception e) {
                System.out.println("初始化播放通道失败");
                throw new RuntimeException(e);
            }


        }*/

    }


    @Override
    public void render() {

        mainMapRender.clearRect();

        if (StackPane.getMargin(previewPane).getTop() == 0) {
            previewShadowMapRender.clearRect();
            previewBackgroundRender.render();
            previewBeatLineRender.render();
            previewMapRender.render();
            previewShadowMapRender.render();
            previewTimingRender.render();
        }

        backgroundRender.render();
        beatLineRender.render();
        mainMapRender.render();
        mainShadowMapRender.render();
        timingRender.render();


        //计算

        previewBackgroundRender.mainJudgedLinePositionPercentage.setValue(
                mainMapRender.getInfo().timelinePosition.get()
        );
        previewMapRender.getInfo().timelinePosition.setValue(mainMapRender.getInfo().getPositionToTime(mainMapRender.getHeight() / 2));
    }

    @Override
    public boolean close() {
        return false;
    }
}
