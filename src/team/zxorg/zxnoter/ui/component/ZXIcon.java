package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ZXIcon extends Pane {

    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    public StringProperty iconKey = new SimpleStringProperty();


    public static final HashMap<String, ZXFileIcon> fileIconMap = new HashMap<>();

    static {
        for (ZXFileIcon icon : ZXFileIcon.values()) {
            fileIconMap.put(icon.extensionName, icon);
        }
    }

    public ZXIcon(ZXFileIcon zxFileIcon,double size) {
        setIconKey(zxFileIcon.iconKey);
        setColor(zxFileIcon.color);
        setSize(size);
    }


    public static ZXFileIcon getFileIcon(Path file) {
        if (Files.isDirectory(file))
            return ZXFileIcon.directory;
        String extensionName = file.getFileName().toString();
        extensionName = extensionName.substring(extensionName.lastIndexOf(".") + 1);
        extensionName = extensionName.toLowerCase();
        ZXFileIcon returnIcon = fileIconMap.get(extensionName);
        if (returnIcon != null)
            return returnIcon;
        return ZXFileIcon.unknown;
    }


    public ZXIcon() {
    }

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
