package team.zxorg.zxnoter.ui_old.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import team.zxorg.zxnoter.info.map.ImdInfo;
import team.zxorg.zxnoter.info.map.OsuInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.io.reader.ImdReader;
import team.zxorg.zxnoter.io.reader.McReader;
import team.zxorg.zxnoter.io.reader.OsuReader;
import team.zxorg.zxnoter.io.writer.ImdWriter;
import team.zxorg.zxnoter.io.writer.McWriter;
import team.zxorg.zxnoter.io.writer.OsuWriter;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.editor.ZXFixedOrbitMapEditor;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.FixedOrbitNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.fixedorbit.SlideNote;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.note.timing.ZXTiming;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.sound.audiomixer.AudioChannel;
import team.zxorg.zxnoter.sound.audiomixer.AudioData;
import team.zxorg.zxnoter.sound.audiomixer.FFmpeg;
import team.zxorg.zxnoter.ui_old.TimeUtils;
import team.zxorg.zxnoter.ui_old.ZXNApp;
import team.zxorg.zxnoter.ui_old.component.CanvasPane;
import team.zxorg.zxnoter.ui_old.component.HToolGroupBar;
import team.zxorg.zxnoter.ui_old.component.InfoPane;
import team.zxorg.zxnoter.ui_old.component.VToolGroupBar;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MapEditor extends BaseEditor {

    /**
     * 需要对应的zxmap
     */
    public ZXMap zxMap;
    Tab tab;

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

    FixedOrbitInfoRender infoRender;//信息渲染器
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
    AudioData hitAudioID = null;


    ArrayList<BaseNote> hitsNotes = new ArrayList<>();


    float hitKeyVolume = 0.1f;


    FixedOrbitNote tempNote;


    AudioChannel audioChannel;

    ToggleButton isReverse;
    Button globalSubbeatButton;

    long audioTimeLength = 10000;//音频时长   (10s)

    LongProperty mapTimeLength = new SimpleLongProperty();//谱面时长

    int globalMeasure = 0;//全局分拍

    LongProperty audioTimeOffset = new SimpleLongProperty(0);//音频时间偏移

    public MapEditor(Path mapPath, Tab tab) {
        this.tab = tab;


        if (mapPath == null) {
            this.zxMap = new ZXMap();
        } else {


            try {
                if (mapPath.toString().endsWith(".zxn")) {
                    this.zxMap = new ZXMap(mapPath);
                } else if (mapPath.toString().endsWith(".osu")) {
                    this.zxMap = new OsuReader().read(mapPath);
                } else if (mapPath.toString().endsWith(".imd")) {
                    this.zxMap = new ImdReader().read(mapPath);
                } else if (mapPath.toString().endsWith(".mc")) {
                    this.zxMap = new McReader().read(mapPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (zxMap == null) throw new RuntimeException("无法解析！");
        try {

            {
                String titleStr = zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode);
                if ("".equals(titleStr) || titleStr == null) titleStr = zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title);
                tab.setText(titleStr);
            }

            if (mapPath != null) this.mapResourcePath = mapPath.getParent();
            this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


            zxFixedOrbitMapEditor = new ZXFixedOrbitMapEditor(zxMap);


            //谱面画板
            mapCanvas.setMinWidth(200);
            mapCanvas.setMaxWidth(1000);
            mapCanvas.getStyleClass().add("map-canvas");
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
                    float zoom = mainMapRender.getInfo().timelineZoom.get() * (deltaY < 0 ? 0.9f : 1.1f);
                    zoom = Math.max(zoom, 0.1f);
                    zoom = Math.min(zoom, 1.0f);
                    mainMapRender.getInfo().timelineZoom.set(zoom);

                    long topTime = mainMapRender.getInfo().getPositionToTime(mainMapRender.getHeight());
                    long bottomTime = mainMapRender.getInfo().getPositionToTime(0);

                    double topPos = previewShadowMapRender.getInfo().getTimeToPosition(topTime);
                    double bottomPos = previewShadowMapRender.getInfo().getTimeToPosition(bottomTime);
                    double height = topPos - bottomPos;
                    if (height > previewBar.getHeight()) height = previewBar.getHeight();
                    previewPane.setPrefHeight(height);
                    event.consume();
                });

                //滚轮监听
                previewBar.setOnScroll(event -> {
                    double deltaY = event.getDeltaY();


                    float zoom = previewMapRender.getInfo().timelineZoom.get() * (deltaY < 0 ? 0.9f : 1.1f);
                    zoom = Math.max(zoom, 0.1f);
                    zoom = Math.min(zoom, 0.3f);

                    previewMapRender.getInfo().timelineZoom.set(zoom);

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
                                                zxFixedOrbitMapEditor.modifyPar(renderNote.note, time - Math.max(renderNote.note.timeStamp, 0), true);
                                            } else if (renderNote.note.orbit != orbit) {//轨道改变 (滑键)
                                                zxFixedOrbitMapEditor.modifyPar(renderNote.note, orbit - renderNote.note.orbit, true);
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
                                            } else zxFixedOrbitMapEditor.move(renderNote.note, orbit, time, true);
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
                    if (event.getCode().equals(KeyCode.SPACE)) {
                        if (audioChannel != null) {
                            if (audioChannel.getPlayState() == AudioChannel.PlayState.PLAY) {
                                audioChannel.pause();
                            } else {
                                audioChannel.play();
                            }
                        }
                    }

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
                            if (event.getDeltaY() > 0) renderBeat.measure++;
                            else renderBeat.measure--;
                            renderBeat.measure = Math.max(renderBeat.measure, 1);
                        }
                    } else {

                        if (!isReverse.isSelected()) deltaY = -deltaY;
                        if (judgeLineAlign) {//判定线对齐分拍
                            long timeNow = (long) (mainMapRender.getInfo().timelinePosition.get() + deltaY);
                            RenderBeat renderBeat = RenderBeat.findTime(renderBeats, timeNow);
                            if (renderBeat != null) {
                                double beatTime = 60000 / renderBeat.timing.absBpm;
                                renderBeat.measure = Math.max(renderBeat.measure, 1);
                                double measureTime = beatTime / renderBeat.measure;
                                mainMapRender.getInfo().timelinePosition.set(RenderBeat.alignBeatsTime(renderBeats, (long) (mainMapRender.getInfo().timelinePosition.get() + measureTime * (deltaY > 0 ? 1 : -1))));
                            }
                        } else//正常滚动
                            mainMapRender.getInfo().timelinePosition.set(mainMapRender.getInfo().timelinePosition.get() + (long) (deltaY / mainMapRender.getInfo().timelineZoom.get()));
                    }

                });


                //previewCanvas.setOnScroll(mapCanvas.getOnScroll());


            }


            //预览渲染器
            previewMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(previewCanvas.canvas), previewCanvas, zxMap, "preview", "default");
            //预览居中
            previewMapRender.getInfo().judgedLinePositionPercentage.setValue(0.5f);
            //预览缩放值
            previewMapRender.getInfo().timelineZoom.setValue(0.1f);


            //预览虚影渲染器
            previewShadowMapRender = new FixedOrbitMapRender(previewMapRender.getInfo(), previewCanvas, zxFixedOrbitMapEditor.shadowMap, "preview-selected", "default");

            previewBackgroundRender = new FixedOrbitPreviewBackgroundRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default", mapTimeLength);

            previewTimingRender = new FixedOrbitTimingRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "default");

            previewBeatLineRender = new FixedOrbitBeatLineRender(previewMapRender.getInfo(), zxMap, previewCanvas.canvas, "preview", renderBeats, mapTimeLength);

            previewBar.getChildren().addAll(previewCanvas, previewPane);
            HBox.setMargin(previewBar, new Insets(0, 0, 0, 26));

            //谱面编辑渲染器
            mainMapRender = new FixedOrbitMapRender(new FixedOrbitRenderInfo(mapCanvas.canvas), mapCanvas, zxMap, "normal", "default");
            mainMapRender.getInfo().timelineZoom.setValue(0.4f);
            mainMapRender.getInfo().judgedLinePositionPercentage.setValue(0.75f);

            //绑定预览和主部分渲染信息
            previewMapRender.getInfo().orbits.bind(mainMapRender.getInfo().orbits);


            //虚影渲染器
            mainShadowMapRender = new FixedOrbitMapRender(mainMapRender.getInfo(), mapCanvas, zxFixedOrbitMapEditor.shadowMap, "normal-shadow", "default");

            //背景渲染器
            backgroundRender = new FixedOrbitBackgroundRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

            timingRender = new FixedOrbitTimingRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default");

            beatLineRender = new FixedOrbitBeatLineRender(mainMapRender.getInfo(), zxMap, mapCanvas.canvas, "default", renderBeats, mapTimeLength);
            beatLineRender.showSubBeats = true;

            //属性栏
            TabPane tabPane = new TabPane();
            tabPane.setSide(Side.RIGHT);
            tabPane.setMinWidth(240);
            tabPane.setPrefWidth(240);
            tabPane.setMaxWidth(400);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            HBox.setHgrow(tabPane, Priority.SOMETIMES);


            zxMap.unLocalizedMapInfo.addListener((info, value) -> {
                if (info.equals(ZXMInfo.KeyCount)) {//同步键数
                    mainMapRender.getInfo().orbits.set(Integer.parseInt(value));
                } else if (info.equals(ZXMInfo.Title) || info.equals(ZXMInfo.TitleUnicode)) {//同步标题
                    String titleStr = zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode);
                    if ("".equals(titleStr) || titleStr == null)
                        titleStr = zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title);
                    tab.setText(titleStr);
                }

            });


            {
                Tab tab1 = new Tab("常用");

                InfoPane infoPane = new InfoPane(zxMap.unLocalizedMapInfo, new ZXMInfo[]{ZXMInfo.AudioPath, ZXMInfo.BgPath, ZXMInfo.Title, ZXMInfo.TitleUnicode, ZXMInfo.Artist, ZXMInfo.ArtistUnicode, ZXMInfo.Version, ZXMInfo.MapCreator, ZXMInfo.ObjectCount, ZXMInfo.TimingCount,});
                ScrollPane scrollPane = new ScrollPane(infoPane);
                scrollPane.setFitToWidth(true);
                tab1.setContent(scrollPane);

                tabPane.getTabs().addAll(tab1);

            }
            {
                Tab tab1 = new Tab("OSU");
                ArrayList<ZXMInfo> zxmInfos = new ArrayList<>();
                for (OsuInfo info : OsuInfo.values()) {
                    zxmInfos.add(info.unLocalize());
                }
                ZXMInfo[] infos = new ZXMInfo[zxmInfos.size()];
                zxmInfos.toArray(infos);
                InfoPane infoPane = new InfoPane(zxMap.unLocalizedMapInfo, infos);
                ScrollPane scrollPane = new ScrollPane(infoPane);
                scrollPane.setFitToWidth(true);
                tab1.setContent(scrollPane);
                tabPane.getTabs().addAll(tab1);
            }
            {
                Tab tab1 = new Tab("IMD");
                ArrayList<ZXMInfo> zxmInfos = new ArrayList<>();
                for (ImdInfo info : ImdInfo.values()) {
                    zxmInfos.add(info.unLocalize());
                }
                ZXMInfo[] infos = new ZXMInfo[zxmInfos.size()];
                zxmInfos.toArray(infos);
                InfoPane infoPane = new InfoPane(zxMap.unLocalizedMapInfo, infos);
                ScrollPane scrollPane = new ScrollPane(infoPane);
                scrollPane.setFitToWidth(true);
                tab1.setContent(scrollPane);
                tabPane.getTabs().addAll(tab1);
            }

            {
                Tab tab1 = new Tab("全部");
                InfoPane infoPane = new InfoPane(zxMap.unLocalizedMapInfo, ZXMInfo.values());
                ScrollPane scrollPane = new ScrollPane(infoPane);
                scrollPane.setFitToWidth(true);
                tab1.setContent(scrollPane);
                tabPane.getTabs().addAll(tab1);
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
            scrollBar.maxProperty().bind(mapTimeLength);

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
            /*ToggleGroup toggleGroup = new ToggleGroup();
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
            toggleButton.setToggleGroup(toggleGroup);*/
            }


            {//状态
                //跳转开头
                {
                    Button button = sideToolBar.addButton("state", "svg.icons.editor.align-top", "跳转末尾");
                    button.setOnAction((event -> {
                        if (zxMap.notes.isEmpty()) return;
                        mainMapRender.getInfo().timelinePosition.setValue(zxMap.notes.getLast().timeStamp);
                    }));
                }


                //播放
                {
                    Button button = sideToolBar.addButton("state", "svg.icons.media.play-line", "播放");
                    button.setOnAction(event -> {
                        if (audioChannel != null) {
                            audioChannel.play();
                        }
                    });
                }


                //暂停
                {
                    Button button = sideToolBar.addButton("state", "svg.icons.media.pause-line", "暂停");
                    button.setOnAction(event -> {
                        if (audioChannel != null) audioChannel.pause();
                    });
                }
                //跳转末尾
                {
                    Button button = sideToolBar.addButton("state", "svg.icons.editor.align-bottom", "跳转开头");
                    button.setOnAction((event -> {
                        if (zxMap.notes.isEmpty()) return;
                        mainMapRender.getInfo().timelinePosition.setValue(zxMap.notes.getFirst().timeStamp);
                    }));
                }
            }

            {//工具
                //吸附编辑
                //sideToolBar.addToggleButton("tool", "svg.icons.design.pencil-ruler-2-line", "吸附");

                ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
                    if ((audioChannel == null || audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) && zxMap.timingPoints.size() > 0)
                        mainMapRender.getInfo().timelinePosition.set(RenderBeat.alignBeatsTime(renderBeats, newValue.longValue()));
                    //zxMap.unLocalizedMapInfo.addInfo(ZXMInfo.TimelineZoom,);//上次打开的位置
                };

                { //判定线对齐
                    ToggleButton toggleButton = sideToolBar.addToggleButton("tool", "svg.icons.zxnoter.judged-line-align", "判定线对齐");
                    if (!zxMap.notes.isEmpty()) {
                        mainMapRender.getInfo().timelinePosition.set(RenderBeat.alignBeatsTime(renderBeats, 0));
                        judgeLineAlign = true;
                        mainMapRender.getInfo().timelinePosition.addListener(changeListener);
                        toggleButton.setSelected(true);
                    }
                    //mainMapRender.getInfo().timelinePosition.addListener(changeListener);
                    toggleButton.setOnAction(event -> {
                        judgeLineAlign = toggleButton.isSelected();
                        if (judgeLineAlign) {
                            mainMapRender.getInfo().timelinePosition.addListener(changeListener);
                        } else {
                            mainMapRender.getInfo().timelinePosition.removeListener(changeListener);
                        }
                    });
                }


            }


            {//工具
                { //反转
                    isReverse = sideToolBar.addToggleButton("tool", "svg.icons.arrows.arrow-up-down-line", "滚动反转");
                }
            }


            CanvasPane infoCanvasPane = new CanvasPane();
            infoCanvasPane.getStyleClass().add("info-canvas-pane");
            infoCanvasPane.setMinWidth(200);
            infoCanvasPane.setPrefWidth(260);
            FixedOrbitRenderInfo fixedOrbitRenderInfo = new FixedOrbitRenderInfo(infoCanvasPane.canvas);
            fixedOrbitRenderInfo.timelinePosition.bind(mainMapRender.getInfo().timelinePosition);
            fixedOrbitRenderInfo.judgedLinePositionPercentage.bind(mainMapRender.getInfo().judgedLinePositionPercentage);
            fixedOrbitRenderInfo.timelineZoom.bind(mainMapRender.getInfo().timelineZoom);
            infoRender = new FixedOrbitInfoRender(fixedOrbitRenderInfo, zxMap, infoCanvasPane.canvas, "default", renderBeats, zxMap);


            HBox.setMargin(infoCanvasPane, new Insets(0, 26, 0, 0));


            infoCanvasPane.setOnMouseMoved(event -> {
                infoRender.point.setXY(event.getX(), event.getY());
            });
            infoCanvasPane.setOnMousePressed(event -> {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (infoRender.selectTiming != null) {
                        zxMap.timingPoints.remove(infoRender.selectTiming);
                        zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.TimingCount, String.valueOf(zxMap.timingPoints.size()));
                        upDateBeats();
                    }
                }
            });


            //主体
            HBox bodyPane = new HBox(sideToolBar, infoCanvasPane, mapCanvas, previewBar, scrollBar, tabPane);
            VBox.setVgrow(bodyPane, Priority.ALWAYS);

            //顶部工具组栏
            HToolGroupBar topToolBar = new HToolGroupBar();


            {//文件
                Button button;
                {//保存文件
                    button = topToolBar.addButton("file", "svg.icons.device.save-line", "保存为");
                    button.setOnAction(event -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("导出谱面到");
                        if (mapResourcePath != null) fileChooser.setInitialDirectory(mapResourcePath.toFile());
                        fileChooser.setInitialFileName(zxMap.unLocalizedMapInfo.getInfo(OsuInfo.Title.unLocalize()));

                        // 添加文件过滤器
                        FileChooser.ExtensionFilter zxnFilter = new FileChooser.ExtensionFilter("zxn谱面", "*.zxn");
                        FileChooser.ExtensionFilter osuFilter = new FileChooser.ExtensionFilter("osu谱面", "*.osu");
                        FileChooser.ExtensionFilter imdFilter = new FileChooser.ExtensionFilter("节奏大师谱面", "*.imd");
                        FileChooser.ExtensionFilter mcFilter = new FileChooser.ExtensionFilter("Malody谱面", "*.mc");

                        fileChooser.getExtensionFilters().addAll(zxnFilter, osuFilter, imdFilter,mcFilter);
                        File file = fileChooser.showSaveDialog(this.getScene().getWindow());
                        if (file != null) {
                            try {
                                if (file.getName().endsWith(".zxn")) zxMap.saveZXN(file.toPath());
                                if (file.getName().endsWith(".osu")) new OsuWriter().writeOut(zxMap, file.toPath());
                                if (file.getName().endsWith(".imd")) new ImdWriter().writeOut(zxMap, file.toPath());
                                if (file.getName().endsWith(".mc")) new McWriter().writeOut(zxMap, file.toPath());
                            } catch (NoSuchFieldException | IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }


                {//导入音频
                    button = topToolBar.addButton("file", "svg.icons.document.file-music-line", "导入音频");
                    button.setOnAction(event -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("导入谱面音乐文件");
                        // 添加文件过滤器
                        FileChooser.ExtensionFilter audioFilter = new FileChooser.ExtensionFilter("音频文件", "*.mp3", "*.wav", "*.flac", "*.mp4", "*.ogg");
                        fileChooser.getExtensionFilters().addAll(audioFilter);

                        File audioFile = fileChooser.showOpenDialog(this.getScene().getWindow());


                        setNewAudioPath(audioFile.toPath());


                    });
                }

            }


            {//时间
                Button button;
                //单位切换
                button = topToolBar.addButton("time", "svg.icons.zxnoter.time-format-line", "格式化时间/总毫秒时间");
                button.setOnAction(event -> {
                    timelineIsFormat.set(!timelineIsFormat.get());
                    infoRender.timeIsFormat = timelineIsFormat.get();
                    if (timelineIsFormat.get()) {
                        button.setShape(ZXResources.getSvg("svg.icons.zxnoter.time-format-line"));
                    } else {
                        button.setShape(ZXResources.getSvg("svg.icons.zxnoter.time-ms-line"));
                    }
                });
                TextField textField = new TextField("00.000");
                textField.setPrefWidth(80);
                textField.setAlignment(Pos.CENTER);
                textField.setOnMouseReleased(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY))
                        if (event.getPickResult().getIntersectedNode() instanceof Pane) textField.selectAll();
                });
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        mainMapRender.getInfo().timelinePosition.set((timelineIsFormat.get() ? TimeUtils.parseTime(textField.getText()) : Long.parseLong(textField.getText().replaceAll("ms", "").replaceAll(" ", ""))));
                    }
                });

                textField.setOnMouseExited(event -> {
                    mapCanvas.requestFocus();
                });
                textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        mapCanvas.requestFocus();
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


            {//时间
                TextField textField = new TextField("0");
                textField.setPrefWidth(40);
                textField.setAlignment(Pos.CENTER);
                textField.setPromptText("ms");
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        textField.selectAll();
                    } else {
                        textField.getParent().requestFocus();
                        audioTimeOffset.set(Long.parseLong(textField.getText()));
                    }
                });
                textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        textField.getParent().requestFocus();
                        audioTimeOffset.set(-Long.parseLong(textField.getText()));
                        event.consume();
                    }
                });

                topToolBar.addControl("time", textField, "音频偏移(ms)");
            }


            {//状态


                {//播放变速
                    Button button = topToolBar.addButton("state", "svg.icons.media.slow-down-line", "播放变速");
                    button.setOnScroll(event -> {
                        if (audioChannel != null) {
                            double speed = audioChannel.getSpeed();
                            double change = (event.isControlDown() ? 0.1 : 0.01);
                            if (event.getDeltaY() > 0) speed += change;
                            else speed -= change;
                            speed = Math.max(speed, 0.1);
                            speed = Math.min(speed, 2);
                            audioChannel.setSpeed(speed);
                            button.getTooltip().setText("播放变速" + Math.round(speed * 100) + "%");
                        }
                    });
                }

                {//音量
                    Button button = topToolBar.addButton("state", "svg.icons.media.volume-down-line", "音乐音量调节");
                    button.setOnScroll(event -> {
                        if (audioChannel != null) {
                            float change = (event.isControlDown() ? 0.05f : 0.01f);
                            float volume = audioChannel.getVolume() * 2;
                            if (event.getDeltaY() > 0) volume += change;
                            else volume -= change;

                            volume = Math.min(volume, 1);
                            volume = Math.max(volume, 0);
                            audioChannel.setVolume(volume / 2);
                            button.getTooltip().setText("音乐音量调节" + Math.round(volume * 100) + "%");
                            if (volume == 0) button.setShape(ZXResources.getSvg("svg.icons.media.volume-mute-line"));
                            else if (volume <= 0.5)
                                button.setShape(ZXResources.getSvg("svg.icons.media.volume-down-line"));
                            else if (volume <= 1) button.setShape(ZXResources.getSvg("svg.icons.media.volume-up-line"));
                        }
                    });
                }

                {//音量
                    Button button = topToolBar.addButton("state", "svg.icons.media.volume-down-line", "key音音量调节");
                    button.setOnScroll(event -> {
                        if (audioChannel != null) {
                            float change = (event.isControlDown() ? 0.05f : 0.01f);
                            float volume = hitKeyVolume * 2;
                            if (event.getDeltaY() > 0) volume += change;
                            else volume -= change;
                            volume = Math.min(volume, 1);
                            volume = Math.max(volume, 0);
                            hitKeyVolume = volume / 2;
                            button.getTooltip().setText("key音音量调节" + Math.round(volume * 100) + "%");
                            if (volume == 0) button.setShape(ZXResources.getSvg("svg.icons.media.volume-mute-line"));
                            else if (volume <= 0.5)
                                button.setShape(ZXResources.getSvg("svg.icons.media.volume-down-line"));
                            else if (volume <= 1) button.setShape(ZXResources.getSvg("svg.icons.media.volume-up-line"));
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
                    Button button = topToolBar.addButton("tool", "svg.icons.zxnoter.4k", "轨道数");
                    button.getTooltip().setText("轨道数" + mainMapRender.getInfo().orbits.get() + "k");
                    button.setShape(ZXResources.getSvg("svg.icons.zxnoter." + mainMapRender.getInfo().orbits.get() + "k"));
                    mainMapRender.getInfo().orbits.addListener((observable, oldValue, newValue) -> {
                        button.getTooltip().setText("轨道数" + newValue + "k");
                        button.setShape(ZXResources.getSvg("svg.icons.zxnoter." + newValue + "k"));
                    });
                    button.setOnScroll(event -> {
                        int orbits = mainMapRender.getInfo().orbits.get();
                        if (event.getDeltaY() > 0) orbits++;
                        else orbits--;
                        zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.KeyCount, String.valueOf(orbits));
                    });
                }


                {
                    //Timing点
                    Button button = topToolBar.addButton("tool", "svg.icons.map.map-pin-2-line", "添加Timing点");
                    Tooltip tooltip = new Tooltip();
                    TextField textField = new TextField();
                    textField.setPrefWidth(40);
                    textField.setPromptText("bpm值");
                    textField.setFocusTraversable(false);
                    CheckBox isBase = new CheckBox("基准");
                    isBase.setSelected(true);
                    Button done = new Button();
                    done.setShape(ZXResources.getSvg("svg.icons.map.map-pin-add-line"));
                    done.setPrefSize(22, 22);
                    done.setOnAction(event -> {
                        double bpm = Double.parseDouble(textField.getText());
                        long time = mainMapRender.getInfo().timelinePosition.get();


                        ZXTiming timing = new ZXTiming(time, bpm, isBase.isSelected(), bpm, 4, 0, 0, 75, isBase.isSelected(), 0);


                        ArrayList<Timing> timings = zxMap.findClosestTimings(time);
                        if (timings.size() > 0) {
                            timing.absBpm = timings.get(0).absBpm;
                            if (timings.get(0).timestamp > time) {
                                int index = zxMap.timingPoints.indexOf(timings.get(0)) - 1;
                                if (index >= 0) timing.absBpm = zxMap.timingPoints.get(index).absBpm;
                            }
                            if (isBase.isSelected()) timing.absBpm = bpm;
                        }


                        zxMap.timingPoints.add(timing);
                        zxMap.timingPoints.sort(Timing::compareTo);
                        System.out.println(zxMap.timingPoints);
                        zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.TimingCount, String.valueOf(zxMap.timingPoints.size()));
                        upDateBeats();
                        if (zxMap.timingPoints.size() == 2) {
                            globalSubbeatButton.getOnScroll().handle(null);
                            getMapTimeLength();
                            //mainMapRender.getInfo().noteLastTime.set(getMapTimeLength());
                        }
                    });


                    HBox hBox = new HBox(isBase, done);
                    hBox.setAlignment(Pos.CENTER);
                    hBox.setSpacing(12);
                    Label title = new Label("添加Timing点");
                    title.setFont(new Font(12));
                    VBox vBox = new VBox(title, textField, hBox);
                    vBox.setSpacing(4);
                    vBox.setAlignment(Pos.CENTER);
                    vBox.setPrefHeight(66);
                    vBox.setOnMouseExited(event1 -> {
                        tooltip.hide();
                    });

                    tooltip.setGraphic(vBox);
                    tooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
                    button.setOnMouseClicked(event -> {
                        tooltip.show(button, event.getScreenX(), event.getScreenY());
                    });
                }


                {


                    //全局分拍数
                    globalSubbeatButton = new Button("1/1");
                    topToolBar.addNode("tool", globalSubbeatButton);

                    globalSubbeatButton.setOnAction(event -> upDateBeats());
                    globalSubbeatButton.setOnScroll(event -> {

                        if (event == null) {
                            globalSubbeatButton.setText("1/" + globalMeasure);
                            for (RenderBeat beat : renderBeats) {
                                beat.measure = globalMeasure;
                            }
                            return;
                        }

                        if (event.getDeltaY() > 0) globalMeasure++;
                        else globalMeasure--;
                        globalMeasure = Math.max(globalMeasure, 1);

                        globalSubbeatButton.setText("1/" + globalMeasure);
                        for (RenderBeat beat : renderBeats) {
                            long time = Math.round(60000 / beat.timing.absBpm);
                            if (zxMap.getScaleNotes(beat.time + 2, time - 4, true).isEmpty())
                                beat.measure = globalMeasure;
                        }
                    });

                }


            }

/*
        {//显示工具
            //显示测量
            topToolBar.addToggleButton("tool", "svg.icons.design.ruler-line", "显示时间");
        }*/


            {//布局
                //左侧扩展
                //topToolBar.addToggleButton("layout", "svg.icons.design.layout-left-line", "扩展");
                //滚动栏
                //topToolBar.addToggleButton("layout", "svg.icons.design.layout-right-2-line", "滚动栏");
                //右侧属性布局
                //topToolBar.addToggleButton("layout", "svg.icons.design.layout-right-line", "属性栏");
            }


            setOnDragOver(event -> {
                if (event.getDragboard().hasFiles()) {
                    // 允许拖放
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            setOnDragDropped(event -> {

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    // 获取拖放的文件列表
                    File audioFile = db.getFiles().get(0);
                    setNewAudioPath(audioFile.toPath());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();


            });


            mainMapRender.getInfo().timelinePosition.addListener((observable, oldValue, newValue) -> {
                if (audioChannel != null) if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) {
                    hitsNotes.clear();


                    ArrayList<BaseNote> findsNotes = zxMap.getScaleNotes(mainMapRender.getInfo().timelinePosition.get() - 1000, 1000, true);
                    //解决重复key音播放
                    for (var note : findsNotes) {
                        if (note.timeStamp < mainMapRender.getInfo().timelinePosition.get()) {
                            hitsNotes.add(note);
                        }
                    }

                    audioChannel.setTime(mainMapRender.getInfo().timelinePosition.get());
                }
            });


            //添加给自己
            this.getChildren().addAll(topToolBar, bodyPane);

            try {
                updateMusic();
            } catch (Exception e) {
            }

            upDateBeats();
            mainMapRender.getInfo().timelinePosition.set(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setNewAudioPath(Path audioPath) {
        if (audioPath != null) {
            HashMap<String, String> metadata = FFmpeg.readMetadata(audioPath);

            String title = metadata.get(FFmpeg.AudioMetadataKey.TITLE.getKey());
            if (title == null) title = "";
            if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode)) || zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode) == null)
                zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.TitleUnicode, title);

            if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title)) || zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title) == null)
                if (Pattern.matches("\\A\\p{ASCII}*\\z", title)) zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.Title, title);

            if ("".equals(title)) {
                title = audioPath.getFileName().toString();

                if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.TitleUnicode)))
                    zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.TitleUnicode, title);

                if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Title)))
                    if (Pattern.matches("\\A\\p{ASCII}*\\z", title))
                        zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.Title, title);
            }


            String artist = metadata.get(FFmpeg.AudioMetadataKey.ARTIST.getKey());
            if (artist == null) artist = "";

            if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.ArtistUnicode)) || zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.ArtistUnicode) == null)
                zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.ArtistUnicode, artist);

            if ("".equals(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Artist)) || zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.Artist) == null)
                if (Pattern.matches("\\A\\p{ASCII}*\\z", artist))
                    zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.Artist, artist);
            mapResourcePath = audioPath.getParent();
            zxMap.unLocalizedMapInfo.setInfo(ZXMInfo.AudioPath, audioPath.getFileName().toString());
            updateMusic();
            upDateBeats();
        }

    }

    public void updateMusic() {

        if (audioChannel != null) {
            ZXNApp.audioMixer.removeChannel(audioChannel);
        }

        {//初始化音频
            try {

                Path audioPath = mapResourcePath.resolve(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.AudioPath));
                if (Files.exists(audioPath)) {
                    /*Path workAudioPath;
                    if (audioPath.getFileName().toString().toLowerCase().endsWith(".wav")) {
                        workAudioPath = audioPath;
                    } else {
                        workAudioPath = audioPath.getParent().resolve(audioPath.getFileName() + ".wav");

                        if (!FFmpeg.audioToWav(audioPath, workAudioPath))
                            throw new RuntimeException("音频转换失败");
                    }*/


                    //格式转换
                    //System.out.println("格式转化");

                    var id = ZXNApp.audioMixer.loadAudio(audioPath);
                    audioChannel = ZXNApp.audioMixer.createChannel(id);
                    audioChannel.setVolume(0.10f);
                    audioChannel.setEndBehavior(AudioChannel.EndBehavior.PAUSE);

                    //更新音频长度
                    audioTimeLength = audioChannel.getDuration();
                    getMapTimeLength();
                    //audioChannel.setPlaySpeed(false, 0.8f);




                    /*ZXNApp.threadAudioMixer.addAudioChannel();*/
                    /*if (FFmpeg.audioFormatConversion(audioPath, workAudioPath) != 0)
                        throw new RuntimeException("音频转换失败");
                    int id = ZXNApp.audioMixer.addAudio(workAudioPath);
                    audioChannel = ZXNApp.audioMixer.createChannel(id);
                    audioChannel.setVolume(0.15f);*/

                    hitAudioID = ZXNApp.audioMixer.loadAudio(ZXResources.getPath("audio.drum-hitfinish"));

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
                //System.out.println("初始化播放通道失败");
                throw new RuntimeException(e);
            }


        }
    }

    public long getMapTimeLength() {
        mapTimeLength.set(Math.max(RenderBeat.getZXMapLastTime(zxMap), audioTimeLength));
        return mapTimeLength.get();
    }

    public void upDateBeats() {
        // 清空之前的节拍数据
        renderBeats.clear();

        // 用于保存上一个基准时间点
        Timing previousBaseTiming = null;

        // 创建一个包含所有时间点的列表，并在末尾添加一个时间点，表示时间轴的结束
        ArrayList<Timing> timingPoints = new ArrayList<>(zxMap.timingPoints);
        timingPoints.add(new Timing(getMapTimeLength(), 0, true, 0));

        // 遍历所有的时间点
        for (Timing timing : timingPoints) {
            // 如果当前时间点不是新的基准BPM，跳过它
            if (!timing.isNewBaseBpm) continue;

            // 如果存在之前的基准时间点，处理当前和之前基准之间的节拍
            if (previousBaseTiming != null) {
                processTiming(previousBaseTiming, timing);
            }

            // 更新之前的基准时间点为当前的基准时间点
            previousBaseTiming = timing;
        }
    }

    /**
     * 处理两个基准时间点之间的节拍。
     *
     * @param previousBaseTiming 上一个基准时间点
     * @param nowBaseTiming      当前基准时间点
     */
    private void processTiming(Timing previousBaseTiming, Timing nowBaseTiming) {
        // 计算一拍的时间周期 (毫秒)
        double beatCycleTime = 60000.0 / previousBaseTiming.absBpm;

        // 计算在这两个时间点之间的节拍数量
        int beatCount = (int) ((nowBaseTiming.timestamp - previousBaseTiming.timestamp) / beatCycleTime);

        // 生成并添加每个节拍
        for (int i = 0; i < beatCount + 1; i++) {
            long time = previousBaseTiming.timestamp + (long) (i * beatCycleTime);
            RenderBeat renderBeat = new RenderBeat(time, previousBaseTiming, i == 0);

            // 设置这个节拍的测量值
            setRenderBeatMeasure(renderBeat, time, beatCycleTime);

            // 将生成的节拍添加到渲染节拍列表中
            renderBeats.add(renderBeat);
        }
    }

    /**
     * 为渲染节拍设置测量值。
     *
     * @param renderBeat    渲染节拍对象
     * @param time          当前节拍的时间戳
     * @param beatCycleTime 一拍的时间周期
     */
    private void setRenderBeatMeasure(RenderBeat renderBeat, long time, double beatCycleTime) {
        // 获取当前节拍时间段内的音符
        ArrayList<BaseNote> notes = zxMap.getScaleNotes(time + 2, Math.round(beatCycleTime) - 4, true);

        // 获取这些音符的关键点（音符的头部和尾部）
        ArrayList<Long> keyPoints = RenderBeat.keyPoint(notes, true);

        keyPoints.add(time);
        keyPoints.add(time + Math.round(beatCycleTime));

        // 尝试找到最合适的测量值
        renderBeat.measure = findBestMeasure(keyPoints, time, beatCycleTime);

        // 如果没有找到合适的测量值，仅使用键头重新计算
        if (renderBeat.measure == -1) {
            keyPoints = RenderBeat.keyPoint(notes, false);
            renderBeat.measure = findBestMeasure(keyPoints, time, beatCycleTime);
        }

        // 如果仍未找到合适的测量值，使用全局测量值
        renderBeat.measure = (renderBeat.measure == -1) ? globalMeasure : Math.max(renderBeat.measure, 1);
    }

    /**
     * 尝试找到最佳测量值。
     *
     * @param keyPoints     当前节拍的关键点列表
     * @param time          当前节拍的时间戳
     * @param beatCycleTime 一拍的时间周期
     * @return 最佳测量值，如果没有找到则返回 -1
     */
    private int findBestMeasure(ArrayList<Long> keyPoints, long time, double beatCycleTime) {
        // 尝试不同的测量值，范围从 1 到 48
        for (int measure = 1; measure < 49; measure++) {
            boolean isMeasureValid = true;

            // 检查每个关键点是否符合当前测量值
            for (long note : keyPoints) {
                if ((note - time + 2) % (beatCycleTime / measure) > 4) {
                    isMeasureValid = false;
                    break;
                }
            }

            // 如果找到一个有效的测量值，返回该值
            if (isMeasureValid) {
                return measure;
            }
        }

        // 如果没有找到合适的测量值，返回 -1
        return -1;
    }


    @Override
    public void render() {

        //if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PLAY))
        //mainMapRender.getInfo().timelinePosition.set(System.currentTimeMillis() - synchroniseTime);

        /*if (audioChannel.getPlayState().equals(AudioChannel.PlayState.PLAY))
            mainMapRender.getInfo().timelinePosition.set(audioChannel.getTime());*/

        if (audioChannel != null) {
            if (!audioChannel.getPlayState().equals(AudioChannel.PlayState.PAUSE)) {
                //根据音频播放时间同步时间线
                mainMapRender.getInfo().timelinePosition.set((long) (audioChannel.getTime() + audioTimeOffset.get()));

                if (zxMap.notes.isEmpty()) return;
                ArrayList<BaseNote> findsNotes = zxMap.getScaleNotes(mainMapRender.getInfo().timelinePosition.get() - 1000, 1000, true);
                Set<BaseNote> allNotes = new HashSet<>(findsNotes);
                //将组合键子键加入
                for (BaseNote note : findsNotes)
                    if (note instanceof ComplexNote complexNote) allNotes.addAll(complexNote.notes);

                //查找所有键
                for (BaseNote note : allNotes) {
                    if (!hitsNotes.contains(note)) {
                        hitsNotes.add(note);
                        if (System.currentTimeMillis() - hitTime > 2) {
                            int count = 0;
                            for (BaseNote sameNote : allNotes) {
                                if (Math.abs(note.timeStamp - sameNote.timeStamp) < 5) count++;
                            }

                            playKeyAudio(count);
                            hitTime = System.currentTimeMillis();
                        }
                    }
                }
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

        //mainMapRender.graphics.rotate(1);
        backgroundRender.render();
        beatLineRender.render();
        mainMapRender.render();
        mainShadowMapRender.render();
        timingRender.render();

        infoRender.clearRect();
        infoRender.render();

        //计算
        previewBackgroundRender.mainJudgedLinePositionPercentage.setValue(mainMapRender.getInfo().timelinePosition.get());

        previewMapRender.getInfo().timelinePosition.setValue(mainMapRender.getInfo().getPositionToTime(mainMapRender.getHeight() / 2));

    }

    private void playKeyAudio(int count) {
        AudioChannel audioChannel1;
        audioChannel1 = ZXNApp.audioMixer.createChannel(hitAudioID);
        audioChannel1.setEndBehavior(AudioChannel.EndBehavior.CLOSE);
        audioChannel1.setVolume(Math.min(hitKeyVolume + count * 0.2f * hitKeyVolume, 1f));
        audioChannel1.play();
    }

    @Override
    public boolean close() {
        audioChannel.close();
        return false;
    }
}
