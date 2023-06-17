package team.zxorg.zxnoter.ui.render;

import javafx.beans.property.*;

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

    /**
     * 最后的键时间戳
     */
    public LongProperty noteLastTime = new SimpleLongProperty();
}
