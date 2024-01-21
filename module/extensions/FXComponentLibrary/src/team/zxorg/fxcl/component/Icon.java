package team.zxorg.fxcl.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class Icon extends Pane {
    StringProperty iconKey = new SimpleStringProperty();
    ObjectProperty<Color> iconColor = new SimpleObjectProperty<>(Color.TRANSPARENT);

    public Icon() {
        iconKey.addListener((observable, oldValue, newValue) -> {

        });
    }

    public void setSize(int width, int height) {
        setMaxSize(width, height);
        setMinSize(width, height);
    }

    public void setColor(Color color) {
        setBackground(Background.fill(color));
    }
}
