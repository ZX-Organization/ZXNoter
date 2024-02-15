package team.zxorg.skin.test;

import javafx.geometry.Orientation;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionCalculator {

    private final ArrayList<Consumer<Orientation>> canvasSizeChangeEvent = new ArrayList<>();
    private double canvasWidth;
    private double canvasHeight;
    private double widthRelativeMagnification;
    private double heightRelativeMagnification;

    public ExpressionCalculator() {
        this(0, 0);
    }

    public ExpressionCalculator(double canvasWidth, double canvasHeight) {
        updateCanvasSize(canvasWidth, canvasHeight);
    }

    public void addCanvasSizeChangeEvent(Consumer<Orientation> recalculate) {
        canvasSizeChangeEvent.add(recalculate);
    }

    public void updateCanvasWidth(double width) {
        canvasWidth = width;
        widthRelativeMagnification = width / CanvasConstants.DEFAULT_CANVAS_WIDTH;
        for (Consumer<Orientation> consumer : canvasSizeChangeEvent) {
            consumer.accept(Orientation.HORIZONTAL);
        }
    }

    public void updateCanvasHeight(double height) {
        canvasHeight = height;
        heightRelativeMagnification = height / CanvasConstants.DEFAULT_CANVAS_HEIGHT;
        for (Consumer<Orientation> consumer : canvasSizeChangeEvent) {
            consumer.accept(Orientation.VERTICAL);
        }
    }

    public void updateCanvasSize(double width, double height) {
        updateCanvasWidth(width);
        updateCanvasHeight(height);
    }

    public double calculateX(String expression) {
        return calculateValue(expression, widthRelativeMagnification, canvasWidth);
    }

    public double calculateY(String expression) {
        return calculateValue(expression, heightRelativeMagnification, canvasHeight);
    }

    private double calculateValue(String expression, double relativeMagnification, double maxPixel) {
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
                result += value * relativeMagnification;
            }
        }

        return result;
    }

    private static class CanvasConstants {
        static final double DEFAULT_CANVAS_WIDTH = 1280.0;
        static final double DEFAULT_CANVAS_HEIGHT = 720.0;
    }
}
