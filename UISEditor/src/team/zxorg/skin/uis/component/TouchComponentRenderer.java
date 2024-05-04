package team.zxorg.skin.uis.component;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import team.zxorg.skin.basis.RenderInterface;
import team.zxorg.skin.basis.RenderRectangle;
import team.zxorg.skin.uis.ExpressionCalculator;
import team.zxorg.skin.uis.ExpressionVector;
import team.zxorg.skin.uis.UISComponent;

import java.util.ArrayList;

public class TouchComponentRenderer extends AbstractComponentRenderer {

    ArrayList<RenderInterface> subRenderList;

    public TouchComponentRenderer(UISComponent component) {
        super(component);

    }

    @Override
    void reloadResComponent() {
        zindex = 1000;
        color = null;
    }


    @Override
    void reloadPosComponent() {
        subRenderList = new ArrayList<>();
        String find = switch (type) {
            case 1 -> "rect";
            case 2 -> "circle";
            default -> "array";
        };
        for (String name : component.findProperties(find)) {
            String value = component.getString(name, "");
            subRenderList.add(
                    switch (type) {
                        case 1 -> new TouchSquare(component.expressionCalculator, value);
                        case 2 -> new TouchCircle(component.expressionCalculator, value);
                        case 3 -> new TouchTriangle(component.expressionCalculator, value);
                        default -> throw new IllegalStateException("Unexpected value: " + type);
                    }
            );
        }
    }

    @Override
    void drawComponent( double width, double height,long time) {
        gc.restore();
        gc.setGlobalAlpha(0.5);
        gc.setLineWidth(1);
        gc.setFill(Color.PINK);
        gc.setStroke(Color.PINK);
        for (RenderInterface render : subRenderList)
            render.draw(gc, width, height,time);
    }

    /**
     *
     */
    private static class TouchSquare implements RenderInterface {
        ExpressionVector pos;
        ExpressionVector size;
        RenderRectangle rr = new RenderRectangle();

        public TouchSquare(ExpressionCalculator ec, String v) {
            String[] str = v.split(",");
            if (str.length < 4)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(ec, str[0].trim(), str[1].trim(), 0);
            size = new ExpressionVector(ec, str[2].trim(), str[3].trim(), 0);
        }

        @Override
        public void draw(GraphicsContext gc, double width, double height,long time) {

            rr.setPos(Pos.BOTTOM_LEFT, pos.getX(), pos.getY());
            rr.setSize(Pos.BOTTOM_LEFT, size.getW(), size.getH());
            gc.strokeRect(rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
        }

    }

    /**
     * 圆形区域
     */
    private static class TouchCircle implements RenderInterface {
        ExpressionVector pos;
        ExpressionVector size;

        public TouchCircle(ExpressionCalculator ec, String v) {
            String[] str = v.split(",");
            if (str.length < 4)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(ec, str[0].trim(), str[1].trim(), 0);
            size = new ExpressionVector(ec, str[2].trim(), str[3].trim(), 0);
        }

        @Override
        public void draw(GraphicsContext gc, double width, double height,long time) {
            gc.strokeOval(pos.getX(), pos.getY(), size.getW(), size.getH());
        }

    }

    /**
     * 三角形区域
     * array
     */
    private static class TouchTriangle implements RenderInterface {
        ExpressionVector pos;
        ExpressionVector pos2;
        ExpressionVector pos3;

        public TouchTriangle(ExpressionCalculator ec, String v) {
            String[] str = v.split(",");
            if (str.length < 6)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(ec, str[0].trim(), str[1].trim(), 0);
            pos2 = new ExpressionVector(ec, str[2].trim(), str[3].trim(), 0);
            pos3 = new ExpressionVector(ec, str[4].trim(), str[5].trim(), 0);
        }

        @Override
        public void draw(GraphicsContext gc, double width, double height,long time) {
            gc.strokeLine(pos.getX(), pos.getY(), pos2.getX(), pos2.getY());
            gc.strokeLine(pos2.getX(), pos2.getY(), pos3.getX(), pos3.getY());
            gc.strokeLine(pos3.getX(), pos3.getY(), pos.getX(), pos.getY());
        }


    }
}
