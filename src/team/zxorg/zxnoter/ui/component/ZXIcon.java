package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.ZXFileType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ZXIcon extends Pane {

    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    public StringProperty iconKey = new SimpleStringProperty();


    public static final HashMap<String, ZXFileType> fileIconMap = new HashMap<>();

    static {
        for (ZXFileType icon : ZXFileType.values()) {
            fileIconMap.put(icon.extensionName, icon);
        }
    }

    public ZXIcon(ZXFileType zxFileIcon, double size) {
        setIconKey(zxFileIcon.iconKey);
        setColor(zxFileIcon.type.color);
        setSize(size);
    }


    public static ZXFileType getFileType(Path file) {
        if (Files.isDirectory(file))
            return ZXFileType.directory;
        String extensionName = file.getFileName().toString();
        extensionName = extensionName.substring(extensionName.lastIndexOf(".") + 1);
        extensionName = extensionName.toLowerCase();
        ZXFileType returnIcon = fileIconMap.get(extensionName);
        if (returnIcon != null)
            return returnIcon;
        return ZXFileType.unknown;
    }

    public static ZXFileType getFileType(String file) {
        String extensionName = file;
        extensionName = extensionName.substring(extensionName.lastIndexOf(".") + 1);
        extensionName = extensionName.toLowerCase();
        ZXFileType returnIcon = fileIconMap.get(extensionName);
        if (returnIcon != null)
            return returnIcon;
        return ZXFileType.unknown;
    }


    public ZXIcon() {
    }

    {
        getStyleClass().add("icon");
        iconKey.addListener((observable, oldValue, newValue) -> {
            shapeProperty().bind(GlobalResources.getIcon(newValue));
        });
        color.addListener((observable, oldValue, newValue) -> {
            getStyleClass().removeAll(getStyleClass().filtered(s -> s.contains("bg-")));
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
