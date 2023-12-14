package team.zxorg.skin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExpressionVector {
    public static final ExpressionCalculator expressionCalculator = new ExpressionCalculator();
    private final StringProperty xExpression = new SimpleStringProperty();
    private final StringProperty yExpression = new SimpleStringProperty();
    private double x = 0;
    private double y = 0;

    @Override
    public String toString() {
        return "ExpressionVector{" +
                "xExpression=" + xExpression +
                ", yExpression=" + yExpression +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public ExpressionVector() {
        this("", "");
    }

    public ExpressionVector(String xValue, String yValue) {
        xExpression.addListener((observable, oldValue, newValue) -> {
            x = expressionCalculator.calculateX(newValue);
        });
        yExpression.addListener((observable, oldValue, newValue) -> {
            y = expressionCalculator.calculateY(newValue);
        });
        expressionCalculator.addCanvasSizeChangeEvent((orientation) -> {
            switch (orientation) {
                case HORIZONTAL -> {
                    x = expressionCalculator.calculateX(xExpression.getValue());
                }
                case VERTICAL -> {
                    y = expressionCalculator.calculateY(yExpression.getValue());
                }
            }
        });
        setX(xValue);
        setY(yValue);
    }

    public static ExpressionVector parse(String expression) {
        if (expression == null) return null;
        String[] str = expression.split(",");
        if (str.length == 1) return new ExpressionVector(str[0].trim(), "");
        if (str.length >= 2) return new ExpressionVector(str[0].trim(), str[1].trim());
        return null;
    }

    public double getX() {
        return x;
    }

    public void setX(String value) {
        xExpression.setValue(value);
    }

    public double getY() {
        return expressionCalculator.getCanvasHeight()-y;
    }

    public void setY(String value) {
        yExpression.setValue(value);
    }

    public double getWidth() {
        return x;
    }

    public void setWidth(String value) {
        xExpression.setValue(value);
    }

    public double getHeight() {
        return y;
    }

    public void setHeight(String value) {
        yExpression.setValue(value);
    }

}
