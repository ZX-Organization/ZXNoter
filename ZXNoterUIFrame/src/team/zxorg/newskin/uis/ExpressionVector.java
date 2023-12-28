package team.zxorg.newskin.uis;

import team.zxorg.zxncore.ZXLogger;

public class ExpressionVector {
    public final ExpressionCalculator expressionCalculator;
    private String xExpression;
    private String yExpression;

    private double x = 0;
    private double y = 0;
    private double w = 0;
    private double h = 0;
    private int index = 0;

    public ExpressionVector(ExpressionCalculator expressionCalculator, String expression, int index) {
        this.expressionCalculator = expressionCalculator;
        this.index = index;
        String[] v = expression.split(",");
        if (v.length != 2)
            ZXLogger.warning("ExpressionVector: expression length is not 2");
        else {
            setX(v[0].trim());
            setY(v[1].trim());
        }
    }


    public ExpressionVector(ExpressionCalculator expressionCalculator, int index) {
        this(expressionCalculator, "", "", index);
    }

    public ExpressionVector(ExpressionCalculator expressionCalculator, String xValue, String yValue, int index) {
        this.index = index;
        this.expressionCalculator = expressionCalculator;
        setX(xValue);
        setY(yValue);
    }


    @Override
    public String toString() {
        return "ExpressionVector{" +
                "xExpression=" + xExpression +
                ", yExpression=" + yExpression +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(String value) {
        xExpression = value;
        String[] values = value.split("\\$");
        if (values.length >= 2) {
            x = expressionCalculator.calculateX(values[0]) + expressionCalculator.calculateX(values[1]) * index;
        } else {
            x = expressionCalculator.calculateX(value);
        }
        w = x;

    }

    public double getY() {
        return y;
    }

    public void setY(String value) {
        yExpression = value;
        String[] values = value.split("\\$");
        if (values.length >= 2) {
            h = expressionCalculator.calculateY(values[0]) + expressionCalculator.calculateX(values[1]) * index;
        } else {
            h = expressionCalculator.calculateY(value);
        }
        y = expressionCalculator.getCanvasHeight() - h;
    }


    public double getWidth() {
        return w;
    }


    public double getHeight() {
        return h;
    }

}
