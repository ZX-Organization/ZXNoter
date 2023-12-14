package team.zxorg.skin.test.uis;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import team.zxorg.skin.test.ExpressionCalculator;

public class ExpressionDimension {
    public final static ExpressionCalculator expressionCalculator = new ExpressionCalculator();
    private final StringProperty xExpression = new SimpleStringProperty();
    private final StringProperty yExpression = new SimpleStringProperty();
    private double x = 0;
    private double y = 0;

    public ExpressionDimension() {
        this("", "");
    }

    public ExpressionDimension(String xValue, String yValue) {
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
    }

    public void setX(String value) {
        xExpression.setValue(value);
    }

    public void setY(String value) {
        yExpression.setValue(value);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
