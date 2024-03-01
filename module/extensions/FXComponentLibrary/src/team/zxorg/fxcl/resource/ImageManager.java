package team.zxorg.fxcl.resource;

import com.github.weisj.jsvg.parser.SVGLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import team.zxorg.extensionloader.core.*;
import team.zxorg.extensionloader.event.ResourceEventListener;

import java.nio.file.Path;
import java.util.HashMap;

public class ImageManager {
    public static final Image UNKNOWN_ICON = new Image(IconManager.class.getResource("/assets/fxComponentLibrary/unknown.png").toString());
    public static HashMap<String, ObjectProperty<Image>> icons = new HashMap<>();

    static {
        Resource.addEventListener(new ResourceEventListener() {
            @Override
            public void onReload() {
                reloadImages();
            }
        });
    }

    public static void reloadImages() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Logger.info(Language.get("message.image.loading"));
        for (Path dir : Resource.listResourceFiles("texture")) {
            for (Path file : Resource.listResourceFiles(dir)) {
                Image icon;
                String filename = file.getFileName().toString();
                String key = dir.getFileName() + "." + filename.substring(0, filename.lastIndexOf("."));
                icon = new Image(Resource.getResourceToInputStream(file));
                ObjectProperty<Image> iconProperty = icons.get(key);
                if (iconProperty == null) {
                    icons.put(key, new SimpleObjectProperty<>(icon));
                } else {
                    iconProperty.set(icon);
                }
            }
        }
        stopWatch.stop();
        Logger.info(Language.get("message.image.loaded", stopWatch.getTime()));

    }


    public static ObjectProperty<Image> getImage(String key) {
        return icons.computeIfAbsent(key, k -> new SimpleObjectProperty<>(UNKNOWN_ICON));
    }
}
