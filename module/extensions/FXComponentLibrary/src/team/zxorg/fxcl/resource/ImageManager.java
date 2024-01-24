package team.zxorg.fxcl.resource;

import com.github.weisj.jsvg.parser.SVGLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.event.ResourceEventListener;

import java.nio.file.Path;
import java.util.HashMap;

public class ImageManager {
    public static final Image UNKNOWN_ICON = new Image(IconManager.class.getResource("/assets/fxComponentLibrary/unknown.png").toString());
    public static HashMap<String, ObjectProperty<Image>> icons = new HashMap<>();

    static {
        Logger.info(Language.get(LanguageKey.MESSAGE_ICON_INITIALIZE));
        Resource.addEventListener(new ResourceEventListener() {
            @Override
            public void onReload() {
                reloadImages();
            }
        });
        reloadImages();
    }

    public static void reloadImages() {
        Logger.info(Language.get(LanguageKey.MESSAGE_ICON_LOADING));
        for (Path dir : Resource.listResourceFiles("icon")) {
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
    }


    public static ObjectProperty<Image> getImage(String key) {
        return icons.computeIfAbsent(key, k -> new SimpleObjectProperty<>(UNKNOWN_ICON));
    }
}
