package team.zxorg.skin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExpressionVector {
    public final ExpressionCalculator expressionCalculator;
    private final StringProperty xExpression = new SimpleStringProperty();
    private final StringProperty yExpression = new SimpleStringProperty();

    private double x = 0;
    private double y = 0;
    private double w = 0;
    private double h = 0;

    @Override
    public String toString() {
        return "ExpressionVector{" +
                "xExpression=" + xExpression +
                ", yExpression=" + yExpression +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public ExpressionVector(ExpressionCalculator expressionCalculator) {
        this("", "", expressionCalculator);
    }

    public ExpressionVector(String xValue, String yValue, ExpressionCalculator expressionCalculator) {
        this.expressionCalculator = expressionCalculator;
        xExpression.addListener((observable, oldValue, newValue) -> {
            String[] values = newValue.split("\\$");
            if (values.length >= 3) {
                //有偏移
                x = expressionCalculator.calculateX(values[0] + "+" + expressionCalculator.calculateX(values[1]) * Integer.parseInt(values[2]) + "px");
            } else {
                x = expressionCalculator.calculateX(newValue);
                w = expressionCalculator.calculateW(newValue);
            }

        });
        yExpression.addListener((observable, oldValue, newValue) -> {
            String[] values = newValue.split("\\$");
            if (values.length >= 3) {
                //有偏移
                y = expressionCalculator.calculateY(values[0] + "+" + expressionCalculator.calculateX(values[1]) * Integer.parseInt(values[2]) + "px");
            } else {
                y = expressionCalculator.calculateY(newValue);
                h = expressionCalculator.calculateH(newValue);
            }

        });
        /*expressionCalculator.addCanvasSizeChangeEvent((orientation) -> {
            switch (orientation) {
                case HORIZONTAL -> {
                    x = expressionCalculator.calculateX(xExpression.getValue());
                    w = expressionCalculator.calculateW(xExpression.getValue());
                }
                case VERTICAL -> {
                    y = expressionCalculator.calculateY(yExpression.getValue());
                    h = expressionCalculator.calculateH(yExpression.getValue());
                }
            }
        });*/
        setX(xValue);
        setY(yValue);
    }

    public static ExpressionVector parse(String expression, ExpressionCalculator expressionCalculator) {
        if (expression == null) return null;
        String[] str = expression.split(",");
        if (str.length == 1) return new ExpressionVector(str[0].trim(), "", expressionCalculator);
        if (str.length == 2) return new ExpressionVector(str[0].trim(), str[1].trim(), expressionCalculator);
        if (str.length == 3) {
            int index = Integer.parseInt(str[2].trim()) - 1;
            String x = str[0].trim();
            String y = str[1].trim();
            if (x.contains("$"))
                x += "$" + index;
            if (y.contains("$"))
                y += "$" + index;
            return new ExpressionVector(x, y, expressionCalculator);
        }
        return null;
    }

    public double getX() {
        return x;
    }

    public void setX(String value) {
        xExpression.setValue(value);
    }

    public void setY(String value) {
        yExpression.setValue(value);
    }

    public double getY() {
        return y;
    }


    public double getWidth() {
        return w;
    }


    public double getHeight() {
        return h;
    }

}
