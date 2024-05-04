package team.zxorg.skin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式计算器
 */
public class ExpressionCalculator {

    //    private final ArrayList<Consumer<Orientation>> canvasSizeChangeEvent = new ArrayList<>();
    /**
     * 画布宽度
     */
    private double canvasWidth;
    /**
     * 画布高度
     */
    private double canvasHeight;
    /**
     * 像素倍率
     */
    private double pixelMagnification;
    /**
     * 单位画布高度
     */
    private double unitCanvasHeight;

    /**
     * 获取像素倍率
     */
    public double getPixelMagnification() {
        return pixelMagnification;
    }

    public ExpressionCalculator() {
        this(0, 0, 720);
    }


    public ExpressionCalculator(double canvasWidth, double canvasHeight, double unitCanvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.unitCanvasHeight = unitCanvasHeight;
        pixelMagnification = canvasHeight / unitCanvasHeight;
    }

    public void setCanvasSize(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        pixelMagnification = canvasHeight / unitCanvasHeight;
    }

    public void setUnitCanvasHeight(double unitCanvasHeight) {
        this.unitCanvasHeight = unitCanvasHeight;
        pixelMagnification = canvasHeight / unitCanvasHeight;
    }

    /*public double getCanvasWidth() {
        return canvasWidth;
    }

    public double getCanvasHeight() {
        return canvasHeight;
    }*/

   /* public void addCanvasSizeChangeEvent(Consumer<Orientation> recalculate) {
        canvasSizeChangeEvent.add(recalculate);
    }

    public void updateCanvasWidth(double width) {
        canvasWidth = width;
//        widthRelativeMagnification = width / CanvasConstants.DEFAULT_CANVAS_WIDTH;
        for (Consumer<Orientation> consumer : canvasSizeChangeEvent) {
            consumer.accept(Orientation.HORIZONTAL);
        }
    }

    public void updateCanvasHeight(double height) {
        canvasHeight = height;
        heightRelativeMagnification = height / defaultCanvasHeight;
        for (Consumer<Orientation> consumer : canvasSizeChangeEvent) {
            consumer.accept(Orientation.VERTICAL);
        }
    }

    public void updateCanvasSize(double width, double height) {
        updateCanvasWidth(width);
        updateCanvasHeight(height);
    }*/

    /**
     * 计算X坐标
     *
     * @param expression 表达式
     * @return 值
     */
    public double calculateX(String expression) {
        return calculateValue(expression, canvasWidth);
    }

    /**
     * 计算Y坐标
     *
     * @param expression 表达式
     * @return 值
     */
    public double calculateY(String expression) {
        return canvasHeight - calculateValue(expression, canvasHeight);
    }

    /**
     * 计算宽度
     *
     * @param expression 表达式
     * @return 值
     */
    public double calculateW(String expression) {
        return calculateValue(expression, canvasWidth);
    }

    /**
     * 计算高度
     *
     * @param expression 表达式
     * @return 值
     */
    public double calculateH(String expression) {
        return calculateValue(expression, canvasHeight);
    }

    /**
     * 计算绝对像素值
     *
     * @param expression 表达式
     * @param maxPixel   最大像素值
     * @return 值
     */
    private double calculateValue(String expression, double maxPixel) {
        Pattern pattern = Pattern.compile("([+-]?\\d*\\.?\\d+)(%|px)?");
        Matcher matcher = pattern.matcher(expression);

        double result = 0;

        while (matcher.find()) {
            String valueStr = matcher.group(1);
            String unit = matcher.group(2);

            double value = Double.parseDouble(valueStr);

            if ("%".equals(unit)) {
                // 百分比
                result += value / 100.0 * maxPixel;
            } else if ("px".equals(unit)) {
                // 像素
                result += value;
            } else {
                // 相对尺寸
                result += value * pixelMagnification;
            }
        }

        return result;
    }

    /*public void setDefaultCanvasHeight(double defaultCanvasHeight) {
        ExpressionCalculator.defaultCanvasHeight = defaultCanvasHeight;
        for (Consumer<Orientation> consumer : canvasSizeChangeEvent) {
            consumer.accept(Orientation.VERTICAL);
            consumer.accept(Orientation.HORIZONTAL);
        }
    }*/


}
