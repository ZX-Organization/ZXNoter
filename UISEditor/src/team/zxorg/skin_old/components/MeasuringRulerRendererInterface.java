package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRenderInterface;
import team.zxorg.skin.uis.UISEditor;

public class MeasuringRulerRendererInterface implements ElementRenderInterface {
    public int setPos = 0;
    public int state = 0;
    double pos1X, pos1Y = 0;
    double pos2X, pos2Y = 0;
    double width, height;
    public UISEditor.UnitInfo unit;

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

        lengthStr = getStr(length);
        pos1Str = getStr(pos1X) + "\n" + getStr(height - pos1Y);
        pos2Str = getStr(pos2X) + "\n" + getStr(height - pos2Y);
    }

    public String getStr(double v) {
        double relativeMagnification = ExpressionVector.expressionCalculator.getPixelMagnification();
        return switch (unit.getId()) {
            case 1 -> ((int) (v * 100)) / 100f + unit.getUnit();
            case 2 -> v + unit.getUnit();
            default -> ((int) (v / relativeMagnification * 100)) / 100f + unit.getUnit();
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

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }
}