package team.zxorg.skin.components;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import team.zxorg.skin.ExpressionVector;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.basis.RenderRectangle;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TouchComponent implements ElementRender {
    int type;
    ArrayList<ElementRender> subRenderList = new ArrayList<ElementRender>();

    public TouchComponent(HashMap<String, String> properties, Path uisPath) {
        type = Integer.parseInt(properties.get("type"));

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (!entry.getKey().contains("type")) {
                subRenderList.add(
                        switch (type) {
                            case 1 -> new TouchSquare(entry.getValue());
                            case 2 -> new TouchRound(entry.getValue());
                            case 3 -> new TouchTriangle(entry.getValue());
                            default -> throw new IllegalStateException("Unexpected value: " + type);
                        }
                );
            }
        }


    }

    @Override
    public void render(GraphicsContext gc, double width, double height) {
        for (ElementRender render : subRenderList)
            render.render(gc, width, height);
    }

    @Override
    public void canvasResized(double width, double height, Orientation orientation) {

    }

    private class TouchSquare implements ElementRender {
        ExpressionVector pos;
        ExpressionVector size;
        RenderRectangle rr = new RenderRectangle();

        public TouchSquare(String v) {
            String[] str = v.split(",");
            if (str.length != 4)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(str[0].trim(), str[1].trim());
            size = new ExpressionVector(str[2].trim(), str[3].trim());
        }

        @Override
        public void render(GraphicsContext gc, double width, double height) {
            gc.setStroke(Color.PINK);
            rr.setPos(Pos.BOTTOM_LEFT, pos.getX(), pos.getY());
            rr.setSize(Pos.BOTTOM_LEFT, size.getWidth(), size.getHeight());
            gc.strokeRect(rr.getLeft(), rr.getTop(), rr.getWidth(), rr.getHeight());
        }

        @Override
        public void canvasResized(double width, double height, Orientation orientation) {

        }
    }

    private class TouchRound implements ElementRender {
        ExpressionVector pos;
        ExpressionVector size;

        public TouchRound(String v) {
            String[] str = v.split(",");
            if (str.length != 4)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(str[0].trim(), str[1].trim());
            size = new ExpressionVector(str[2].trim(), str[3].trim());
        }

        @Override
        public void render(GraphicsContext gc, double width, double height) {
            gc.setStroke(Color.PINK);
            gc.strokeRoundRect(pos.getX(), pos.getX(), pos.getX() + size.getWidth(), pos.getY() + size.getHeight(), 100, 100);
        }

        @Override
        public void canvasResized(double width, double height, Orientation orientation) {

        }
    }

    private class TouchTriangle implements ElementRender {
        ExpressionVector pos;
        ExpressionVector pos2;
        ExpressionVector pos3;

        public TouchTriangle(String v) {
            String[] str = v.split(",");
            if (str.length != 6)
                throw new IllegalArgumentException("Invalid number of arguments");
            pos = new ExpressionVector(str[0].trim(), str[1].trim());
            pos2 = new ExpressionVector(str[2].trim(), str[3].trim());
            pos3 = new ExpressionVector(str[4].trim(), str[5].trim());
        }

        @Override
        public void render(GraphicsContext gc, double width, double height) {
            gc.setStroke(Color.PINK);
            gc.strokeLine(pos.getX(), pos.getY(), pos2.getX(), pos2.getY());
            gc.strokeLine(pos2.getX(), pos2.getY(), pos3.getX(), pos3.getY());
            gc.strokeLine(pos3.getX(), pos3.getY(), pos.getX(), pos.getY());
        }

        @Override
        public void canvasResized(double width, double height, Orientation orientation) {

        }
    }

}
