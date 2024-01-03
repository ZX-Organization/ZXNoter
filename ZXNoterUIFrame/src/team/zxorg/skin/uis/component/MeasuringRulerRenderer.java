package team.zxorg.skin.uis.component;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.skin.basis.RenderInterface;
import team.zxorg.skin.uis.ExpressionCalculator;
import team.zxorg.skin.uis.ui.UISEditor;

public class MeasuringRulerRenderer implements RenderInterface {
    ExpressionCalculator expressionCalculator;

    public MeasuringRulerRenderer(ExpressionCalculator expressionCalculator) {
        this.expressionCalculator = expressionCalculator;
    }

    public int setPos = 0;
    public int state = 0;
    double pos1X, pos1Y = 0;
    double pos2X, pos2Y = 0;
    double width, height;
    public UISEditor.UnitInfo unit;

    @Override
    public void initialize(Canvas canvas) {
        canvas.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                setPos = 1;
            } else {
                setPos = 2;
            }
        });
        canvas.setOnMouseDragged(event -> {
            if (event.isShiftDown()) {
                state = 1;
            } else if (event.isAltDown()) {
                state = 2;
            } else {
                state = 0;
            }
            switch (setPos) {
                case 1 -> setPos1(event.getX(), event.getY());
                case 2 -> setPos2(event.getX(), event.getY());
            }
        });
        canvas.setOnMouseReleased(event -> setPos = 0);
    }

    public void setPos1(double x, double y) {
        this.pos1X = x;
        this.pos1Y = y;
        //水平 垂直 对齐pos2
        if (state == 1) {
            if (Math.abs(pos1X - pos2X) > Math.abs(pos1Y - pos2Y)) {
                pos1Y = pos2Y;
            } else {
                pos1X = pos2X;
            }
        } else if (state == 2) {
            this.pos1X = Math.round(x);
            this.pos1Y = Math.round(y);
        }
        updatePos();
    }

    public void setPos2(double x, double y) {
        this.pos2X = x;
        this.pos2Y = y;
        //水平 垂直 对齐pos2
        if (state == 1) {
            if (Math.abs(pos1X - pos2X) > Math.abs(pos1Y - pos2Y)) {
                pos2Y = pos1Y;
            } else {
                pos2X = pos1X;
            }
        }else if (state == 2) {
            this.pos2X = Math.round(x);
            this.pos2Y = Math.round(y);
        }
        updatePos();
    }

    /**
     * 长度显示
     */
    private String lengthStr = "";
    String pos1Str = "";
    String pos2Str = "";


    private void updatePos() {
        double length = calculateEuclideanDistance(pos1X, pos1Y, pos2X, pos2Y);

        lengthStr = getStr(length, true);
        pos1Str = getStr(pos1X, true) + "\n" + getStr(height - pos1Y, false);
        pos2Str = getStr(pos2X, true) + "\n" + getStr(height - pos2Y, false);
    }

    public String getStr(double v, boolean isHorizontal) {
        double width = expressionCalculator.getCanvasWidth();
        double height = expressionCalculator.getCanvasHeight();
        double relativeMagnification = expressionCalculator.getPixelMagnification();
        return switch (unit.id()) {
            case 1 -> (int) (v * 100) / 100f + unit.unit();
            case 2 -> (int) (v / (isHorizontal ? width : height) * 10000) / 100f + unit.unit();
            default -> (int) (v / relativeMagnification * 100) / 100f + unit.unit();
        };
    }


    @Override
    public void draw(GraphicsContext gc, double width, double height) {
        this.width = width;
        this.height = height;
        gc.save();
        gc.setLineWidth(1);
        gc.setStroke(Color.BLUE);
        gc.strokeLine(pos1X, pos1Y, pos2X, pos2Y);
        gc.setLineWidth(3);
        gc.setStroke(Color.RED);
        gc.strokeLine(pos1X - 5, pos1Y, pos1X + 5, pos1Y);
        gc.strokeLine(pos1X, pos1Y - 5, pos1X, pos1Y + 5);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(pos2X - 5, pos2Y, pos2X + 5, pos2Y);
        gc.strokeLine(pos2X, pos2Y - 5, pos2X, pos2Y + 5);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);


        gc.fillText(lengthStr, (pos1X + pos2X) / 2, (pos1Y + pos2Y) / 2);


        gc.fillText(pos1Str, pos1X, pos1Y);
        gc.fillText(pos2Str, pos2X, pos2Y);
        gc.restore();
    }

    public static double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
