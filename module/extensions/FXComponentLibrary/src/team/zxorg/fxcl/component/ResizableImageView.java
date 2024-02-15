package team.zxorg.fxcl.component;

import javafx.scene.image.ImageView;

public class ResizableImageView extends ImageView {
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double minWidth(double height) {
        return 1D;
    }

    @Override
    public double minHeight(double width) {
        return 1D;
    }
    @Override
    public double prefWidth(double height) {
        return 0;
    }

    @Override
    public double prefHeight(double width) {
        return 0;
    }
    @Override
    public void resize(double width, double height) {
        this.setFitWidth(width);
        this.setFitHeight(height);
    }
}
