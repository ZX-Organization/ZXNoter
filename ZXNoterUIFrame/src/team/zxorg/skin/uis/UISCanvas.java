package team.zxorg.skin.uis;

import javafx.scene.canvas.GraphicsContext;
import team.zxorg.skin.DeviceType;
import team.zxorg.skin.uis.component.AbstractComponentRenderer;
import team.zxorg.skin.uis.component.AnimationComponentRenderer;
import team.zxorg.skin.uis.component.MeasuringRulerRenderer;
import team.zxorg.skin.uis.ui.Perspective;
import team.zxorg.ui.component.LayerCanvasPane;
import team.zxorg.zxncore.ZXLogger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class UISCanvas extends LayerCanvasPane {
    UISSkin skin;
    Perspective perspective = new Perspective();
    ExpressionCalculator expressionCalculator = new ExpressionCalculator();

    public MeasuringRulerRenderer measureRuler = new MeasuringRulerRenderer(expressionCalculator);

    ArrayList<AbstractComponentRenderer> componentRenders = new ArrayList<>();
    HashMap<UISComponent, AbstractComponentRenderer> componentMap = new HashMap<>();
    //开始播放时的时间戳
    public long startTime = 0;
    //暂停时的时间位置
    public long pauseTime = 0;
    //暂停状态
    public boolean isPaused = false;

    DeviceType deviceType;
    double aspectRatio = 1;
    double zoomRate = 1;

    public UISCanvas() {
        createCanvas("bottom");
        createCanvas("3d").setEffect(perspective.getEffect());
        createCanvas("top");
        measureRuler.initialize(createCanvas("mark"));
    }


    public void draw() {
        double width = expressionCalculator.getCanvasWidth();
        double height = expressionCalculator.getCanvasHeight();
        long currentTime = (isPaused ? pauseTime : System.currentTimeMillis() - startTime);
        /*if (!isPaused) {
            if (!timeLineSlider.isValueChanging())
                timeLineSlider.setValue(currentTime / 1000.);
        }
        timeLabel.setText(currentTime + " ms");*/

        clearRect();

        GraphicsContext gc3d = getGraphicsContext2D("3d");
        gc3d.save();
        gc3d.beginPath();
        gc3d.rect(-width, 0, width * 4, height);
        gc3d.clip();

        for (AbstractComponentRenderer cr : componentRenders) {
            GraphicsContext gc = getGraphicsContext2D(cr.getLayoutName());
            try {
                AbstractComponentRenderer anim = componentMap.get(cr.getMotion());
                gc.save();
                if (anim instanceof AnimationComponentRenderer renderer) {
                    renderer.update(gc, width, height, cr, currentTime);
                }
                cr.draw(gc, width, height, currentTime);
                gc.restore();
                gc.restore();
                gc.restore();
                gc.restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        {
            GraphicsContext gc = getGraphicsContext2D("mark");
            measureRuler.draw(gc, width, height, currentTime);
        }
        gc3d.restore();
        gc3d.restore();
        gc3d.restore();
        gc3d.restore();
    }


    public void updateSkin() {
        ZXLogger.info("更新皮肤");
        if (skin == null)
            return;

        /*updateCanvasSize();
        skin.setDeviceType(deviceTypeChoiceBox.getValue());*/
        //updateCanvasSize();


        skin.setDeviceType(deviceType);

        skin.updateRenderer(componentRenders, componentMap, this);
        UISSkin.sortRenders(componentRenders);

        perspective.setAngle(skin.getAngle());

        /*if (autoReplayCheckBox.isSelected() & skin.updateRenderer(componentRenders, componentMap, this))
            resetTime();*/

        {

            double unitHeight = skin.unit;
            //expressionCalculator.setUnitCanvasHeight(unitHeight * zoomRate);
            double width = unitHeight * aspectRatio * zoomRate;
            double height = unitHeight * zoomRate;
            setMinSize(width, height);
            setMaxSize(width, height);
            expressionCalculator.setCanvasSize(width, height);
            perspective.setSize(width, height);

        }


        for (AbstractComponentRenderer component : componentMap.values()) {
            component.reloadPos();
            component.reloadStyle();
        }

        resetTime();
    }

    public void setDeviceType(DeviceType deviceType) {
        ZXLogger.info("设置设备类型: " + deviceType);
        this.deviceType = deviceType;
    }

    public void loadSkin(Path uisPath) {
        ZXLogger.info("加载皮肤: " + uisPath);
        componentRenders.clear();
        componentMap.clear();

        skin = new UISSkin(uisPath, expressionCalculator);


        /*for (UISComponent component : skin.getComponents()) {
            AbstractComponentRenderer render = AbstractComponentRenderer.toRenderer(component, this);
            if (render != null) {
                componentRenders.add(render);
                componentMap.put(component, render);
            }

        }*/

        updateSkin();

        resetTime();
        //updateCanvasSize();
    }

    /**
     * 设置纵横比
     *
     * @param aspectRatio 纵横比
     */
    public void setAspectRatio(double aspectRatio) {
        ZXLogger.info("设置纵横比: " + aspectRatio);
        this.aspectRatio = aspectRatio;
        updateSkin();
    }

    /**
     * 设置缩放率
     *
     * @param zoomRate 缩放率
     */
    public void setZoomRate(double zoomRate) {
        ZXLogger.info("设置缩放率: " + zoomRate);
        this.zoomRate = zoomRate;
        //expressionCalculator.setUnitCanvasHeight(skin.unit * zoomRate);
        ZXLogger.info("设置缩放率后, 单位画布高度: " + expressionCalculator.getUnitCanvasHeight());
        updateSkin();
    }


    public void resetTime() {
        if (isPaused) {
            pauseTime = -3500;
        } else {
            startTime = System.currentTimeMillis() + 3500;
        }

    }
}
