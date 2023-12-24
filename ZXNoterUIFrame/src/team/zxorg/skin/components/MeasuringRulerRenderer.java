package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.ElementRenderer;

public class MeasuringRulerRenderer implements ElementRenderer {
    public int setPos = 0;
    public int state = 0;
    double pos1X, pos1Y = 0;
    double pos2X, pos2Y = 0;

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
    }


    @Override
    public void render(GraphicsContext gc, double width, double height) {
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
        gc.restore();
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }
}
