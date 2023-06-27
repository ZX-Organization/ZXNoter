package team.zxorg.zxnoter.ui_old.editor;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.audiomixer.AudioChannel;
import team.zxorg.zxnoter.audiomixer.FFmpeg;
import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.ImdWriter;
import team.zxorg.zxnoter.io.writer.OsuWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.map.mapInfo.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.TimeUtils;
import team.zxorg.zxnoter.ui_old.ZXNApp;
import team.zxorg.zxnoter.ui_old.component.CanvasPane;
import team.zxorg.zxnoter.ui_old.component.HToolGroupBar;
import team.zxorg.zxnoter.ui_old.component.VToolGroupBar;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    MouseEvent pressedMouseEvent = new MouseDragEvent(null, null, null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, null, null);//鼠标按下事件


    ArrayList<RenderBeat> renderBeats = new ArrayList<>();//节拍时间点列表


    Path mapResourcePath;//谱面资源路径


    //预览容器
    Pane previewPane = new Pane();

    //预览堆叠
    StackPane previewBar = new StackPane();


    long hitTime = 0;

    boolean judgeLineAlign = false;//判定线对齐分拍线

    // AudioChannel audioChannel = null;
    int hitAudioID = 0;


    ArrayList<BaseNote> hitsNotes = new ArrayList<>();

    /**
     * 音频同步时间
     */
    long synchronisedTime = 0;
    /**
     * 现实同步时间
     */
    long synchroniseTime = 0;


    FixedOrbitNote tempNote;


    AudioChannel audioChannel;


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
        if (!Files.exists(mapPath)) {
            this.zxMap = new ZXMap();
            zxMap.unLocalizedMapInfo = new UnLocalizedMapInfo();
            zxMap.notes = new ArrayList<>();
            zxMap.timingPoints = new ArrayList<>();


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
                event.consume();
            });

            //滚轮监听
            previewBar.setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                previewMapRender.getInfo().timelineZoom.set(previewMapRender.getInfo().timelineZoom.get() * (deltaY < 0 ? 0.9f : 1.1f));

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
                mainMapRender.renderPoint.setXY(event.getX(), event.getY());
            });

            //按下事件
            mapCanvas.setOnMousePressed(event -> {
                //获取鼠标位置实体键
                mainMapRender.renderPoint.setXY(event.getX(), event.getY());
                mainMapRender.render();
                RenderNote renderNote = mainMapRender.renderNote;//当前选择的键

                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    pressedMouseEvent = event;


                    int orbit = (int) (event.getX() / mainMapRender.getInfo().orbitWidth.get());
                    long time = mainMapRender.getInfo().getPositionToTime(event.getY());

                    if (!event.isControlDown())//时间对齐分拍
                        time = RenderBeat.alignBeatsTime(renderBeats, time);
                    if (renderNote.note == null) {
                        zxFixedOrbitMapEditor.addNote(time, orbit);
                        zxFixedOrbitMapEditor.modifyDone();
                    }
                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (renderNote.complexNote == null) {
                        zxFixedOrbitMapEditor.deleteNote(renderNote.note);
                    } else {
                        zxFixedOrbitMapEditor.deleteChildNote(renderNote.complexNote, renderNote.complexNote.notes.indexOf(renderNote.note));
                    }
                    zxFixedOrbitMapEditor.modifyDone();
                }
            });


            //拖拽事件
            mapCanvas.setOnMouseDragged(event -> {
                RenderNote renderNote = mainMapRender.renderNote;//当前选择的键
                int orbit = (int) (event.getX() / mainMapRender.getInfo().orbitWidth.get());
                long time = mainMapRender.getInfo().getPositionToTime(event.getY());

                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (!event.isControlDown())//时间对齐分拍
                        time = RenderBeat.alignBeatsTime(renderBeats, time);
                    boolean isKeep = event.isShiftDown();
                    {
                        //zxFixedOrbitMapEditor.shadowMap.notes.clear();
                        if (renderNote.note != null) {
                            if (renderNote.complexNote == null) {//不是组合键   简单键
                                if (renderNote.pos.equals(RenderNote.RenderNoteObject.FOOT)) {//尾部 对长键编辑属性

                                    if (event.isShiftDown()) {


                                        int tempOrbit = orbit - renderNote.note.orbit;
                                        long tempTime = time - renderNote.note.timeStamp;
                                        boolean isSide = (tempOrbit != 0);

                                        if (tempNote instanceof ComplexNote complexNote) {
                                            if (complexNote.notes.size() > 0) {
                                                FixedOrbitNote latestNote = complexNote.notes.get(complexNote.notes.size() - 1);
                                                tempOrbit = orbit - latestNote.orbit;//相对之前键的绝对轨道
                                                tempTime = time - latestNote.timeStamp;//相对之前键的绝对时间

                                                isSide = (latestNote.orbit != orbit);
                                                if (latestNote instanceof SlideNote slideNote) {
                                                    isSide = (latestNote.orbit + slideNote.slideArg != orbit);
                                                }
                                            }
                                        }

                                        if (isSide) {
                                            tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.note, tempOrbit, ZXFixedOrbitMapEditor.SLIDE_NOTE);
                                        } else {
                                            if (Math.max(tempTime, 0) != 0)
                                                tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.note, tempTime, ZXFixedOrbitMapEditor.LONG_NOTE);
                                        }


                                    } else {


                                        if (renderNote.note instanceof LongNote) {//长键
                                            zxFixedOrbitMapEditor.modifyPar(renderNote.note, Math.max((int) (time - renderNote.note.timeStamp), 0), true);
                                        } else if (renderNote.note instanceof SlideNote) {//滑键
                                            zxFixedOrbitMapEditor.modifyPar(renderNote.note, orbit - renderNote.note.orbit, true);
                                        }
                                    }
                                } else if (renderNote.pos.equals(RenderNote.RenderNoteObject.HEAD)) {//头部编辑 (移动、转换)
                                    if (event.isAltDown()) {//单键转换
                                        if (renderNote.note.timeStamp != time) {//时间改变 (长键)
                                            zxFixedOrbitMapEditor.modifyPar(
                                                    renderNote.note
                                                    , time - Math.max(renderNote.note.timeStamp, 0)
                                                    , true
                                            );
                                        } else if (renderNote.note.orbit != orbit) {//轨道改变 (滑键)
                                            zxFixedOrbitMapEditor.modifyPar(
                                                    renderNote.note
                                                    , orbit - renderNote.note.orbit
                                                    , true
                                            );
                                        }

                                    } else {//移动
                                        if (event.isShiftDown()) {
                                            int tempOrbit = orbit - renderNote.note.orbit;
                                            long tempTime = time - renderNote.note.timeStamp;
                                            boolean isSide = (tempOrbit != 0);

                                            if (tempNote instanceof ComplexNote complexNote) {
                                                if (complexNote.notes.size() > 0) {
                                                    FixedOrbitNote latestNote = complexNote.notes.get(complexNote.notes.size() - 1);
                                                    tempOrbit = orbit - latestNote.orbit;//相对之前键的绝对轨道
                                                    tempTime = time - latestNote.timeStamp;//相对之前键的绝对时间

                                                    isSide = (latestNote.orbit != orbit);
                                                    if (latestNote instanceof SlideNote slideNote) {
                                                        isSide = (latestNote.orbit + slideNote.slideArg != orbit);
                                                    }
                                                }
                                            }

                                            if (isSide) {
                                                tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.note, tempOrbit, ZXFixedOrbitMapEditor.SLIDE_NOTE);
                                            } else {
                                                if (Math.max(tempTime, 0) != 0)
                                                    tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.note, tempTime, ZXFixedOrbitMapEditor.LONG_NOTE);
                                            }
                                        } else
                                            zxFixedOrbitMapEditor.move(renderNote.note, orbit, time, true);
                                    }
                                }
                            } else { //组合键
                                if (renderNote.pos.equals(RenderNote.RenderNoteObject.BODY)) {//身体
                                    if (renderNote.note instanceof LongNote) {//长键
                                        //身体  对长键编辑轨道
                                        zxFixedOrbitMapEditor.move(renderNote.complexNote, orbit, renderNote.complexNote.notes.indexOf(renderNote.note), isKeep, true);
                                    } else if (renderNote.note instanceof SlideNote) {//滑键
                                        //对滑键修改时间
                                        zxFixedOrbitMapEditor.move(renderNote.complexNote, Math.max(time, renderNote.complexNote.notes.get(renderNote.complexNote.notes.indexOf(renderNote.note) - 1).timeStamp), renderNote.complexNote.notes.indexOf(renderNote.note), isKeep, true);
                                    }
                                } else if (renderNote.pos.equals(RenderNote.RenderNoteObject.FOOT)) {//如果是末尾键
                                    if (event.isShiftDown()) {

                                        int tempOrbit = orbit - renderNote.note.orbit;
                                        long tempTime = time - renderNote.note.timeStamp;
                                        boolean isSide = (tempOrbit != 0);

                                        if (tempNote instanceof ComplexNote complexNote) {
                                            if (complexNote.notes.size() > 0) {
                                                FixedOrbitNote latestNote = complexNote.notes.get(complexNote.notes.size() - 1);
                                                tempOrbit = orbit - latestNote.orbit;//相对之前键的绝对轨道
                                                tempTime = time - latestNote.timeStamp;//相对之前键的绝对时间

                                                isSide = (latestNote.orbit != orbit);
                                                if (latestNote instanceof SlideNote slideNote) {
                                                    isSide = (latestNote.orbit + slideNote.slideArg != orbit);
                                                }
                                            }
                                        }
                                        if (isSide) {
                                            tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.complexNote, tempOrbit, ZXFixedOrbitMapEditor.SLIDE_NOTE);
                                        } else {
                                            if (Math.max(tempTime, 0) != 0)
                                                tempNote = zxFixedOrbitMapEditor.addEndOfNote(renderNote.complexNote, tempTime, ZXFixedOrbitMapEditor.LONG_NOTE);
                                        }
                                    } else {
                                        if (renderNote.complexNote.notes.get(renderNote.complexNote.notes.size() - 1).equals(renderNote.note)) {//头节点  对长键编辑属性
                                            if (renderNote.note instanceof LongNote longNote) {//长键
                                                zxFixedOrbitMapEditor.modifyEndPar(renderNote.complexNote, (Math.max((int) (time - renderNote.note.timeStamp), 0)), true);
                                            } else if (renderNote.note instanceof SlideNote slideNote) {//滑键
                                                zxFixedOrbitMapEditor.modifyEndPar(renderNote.complexNote, orbit - slideNote.orbit, true);
                                            }
                                        }
                                    }
                                }


                            }
                            //zxFixedOrbitMapEditor.move(renderNote.note, time);
                        }
                    }
                }

                //zxFixedOrbitMapEditor.shadowMap.notes.clear();

                //zxFixedOrbitMapEditor.move(renderNote.note, 1);
            });

            //松开事件
            mapCanvas.setOnMouseReleased(event -> {
                //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);
                zxFixedOrbitMapEditor.modifyDone();
                tempNote = null;
                //System.out.println(zxFixedOrbitMapEditor.shadowMap.notes);

            });

            this.setOnKeyPressed(event -> {
                if (event.isControlDown()) {
                    if (event.getCode().equals(KeyCode.Z)) {
                        zxFixedOrbitMapEditor.withdraw();
                    } else if (event.getCode().equals(KeyCode.Y)) {
                        zxFixedOrbitMapEditor.redo();
                    }
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
                if (event.isAltDown()) {//修改分拍
                    RenderBeat renderBeat = RenderBeat.findTime(renderBeats, mainMapRender.getInfo().getPositionToTime(event.getY()));
                    if (renderBeat != null) {
                        if (renderBeat.measure < 1)
                            renderBeat.measure = 1;
                        if (event.getDeltaY() > 0)
                            renderBeat.measure++;
                        else
                            renderBeat.measure--;
                    }
                } else {

                    if (judgeLineAlign) {//判定线对齐分拍
                        long timeNow = (long) (mainMapRender.getInfo().timelinePosition.get() + deltaY);
                        RenderBeat renderBeat = RenderBeat.findTime(renderBeats, timeNow);
                        if (renderBeat != null) {
                            double beatTime = 60000 / renderBeat.timing.absBpm;
                            double measureTime = beatTime / renderBeat.measure;
                            mainMapRender.getInfo().timelinePosition.set(RenderBeat.alignBeatsTime(renderBeats, (long) (mainMapRender.getInfo().timelinePosition.get() + measureTime * (deltaY > 0 ? 1 : -1))));
                        }
                    } else//正常滚动
                        mainMapRender.getInfo().timelinePosition.set(mainMapRender.getInfo().timelinePosition.get() + (long) (deltaY / mainMapRender.getInfo().timelineZoom.get()));
                }

            });


            previewCanvas.setOnScroll(mapCanvas.getOnScroll());


        }


        //预览渲染器
        previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(previewCanvas.canvas), previewCanvas, zxMap, "preview", "default");
        //预览居中
        previewMapRender.getInfo().judgedLinePositionPercentage.setValue(0.5f);
        //预览缩放值
        previewMapRender.getInfo().timelineZoom.setValue(0.18f);


        //预览虚影渲染器
        previewShadowMapRender = new FixedOrbitMapRender(previewMapRender.getInfo(), previewCanvas, zxFixedOrbitMapEditor.shadowMap, "preview-selected", "default");

        previewBackgroundRender = new FixedOrbitPreviewBackgroundRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default");

        previewTimingRender = new FixedOrbitTimingRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default");

        previewBeatLineRender = new FixedOrbitBeatLineRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default", renderBeats);

        previewBar.getChildren().addAll(previewCanvas, previewPane);


        //谱面编辑渲染器
        mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(mapCanvas.canvas), mapCanvas, zxMap, "normal", "default");
        mainMapRender.getInfo().timelineZoom.setValue(1.2f);
        mainMapRender.getInfo().judgedLinePositionPercentage.setValue(0.75f);

        //绑定预览和主部分渲染信息
        previewMapRender.getInfo().orbits.bind(mainMapRender.getInfo().orbits);


        //虚影渲染器
        mainShadowMapRender = new FixedOrbitMapRender(mainMapRender.getInfo(), mapCanvas, zxFixedOrbitMapEditor.shadowMap, "normal-shadow", "default");

        //背景渲染器
        backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

        timingRender = new FixedOrbitTimingRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

        beatLineRender = new FixedOrbitBeatLineRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default", renderBeats);

        //属性栏
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.RIGHT);
        tabPane.setPrefWidth(180);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        HBox.setHgrow(tabPane, Priority.SOMETIMES);

        {
            Tab tab = new Tab("常用");
            tabPane.getTabs().addAll(tab);
        }
        {
            Tab tab = new Tab("OSU");
            tabPane.getTabs().addAll(tab);
        }
        {
            Tab tab = new Tab("IMD");
            tabPane.getTabs().addAll(tab);
        }
        {
            Tab tab = new Tab("全部");
            tabPane.getTabs().addAll(tab);
        }

        /*for (int i = 0; i < 4; i++) {
            Tab tab = new Tab("WDNMD");
            tab.setGraphic(ZXResources.getSvgPane("svg.icons.System.information-line", 18, Color.DARKGREEN));
            tabPane.getTabs().addAll(tab);
        }*/

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
            {
                Button button = sideToolBar.addButton("state", "svg.icons.media.play-line", "播放");
                button.setOnAction(event -> {
                    if (audioChannel != null) {
                        synchroniseTime = System.currentTimeMillis();
                        synchronisedTime = mainMapRender.getInfo().timelinePosition.get();

                        try {
                            audioChannel.setTime(mainMapRender.getInfo().timelinePosition.get());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        audioChannel.play();
                    }
                });
            }


            //暂停
            {
                Button button = sideToolBar.addButton("state", "svg.icons.media.pause-line", "暂停");
                button.setOnAction(event -> {
                    if (audioChannel != null)
                        audioChannel.pause();
                });
            }
            //跳转末尾
            sideToolBar.addButton("state", "svg.icons.editor.align-bottom", "跳转末尾");

        }

        {//工具
            //吸附编辑
            //sideToolBar.addToggleButton("tool", "svg.icons.design.pencil-ruler-2-line", "吸附");

            ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
                mainMapRender.getInfo().timelinePosition.set(RenderBeat.alignBeatsTime(renderBeats, newValue.longValue()));
            };

            //判定线对齐
            ToggleButton toggleButton = sideToolBar.addToggleButton("tool", "svg.icons.zxnoter.judged-line-align", "判定线对齐");
            toggleButton.setOnAction(event -> {
                judgeLineAlign = toggleButton.isSelected();
                if (judgeLineAlign) {
                    mainMapRender.getInfo().timelinePosition.addListener(changeListener);
                } else {
                    mainMapRender.getInfo().timelinePosition.removeListener(changeListener);
                }
            });
        }


        //主体
        HBox bodyPane = new HBox(sideToolBar, mapCanvas, previewBar, scrollBar, tabPane);
        VBox.setVgrow(bodyPane, Priority.ALWAYS);

        //顶部工具组栏
        HToolGroupBar topToolBar = new HToolGroupBar();


        {//文件
            Button button;
            //保存文件
            button = topToolBar.addButton("file", "svg.icons.device.save-line", "保存为");
            button.setOnAction(event -> {
                try {
                    OsuWriter.writeOut(zxMap, OsuWriter.checkLocalizedInfos(zxMap), Paths.get("./docs/output"));
                    ImdWriter.writeOut(zxMap, ImdWriter.checkLocalizedInfos(zxMap), Paths.get("./docs/output"));
                } catch (NoSuchFieldException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }


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


            {//播放变速
                Button button = topToolBar.addButton("state", "svg.icons.media.slow-down-line", "播放变速");
                button.setOnScroll(event -> {
                    if (audioChannel.getPlayState() == AudioChannel.PlayState.PAUSE) {
                        double speed = audioChannel.getPlaySpeed();
                        if (event.getDeltaY() > 0)
                            speed += 0.2;
                        else
                            speed -= 0.2;
                        audioChannel.setPlaySpeed(false, speed);
                        button.getTooltip().setText("播放变速" + Math.round(speed * 100) + "%");
                    }
                });
            }

            {
               /* //分拍
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
                });*/
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
        this.

                getChildren().

                addAll(topToolBar, bodyPane);


        {//初始化音频
            try {

                Path audioPath = mapResourcePath.resolve(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.AudioPath));
                if (Files.exists(audioPath)) {
                    Path workAudioPath = audioPath.getParent().resolve("work.wav");
                    //格式转换
                    System.out.println("格式转化");
                    if (!FFmpeg.audioToWav(audioPath, workAudioPath))
                        throw new RuntimeException("音频转换失败");
                    int id = ZXNApp.audioMixer.addAudio(workAudioPath);
                    audioChannel = ZXNApp.audioMixer.createChannel(id);
                    audioChannel.setVolume(0.13f);
                    //audioChannel.setPlaySpeed(false, 0.8f);

                    mainMapRender.getInfo().timelinePosition.addListener((observable, oldValue, newValue) -> {
                        if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) {
                            try {
                                hitsNotes.clear();
                                audioChannel.setTime(mainMapRender.getInfo().timelinePosition.get());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


                    /*ZXNApp.threadAudioMixer.addAudioChannel();*/
                    /*if (FFmpeg.audioFormatConversion(audioPath, workAudioPath) != 0)
                        throw new RuntimeException("音频转换失败");
                    int id = ZXNApp.audioMixer.addAudio(workAudioPath);
                    audioChannel = ZXNApp.audioMixer.createChannel(id);
                    audioChannel.setVolume(0.15f);*/

                    hitAudioID = ZXNApp.audioMixer.addAudio(ZXResources.getPath("audio.drum-hitfinish"));

                   /* mainMapRender.getInfo().timelinePosition.addListener((observable, oldValue, newValue) -> {
                        if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) {
                            try {

                                //audioChannel.setTime(newValue.longValue());
                                //audioChannel.play();
                                //audioChannel.pause();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    });*/

                    //ZXNApp.audioMixer.addAudioChannel(audioInputChannel);
                }
            } catch (Exception e) {
                System.out.println("初始化播放通道失败");
                throw new RuntimeException(e);
            }


        }


    }


    @Override
    public void render() {
        //if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PLAY))
        //mainMapRender.getInfo().timelinePosition.set(System.currentTimeMillis() - synchroniseTime);

        /*if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PLAY))
            mainMapRender.getInfo().timelinePosition.set(audioChannel.getTime());*/

        new Thread(() -> {
            ArrayList<BaseNote> findsNotes = zxMap.getScaleNotes(mainMapRender.getInfo().timelinePosition.get() - 100, 100, true);
            for (BaseNote note : findsNotes) {
                if (Math.abs(note.timeStamp - mainMapRender.getInfo().timelinePosition.get()) < 20)
                    if (!hitsNotes.contains(note)) {
                        hitsNotes.add(note);
                        if (System.currentTimeMillis() - hitTime > 10) {
                            AudioChannel audioChannel1;
                            try {
                                audioChannel1 = ZXNApp.audioMixer.createChannel(hitAudioID);
                            } catch (UnsupportedAudioFileException | IOException e) {
                                throw new RuntimeException(e);
                            }
                            audioChannel1.setEndBehavior(AudioChannel.EndBehavior.CLOSE);
                            audioChannel1.setVolume(0.23f);
                            audioChannel1.play();
                            hitTime = System.currentTimeMillis();
                        }
                    }
            }
        }).start();

        if (audioChannel != null) {
            if (!audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) {
                mainMapRender.getInfo().timelinePosition.set(audioChannel.getTime());
                //System.out.println(audioChannel.getTime());
            }
        }


        mainMapRender.clearRect();

        if (StackPane.getMargin(previewPane).getTop() == 0) {
            previewShadowMapRender.clearRect();
            previewBackgroundRender.render();
            previewBeatLineRender.render();
            previewMapRender.render();
            previewShadowMapRender.render();
            previewTimingRender.render();
        }

        // mainMapRender.graphics.rotate(1);
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
