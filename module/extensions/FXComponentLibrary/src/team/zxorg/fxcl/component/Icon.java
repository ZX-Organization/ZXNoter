package team.zxorg.fxcl.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import team.zxorg.fxcl.resource.IconManager;

public class Icon extends StackPane {
    ResizableImageView imageView = new ResizableImageView();
    StringProperty iconKeyProperty = new SimpleStringProperty();
    ObjectProperty<Image> icon = new SimpleObjectProperty<>();
    ObjectProperty<Color> fillColorProperty = new SimpleObjectProperty<>(null);
    Shadow shadow = new Shadow() {
        {
            setWidth(0);
            setHeight(0);
        }
    };

    public ObjectProperty<Color> fillColorProperty() {
        return fillColorProperty;
    }

    public ObjectProperty<Color> fillColorPropertyProperty() {
        return fillColorProperty;
    }

    public StringProperty iconKeyProperty() {
        return iconKeyProperty;
    }

    public StringProperty iconKeyPropertyProperty() {
        return iconKeyProperty;
    }

    public String getIconKey() {
        return iconKeyProperty.get();
    }

    public Icon(String key) {
        getChildren().add(imageView);
        getStyleClass().add("icon");
        iconKeyProperty.addListener((observable, oldValue, newValue) -> {
            if (icon != null)
                icon.removeListener(iconChangeListener);
            icon = IconManager.getIcon(newValue);
            icon.addListener(iconChangeListener);
            imageView.setImage(icon.get());
        });
        fillColorProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                shadow.setColor(newValue);
                setEffect(shadow);
            } else {
                setEffect(null);
            }
        });

        iconKeyProperty.set(key);
    }

    public Icon(String key, double size) {
        this(key);
        setSize(size, size);
    }

    public Icon(String key, double size, Color color) {
        this(key, size);
        setColor(color);
    }

    private final ChangeListener<Image> iconChangeListener = (observable, oldValue, newValue) -> {
        imageView.setImage(newValue);
    };

    public void setSize(double width, double height) {
        setPrefSize(width, height);
        setMinSize(width, height);
        setMaxSize(width, height);
    }

    public void setColor(Color color) {
        fillColorProperty.set(color);
    }
}
