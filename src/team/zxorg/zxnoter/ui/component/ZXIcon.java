package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;

public class ZXIcon extends Pane {

    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    public StringProperty iconKey = new SimpleStringProperty();

    {
        getStyleClass().add("icon");
        iconKey.addListener((observable, oldValue, newValue) -> {
            shapeProperty().bind(GlobalResources.getIcon(newValue));
        });
        color.addListener((observable, oldValue, newValue) -> {
            getStyleClass().filtered(s -> s.contains("bg-")).clear();
            getStyleClass().add("bg-" + newValue);
        });
    }


    public void setSize(double size) {
        setPrefSize(size, size);
        setMaxSize(size, size);
        setMinSize(size, size);
    }

    public void setIconKey(String key) {
        iconKey.set(key);
    }

    public void setColor(ZXColor zxColor) {
        color.set(zxColor);
    }
}
