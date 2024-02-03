package team.zxorg.fxcl.resource;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.geometry.size.FloatSize;
import com.github.weisj.jsvg.parser.SVGLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.LanguageKey;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.Resource;
import team.zxorg.extensionloader.event.ResourceEventListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class IconManager {
    public static final Image UNKNOWN_ICON = new Image(IconManager.class.getResource("/assets/fxComponentLibrary/unknown.png").toString());
    public static HashMap<String, ObjectProperty<Image>> icons = new HashMap<>();

    static {
        Logger.info(Language.get(LanguageKey.MESSAGE_ICON_INITIALIZE));
        Resource.addEventListener(new ResourceEventListener() {
            @Override
            public void onReload() {
                reloadIcons();
            }
        });
        reloadIcons();
    }

    public static void reloadIcons() {
        Logger.info(Language.get(LanguageKey.MESSAGE_ICON_LOADING));
        SVGLoader svgLoader = new SVGLoader();
        for (Path dir : Resource.listResourceFiles("icon")) {
            for (Path file : Resource.listResourceFiles(dir)) {
                Image icon;
                String filename = file.getFileName().toString();
                String key = dir.getFileName() + "." + filename.substring(0, filename.lastIndexOf("."));
                if (file.toString().endsWith(".svg")) {
                    SVGDocument svgDocument = svgLoader.load(Resource.getResourceToInputStream(file));
                    FloatSize size = svgDocument.size();
                    size.setSize(256, 256);

                    BufferedImage image = new BufferedImage((int) size.width, (int) size.height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = image.createGraphics();
                    svgDocument.render(null, g, new ViewBox(size));
                    g.dispose();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "png", outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    icon = new Image(new ByteArrayInputStream(outputStream.toByteArray()), 0, 0, true, true);
                } else {
                    icon = new Image(Resource.getResourceToInputStream(file), 0, 0, true, true);
                }
                ObjectProperty<Image> iconProperty = icons.get(key);
                if (iconProperty == null) {
                    icons.put(key, new SimpleObjectProperty<>(icon));
                } else {
                    iconProperty.set(icon);
                }
            }
        }
    }


    public static ObjectProperty<Image> getIcon(String key) {
        return icons.computeIfAbsent(key, k -> new SimpleObjectProperty<>(UNKNOWN_ICON));
    }
}
