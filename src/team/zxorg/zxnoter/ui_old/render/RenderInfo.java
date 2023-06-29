package team.zxorg.zxnoter.ui_old.render;

import javafx.beans.property.*;
import javafx.scene.canvas.Canvas;

public class RenderInfo {
    /**
     * 时间线缩放 (一毫秒的像素距离)
     */
    public FloatProperty timelineZoom = new SimpleFloatProperty(10);
    /**
     * 时间线位置
     */
    public LongProperty timelinePosition = new SimpleLongProperty(0);
    /**
     * 所使用的主题
     */
    public StringProperty theme = new SimpleStringProperty("");
    /**
     * 画布宽度
     */
    public DoubleProperty canvasWidth = new SimpleDoubleProperty();
    /**
     * 画布高度
     */
    public DoubleProperty canvasHeight = new SimpleDoubleProperty();



    public IntegerProperty orbits=new SimpleIntegerProperty();

    public RenderInfo(Canvas canvas) {
        canvasWidth.bind(canvas.widthProperty());
        canvasHeight.bind(canvas.heightProperty());
    }
}
